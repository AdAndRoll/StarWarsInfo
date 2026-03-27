package com.example.data.utils

import retrofit2.Response

/**
 * Обертка для безопасного выполнения сетевых запросов.
 * Принимает лямбду, которая должна вернуть Retrofit Response.
 */
suspend fun <T : Any> safeApiCall(
    execute: suspend () -> Response<T>
): NetworkResult<T> {
    return try {
        val response = execute()
        val body = response.body()

        if (response.isSuccessful && body != null) {
            // Если код 200-299 и тело не пустое
            NetworkResult.Success(body)
        } else {
            // Если сервер ответил ошибкой (например, 404 или 500)
            NetworkResult.Error(Exception("Error Code: ${response.code()}, Message: ${response.message()}"))
        }
    } catch (e: Exception) {
        // Если вообще нет интернета или ошибка парсинга JSON
        NetworkResult.Error(e)
    }
}