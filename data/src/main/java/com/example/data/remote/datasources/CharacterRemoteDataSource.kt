package com.example.data.remote.datasources

import android.util.Log
import com.example.data.remote.api.SwapiApi
import com.example.data.remote.dto.CharacterDto
import com.example.data.remote.dto.SwapiResponse
import com.example.data.utils.NetworkResult
import kotlinx.coroutines.async
import kotlinx.coroutines.awaitAll
import kotlinx.coroutines.coroutineScope
import retrofit2.HttpException
import java.io.IOException
import javax.inject.Inject

class CharacterRemoteDataSource @Inject constructor(
    private val api: SwapiApi
) {
    private val TAG = "CharacterRemoteDataSource"

    /**
     * Получает список персонажей с распаковкой Response.
     */
    suspend fun getCharacters(
        page: Int,
        name: String? = null
    ): NetworkResult<SwapiResponse<CharacterDto>> {
        return try {
            Log.d(TAG, "Making API call for page $page with search: $name")

            val response = api.getCharacters(page = page, name = name)

            if (response.isSuccessful) {
                val body = response.body()
                if (body != null) {
                    NetworkResult.Success(body)
                } else {
                    NetworkResult.Error(Exception("Empty response body"))
                }
            } else {
                NetworkResult.Error(HttpException(response))
            }
        } catch (e: Exception) {
            handleException(e)
        }
    }

    /**
     * Получает одного персонажа по ID.
     */
    suspend fun getCharacterById(characterId: String): NetworkResult<CharacterDto> {
        return try {
            val response = api.getCharacterById(characterId)
            if (response.isSuccessful) {
                val body = response.body()
                if (body != null) NetworkResult.Success(body)
                else NetworkResult.Error(Exception("Body is null"))
            } else {
                NetworkResult.Error(HttpException(response))
            }
        } catch (e: Exception) {
            handleException(e)
        }
    }

    /**
     * Групповой запрос: параллельно запрашивает каждого персонажа и фильтрует только успешные.
     */
    suspend fun getCharactersByIds(characterIds: List<String>): NetworkResult<List<CharacterDto>> = coroutineScope {
        try {
            Log.d(TAG, "Fetching multiple characters: $characterIds")

            val deferredResults = characterIds.map { id ->
                async { api.getCharacterById(id) }
            }

            val responses = deferredResults.awaitAll()

            // Собираем только те, что прошли успешно и содержат данные
            val characters = responses.mapNotNull { response ->
                if (response.isSuccessful) response.body() else null
            }

            NetworkResult.Success(characters)
        } catch (e: Exception) {
            Log.e(TAG, "Error fetching multiple characters: ${e.localizedMessage}")
            NetworkResult.Error(e)
        }
    }

    /**
     * Вспомогательный метод для обработки общих ошибок (Network/HTTP).
     */
    private fun <T : Any> handleException(e: Exception): NetworkResult<T> {
        return when (e) {
            is IOException -> {
                Log.e(TAG, "Network error: ${e.localizedMessage}")
                NetworkResult.Error(e)
            }
            is HttpException -> {
                Log.e(TAG, "HTTP error: ${e.code()}")
                NetworkResult.Error(e)
            }
            else -> {
                Log.e(TAG, "Unexpected error: ${e.localizedMessage}")
                NetworkResult.Error(e)
            }
        }
    }
}