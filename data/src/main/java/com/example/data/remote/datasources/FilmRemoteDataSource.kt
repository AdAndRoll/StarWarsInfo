package com.example.data.remote.datasources

import com.example.data.remote.api.FilmApiService
import com.example.data.remote.dto.FilmDto
import com.example.data.utils.NetworkResult
import com.example.data.utils.safeApiCall
import kotlinx.coroutines.async
import kotlinx.coroutines.awaitAll
import kotlinx.coroutines.coroutineScope
import javax.inject.Inject

/**
 * Источник данных для удаленного API фильмов (вместо Эпизодов).
 * Инкапсулирует логику сетевых запросов к SWAPI.
 */
class FilmRemoteDataSource @Inject constructor(
    private val api: FilmApiService
) {
    suspend fun getFilm(filmId: String): NetworkResult<FilmDto> {
        return safeApiCall {
            api.getFilm(filmId)
        }
    }

    /**
     * Получает информацию о нескольких фильмах параллельно.
     */
    suspend fun getFilmsSummariesByIds(ids: List<String>): NetworkResult<List<FilmDto>> {
        return try {
            coroutineScope {
                val results = ids.map { id ->
                    async {
                        val response = api.getFilm(id)
                        if (response.isSuccessful) response.body() else null
                    }
                }.awaitAll().filterNotNull() // Игнорируем те, что не загрузились

                NetworkResult.Success(results)
            }
        } catch (e: Exception) {
            NetworkResult.Error(e)
        }
    }
}