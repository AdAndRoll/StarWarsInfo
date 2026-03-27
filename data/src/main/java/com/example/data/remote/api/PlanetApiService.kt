package com.example.data.remote.api

import com.example.data.remote.dto.PlanetDto
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Path

/**
 * Интерфейс для работы с планетами (вместо локаций).
 */
interface PlanetApiService {
    @GET("planets/{id}/")
    suspend fun getPlanetDetails(@Path("id") id: String): Response<PlanetDto>
}