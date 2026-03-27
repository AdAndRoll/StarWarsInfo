package com.example.data.remote.api

import com.example.data.remote.dto.CharacterDto
import com.example.data.remote.dto.SwapiResponse
import retrofit2.Response // Импортируем Response
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface SwapiApi {

    @GET("people/")
    suspend fun getCharacters(
        @Query("page") page: Int,
        @Query("search") name: String? = null
    ): Response<SwapiResponse<CharacterDto>> // Обернули в Response

    @GET("people/{id}/")
    suspend fun getCharacterById(@Path("id") id: String): Response<CharacterDto> // Обернули в Response
}