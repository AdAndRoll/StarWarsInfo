package com.example.data.repository

import android.util.Log
import com.example.data.local.dao.CharacterDetailsDao
import com.example.data.local.datasources.FilmLocalDataSource
import com.example.data.mappers.toCharacterDetailsEntity
import com.example.data.mappers.toFilmEntity
import com.example.data.mappers.toFilmSummary
import com.example.data.mappers.toSWFilm
import com.example.data.mappers.toSwapiId
import com.example.data.remote.datasources.CharacterRemoteDataSource
import com.example.data.remote.datasources.FilmRemoteDataSource
import com.example.data.utils.NetworkResult
import com.example.domain.model.SWFilm
import com.example.domain.model.SWFilmSummary
import com.example.domain.model.SWCharacterSummary
import com.example.domain.repository.FilmRepository
import com.example.domain.utils.Result
import kotlinx.coroutines.async
import kotlinx.coroutines.awaitAll
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class FilmRepositoryImpl @Inject constructor(
    private val remoteDataSource: FilmRemoteDataSource,
    private val localDataSource: FilmLocalDataSource,
    private val characterRemoteDataSource: CharacterRemoteDataSource,
    private val characterDetailsDao: CharacterDetailsDao
) : FilmRepository {

    private val TAG = "FilmRepositoryImpl"

    override fun getFilmDetails(filmId: String): Flow<Result<SWFilm>> = flow {
        Log.d(TAG, "Fetching film ID: $filmId")

        // 1. Сначала данные из кэша
        val localData = localDataSource.getFilm(filmId)
        if (localData != null) {
            val characters = getCharacters(localData.characterUrls)
            emit(Result.Success(localData.toSWFilm(characters)))
        }

        // 2. Сетевой запрос для актуализации
        when (val networkResult = remoteDataSource.getFilm(filmId)) {
            is NetworkResult.Success -> {
                val filmDto = networkResult.data
                localDataSource.insertFilm(filmDto.toFilmEntity())

                // Загружаем персонажей фильма в кэш
                val characterIds = filmDto.characterUrls.map { it.toSwapiId() }
                fetchAndSaveCharacters(characterIds)

                val characters = getCharacters(filmDto.characterUrls)
                emit(Result.Success(filmDto.toSWFilm(characters)))
            }
            is NetworkResult.Error -> {
                if (localData == null) emit(Result.Error(networkResult.exception))
            }
        }
    }

    /**
     * Получает список кратких описаний фильмов.
     * В SWAPI нет метода "несколько по ID", поэтому делаем параллельные запросы.
     */
    override suspend fun getFilmsSummariesByIds(ids: List<String>): Result<List<SWFilmSummary>> = coroutineScope {
        try {
            val results = ids.map { id ->
                async { remoteDataSource.getFilm(id) }
            }.awaitAll()

            val summaries = results.mapNotNull {
                (it as? NetworkResult.Success)?.data?.toFilmSummary()
            }

            Result.Success(summaries)
        } catch (e: Exception) {
            Result.Error(e)
        }
    }

    /**
     * Вспомогательный метод: собирает из БД список персонажей (ID + Имя)
     */
    private suspend fun getCharacters(characterUrls: List<String>): List<SWCharacterSummary> {
        return characterUrls.mapNotNull { url ->
            val id = url.toSwapiId()
            characterDetailsDao.getCharacterDetailsById(id)?.let {
                SWCharacterSummary(id = it.id, name = it.name)
            }
        }
    }

    /**
     * Загружает детали персонажей и сохраняет их в БД, чтобы при просмотре фильма
     * мы видели имена героев, а не просто ссылки.
     */
    private suspend fun fetchAndSaveCharacters(characterIds: List<String>) = coroutineScope {
        characterIds.map { id ->
            async {
                // Проверяем, нет ли его уже в базе, чтобы не спамить запросами
                if (characterDetailsDao.getCharacterDetailsById(id) == null) {
                    val result = characterRemoteDataSource.getCharacterById(id)
                    if (result is NetworkResult.Success) {
                        characterDetailsDao.insertCharacterDetails(result.data.toCharacterDetailsEntity())
                    }
                }
            }
        }.awaitAll()
    }
}