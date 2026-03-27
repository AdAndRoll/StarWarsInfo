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
        val remoteKey = characterLocalDataSource.getRemoteKey()
        val now = System.currentTimeMillis()

        // Кэш устарел, если прошло много времени или сменился поисковый запрос
        val isFilterChanged = remoteKey?.filterName != (filter.name ?: "")
        val isCacheOutdated = (now - (remoteKey?.createdAt ?: 0L)) >= CACHE_TIMEOUT

        return if (isFilterChanged || isCacheOutdated) {
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
                LoadType.REFRESH -> 1
                LoadType.PREPEND -> return MediatorResult.Success(endOfPaginationReached = true)
                LoadType.APPEND -> {
                    val remoteKey = characterLocalDataSource.getRemoteKey()
                    // Если следующей страницы нет, значит мы приехали
                    remoteKey?.nextKey ?: return MediatorResult.Success(endOfPaginationReached = true)
                }
            }

            Log.d(TAG, "SWAPI Request -> Page: $page, Search: ${filter.name}")

            val apiResult = characterRemoteDataSource.getCharacters(
                page = page,
                name = filter.name
            )

            when (apiResult) {
                is NetworkResult.Success -> {
                    val results = apiResult.data.results

                    // Извлекаем номер следующей страницы из "https://swapi.dev/api/people/?search=luke&page=2"
                    val nextKey = apiResult.data.next?.let { url ->
                        val uri = android.net.Uri.parse(url)
                        uri.getQueryParameter("page")?.toIntOrNull()
                    }

                    val endOfPaginationReached = results.isEmpty() || apiResult.data.next == null

                    starWarsDatabase.withTransaction {
                        if (loadType == LoadType.REFRESH) {
                            characterLocalDataSource.clearAllCharacters()
                            characterLocalDataSource.clearAllRemoteKeys()
                        }

                        val newRemoteKey = RemoteKeyEntity(
                            id = 0, // В нашей реализации один ключ на весь список
                            prevKey = if (page == 1) null else page - 1,
                            nextKey = nextKey,
                            createdAt = System.currentTimeMillis(),
                            filterName = filter.name ?: "",
                        )

                        characterLocalDataSource.insertRemoteKey(newRemoteKey)
                        characterLocalDataSource.insertCharacters(results.map { it.toCharacterEntity() })
                    }
                    MediatorResult.Success(endOfPaginationReached = endOfPaginationReached)
                }
                is NetworkResult.Error -> MediatorResult.Error(apiResult.exception)
            }
        } catch (e: Exception) {
            MediatorResult.Error(e)
        }
    }
}