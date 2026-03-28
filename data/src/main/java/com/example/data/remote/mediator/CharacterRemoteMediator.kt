package com.example.data.remote.mediator

import android.util.Log
import androidx.paging.ExperimentalPagingApi
import androidx.paging.LoadType
import androidx.paging.PagingState
import androidx.paging.RemoteMediator
import androidx.room.withTransaction
import com.example.data.local.database.StarWarsDatabase
import com.example.data.local.datasources.CharacterLocalDataSource
import com.example.data.local.entity.CharacterEntity
import com.example.data.local.entity.RemoteKeyEntity
import com.example.data.mappers.toCharacterEntity
import com.example.data.mappers.toSwapiId // Импортируем твой маппер
import com.example.data.remote.datasources.CharacterRemoteDataSource
import com.example.data.utils.NetworkResult
import com.example.domain.model.CharacterFilter
import java.util.concurrent.TimeUnit

@OptIn(ExperimentalPagingApi::class)
class CharacterRemoteMediator(
    private val characterRemoteDataSource: CharacterRemoteDataSource,
    private val characterLocalDataSource: CharacterLocalDataSource,
    private val starWarsDatabase: StarWarsDatabase,
    private val filter: CharacterFilter
) : RemoteMediator<Int, CharacterEntity>() {

    private val CACHE_TIMEOUT = TimeUnit.MINUTES.toMillis(30)
    private val TAG = "CharacterRemoteMediator"

    override suspend fun initialize(): InitializeAction {
        val randomKey = characterLocalDataSource.getRemoteKeyByCharacterId("1")
        val now = System.currentTimeMillis()
        val isCacheOutdated = (now - (randomKey?.createdAt ?: 0L)) >= CACHE_TIMEOUT
        val dbCount = characterLocalDataSource.getAllCharactersCount()

        return if (dbCount == 0 || isCacheOutdated) {
            InitializeAction.LAUNCH_INITIAL_REFRESH
        } else {
            InitializeAction.SKIP_INITIAL_REFRESH
        }
    }

    override suspend fun load(
        loadType: LoadType,
        state: PagingState<Int, CharacterEntity>
    ): MediatorResult {
        return try {
            val page = when (loadType) {
                LoadType.REFRESH -> {
                    val remoteKeys = getRemoteKeyClosestToCurrentPosition(state)
                    remoteKeys?.nextKey?.minus(1) ?: 1
                }
                LoadType.PREPEND -> {
                    return MediatorResult.Success(endOfPaginationReached = true)
                }
                LoadType.APPEND -> {
                    val remoteKeys = getRemoteKeyForLastItem(state)
                    val nextKey = remoteKeys?.nextKey

                    // Если ключей нет в базе, но это APPEND — значит мы еще в процессе загрузки первой страницы
                    if (nextKey == null) {
                        return MediatorResult.Success(endOfPaginationReached = remoteKeys != null)
                    }
                    nextKey
                }
            }

            Log.d(TAG, "✈️ Request: Page $page, Type $loadType, Filter: '${filter.name ?: ""}'")

            val apiResult = characterRemoteDataSource.getCharacters(
                page = page,
                name = filter.name
            )

            when (apiResult) {
                is NetworkResult.Success -> {
                    val apiResponse = apiResult.data
                    val results = apiResponse.results
                    val endOfPaginationReached = results.isEmpty() || apiResponse.next == null

                    starWarsDatabase.withTransaction {
                        if (loadType == LoadType.REFRESH) {
                            characterLocalDataSource.clearAllCharacters()
                            characterLocalDataSource.clearAllRemoteKeys()
                        }

                        val prevKey = if (page == 1) null else page - 1
                        val nextKey = if (endOfPaginationReached) null else page + 1

                        val keys = results.map { dto ->
                            RemoteKeyEntity(
                                characterId = dto.url.toSwapiId(), // ИСПОЛЬЗУЕМ МАППЕР
                                prevKey = prevKey,
                                nextKey = nextKey,
                                createdAt = System.currentTimeMillis()
                            )
                        }

                        characterLocalDataSource.insertRemoteKeys(keys)
                        characterLocalDataSource.insertCharacters(results.map { it.toCharacterEntity() })
                    }

                    Log.d(TAG, "✅ Success: Page $page, Added ${results.size}. End=$endOfPaginationReached")
                    MediatorResult.Success(endOfPaginationReached = endOfPaginationReached)
                }

                is NetworkResult.Error -> {
                    Log.e(TAG, "❌ Network Error: ${apiResult.exception.message}")
                    MediatorResult.Error(apiResult.exception)
                }
            }
        } catch (e: Exception) {
            Log.e(TAG, "❌ Unexpected Error", e)
            MediatorResult.Error(e)
        }
    }

    private suspend fun getRemoteKeyForLastItem(state: PagingState<Int, CharacterEntity>): RemoteKeyEntity? {
        val lastItem = state.pages.lastOrNull { it.data.isNotEmpty() }?.data?.lastOrNull()
        return lastItem?.let { character ->
            val key = characterLocalDataSource.getRemoteKeyByCharacterId(character.id)
            Log.d(TAG, "🔍 APPEND: Last ID in UI: ${character.id}, Key found: ${key?.nextKey}")
            key
        }
    }

    private suspend fun getRemoteKeyClosestToCurrentPosition(state: PagingState<Int, CharacterEntity>): RemoteKeyEntity? {
        return state.anchorPosition?.let { position ->
            state.closestItemToPosition(position)?.id?.let { id ->
                val key = characterLocalDataSource.getRemoteKeyByCharacterId(id)
                Log.d(TAG, "🔍 REFRESH: Anchor ID: $id, Key found: ${key?.nextKey}")
                key
            }
        }
    }
}