package com.example.data.repository

import android.util.Log
import androidx.paging.ExperimentalPagingApi
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import androidx.paging.map
import com.example.data.local.database.StarWarsDatabase
import com.example.data.local.datasources.CharacterDetailsLocalDataSource
import com.example.data.local.datasources.CharacterLocalDataSource
import com.example.data.mappers.toCharacter
import com.example.data.mappers.toCharacterDetailsRaw
import com.example.data.mappers.toCharacterDetailsEntity
import com.example.data.remote.datasources.CharacterRemoteDataSource
import com.example.data.remote.mediator.CharacterRemoteMediator
import com.example.data.utils.NetworkResult
import com.example.domain.model.CharacterFilter
import com.example.domain.model.SWCharacter
import com.example.domain.model.SWCharacterDetailsRaw
import com.example.domain.repository.CharacterRepository
import com.example.domain.utils.Result
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

private const val TAG = "CharacterRepositoryImpl"

@OptIn(ExperimentalPagingApi::class)
class CharacterRepositoryImpl @Inject constructor(
    private val characterRemoteDataSource: CharacterRemoteDataSource,
    private val characterLocalDataSource: CharacterLocalDataSource,
    private val characterDetailsLocalDataSource: CharacterDetailsLocalDataSource,
    private val starWarsDatabase: StarWarsDatabase
) : CharacterRepository {

    override fun getCharacters(
        filter: CharacterFilter
    ): Flow<PagingData<SWCharacter>> {
        return Pager(
            config = PagingConfig(
                pageSize = 10, // В SWAPI обычно 10 элементов на страницу
                enablePlaceholders = true,
                initialLoadSize = 10,
                prefetchDistance = 5
            ),
            remoteMediator = CharacterRemoteMediator(
                characterRemoteDataSource = characterRemoteDataSource,
                characterLocalDataSource = characterLocalDataSource,
                starWarsDatabase = starWarsDatabase,
                filter = filter
            ),
            pagingSourceFactory = {
                characterLocalDataSource.getCharactersPagingSource(filter)
            }
        ).flow.map { pagingData ->
            pagingData.map { it.toCharacter() }
        }
    }

    override suspend fun refreshCharacters() {
        Log.d(TAG, "Очистка кэша списка персонажей.")
        characterLocalDataSource.clearAllCharacters()
        characterLocalDataSource.clearAllRemoteKeys()
    }

    // ID теперь String, так как в SWAPI мы вырезаем его из URL
    override suspend fun getCharacterDetails(characterId: String): Result<SWCharacterDetailsRaw> {
        Log.d(TAG, "Запрос деталей персонажа с ID: $characterId")
        return try {
            val localDetails = characterDetailsLocalDataSource.getCharacterDetails(characterId)
            if (localDetails != null) {
                Log.d(TAG, "Детали персонажа найдены в кэше.")
                Result.Success(localDetails.toCharacterDetailsRaw())
            } else {
                Log.d(TAG, "Детали персонажа не найдены в кэше. Сетевой запрос для ID: $characterId")
                // Внимание: метод в DataSource тоже должен принимать String
                when (val remoteResult = characterRemoteDataSource.getCharacterById(characterId)) {
                    is NetworkResult.Success -> {
                        Log.d(TAG, "Сетевой запрос успешен. Сохраняем в кэш.")
                        val detailsEntity = remoteResult.data.toCharacterDetailsEntity()
                        characterDetailsLocalDataSource.insertCharacterDetails(detailsEntity)
                        Result.Success(detailsEntity.toCharacterDetailsRaw())
                    }

                    is NetworkResult.Error -> {
                        Log.e(TAG, "Ошибка сети: ${remoteResult.exception.localizedMessage}")
                        Result.Error(remoteResult.exception)
                    }
                }
            }
        } catch (e: Exception) {
            Log.e(TAG, "Ошибка в репозитории: ${e.localizedMessage}")
            Result.Error(e)
        }
    }
}