package com.example.domain.repository

import com.example.domain.model.SWFilm
import com.example.domain.model.SWFilmSummary
import com.example.domain.utils.Result
import kotlinx.coroutines.flow.Flow

interface FilmRepository {
    /**
     * Получает сокращенную информацию о нескольких фильмах по списку их ID.
     * Используется для списка на экране деталей персонажа.
     */
    suspend fun getFilmsSummariesByIds(ids: List<String>): Result<List<SWFilmSummary>>

    /**
     * Получает полную информацию о фильме (если захочешь сделать отдельный экран).
     */
    fun getFilmDetails(filmId: String): Flow<Result<SWFilm>>
}