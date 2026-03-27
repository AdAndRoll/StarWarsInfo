package com.example.data.remote.api

import com.example.data.remote.dto.FilmDto
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Path

/**
 * Интерфейс Retrofit для API "Фильмы" (вместо Эпизодов).
 */
interface FilmApiService {

    @GET("films/{id}/")
    suspend fun getFilm(@Path("id") id: String): Response<FilmDto>
}