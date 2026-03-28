package com.example.data.repository

import android.util.Log
import com.example.data.local.dao.CharacterDetailsDao
import com.example.data.local.datasources.PlanetLocalDataSource
import com.example.data.mappers.toCharacterDetailsEntity
import com.example.data.mappers.toDomainModel // ОБЯЗАТЕЛЬНО проверь этот импорт
import com.example.data.mappers.toEntity
import com.example.data.mappers.toSwapiId
import com.example.data.remote.datasources.CharacterRemoteDataSource
import com.example.data.remote.datasources.PlanetRemoteDataSource
import com.example.data.utils.NetworkResult
import com.example.domain.model.SWPlanetDetail
import com.example.domain.model.SWCharacterSummary
import com.example.domain.repository.PlanetRepository
import com.example.domain.utils.Result
import kotlinx.coroutines.async
import kotlinx.coroutines.awaitAll
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class PlanetRepositoryImpl @Inject constructor(
    private val planetRemoteDataSource: PlanetRemoteDataSource,
    private val planetLocalDataSource: PlanetLocalDataSource,
    private val characterRemoteDataSource: CharacterRemoteDataSource,
    private val characterDetailsDao: CharacterDetailsDao
) : PlanetRepository {

    private val TAG = "PlanetRepositoryImpl"

    override fun getPlanetDetails(planetId: String): Flow<Result<SWPlanetDetail>> = flow {
        // 1. Пытаемся взять из кэша
        val cleanId = planetId.toSwapiId()
        Log.d(TAG, "Fetching film ID: $cleanId")
        val localDetails = planetLocalDataSource.getPlanetDetails(cleanId)

        if (localDetails != null) {
            // Используем residentUrls (из твоей Entity)
            val cachedResidents = getResidents(localDetails.residentUrls)
            // Маппим в доменную модель (Candidate #2 в твоем логе)
            emit(Result.Success(localDetails.toDomainModel(cachedResidents)))
        }

        // 2. Идем в сеть
        when (val remoteResult = planetRemoteDataSource.getPlanetDetails(cleanId)) {
            is NetworkResult.Success -> {
                val planetDto = remoteResult.data

                // Сохраняем в кэш
                planetLocalDataSource.savePlanetDetails(planetDto.toEntity())

                // Загружаем жителей
                val residentIds = planetDto.residentUrls.map { it.toSwapiId() }
                fetchAndSaveCharacters(residentIds)

                // Получаем актуальный список жителей из БД
                val finalResidents = getResidents(planetDto.residentUrls)

                // Маппим DTO в домен (Candidate #3 в твоем логе)
                emit(Result.Success(planetDto.toDomainModel(finalResidents)))
            }

            is NetworkResult.Error -> {
                if (localDetails == null) {
                    emit(Result.Error(remoteResult.exception))
                }
            }
        }
    }

    private suspend fun getResidents(urls: List<String>): List<SWCharacterSummary> {
        return urls.mapNotNull { url ->
            val id = url.toSwapiId()
            characterDetailsDao.getCharacterDetailsById(id)?.let {
                SWCharacterSummary(id = it.id, name = it.name)
            }
        }
    }

    private suspend fun fetchAndSaveCharacters(ids: List<String>) = coroutineScope {
        ids.map { id ->
            async {
                if (characterDetailsDao.getCharacterDetailsById(id) == null) {
                    val res = characterRemoteDataSource.getCharacterById(id)
                    if (res is NetworkResult.Success) {
                        characterDetailsDao.insertCharacterDetails(res.data.toCharacterDetailsEntity())
                    }
                }
            }
        }.awaitAll()
    }
}