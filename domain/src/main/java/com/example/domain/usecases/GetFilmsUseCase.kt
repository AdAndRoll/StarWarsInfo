package com.example.domain.usecases

import com.example.domain.model.SWFilmSummary
import com.example.domain.repository.FilmRepository

import com.example.domain.utils.Result

/**
 * Use Case для получения краткой информации о фильмах по списку их URL-адресов.
 * (Аналог GetCharacterEpisodesUseCase)
 */
class GetFilmsUseCase(private val repository: FilmRepository) {

    /**
     * Извлекает ID из URL и запрашивает данные у репозитория.
     * @param filmUrls Список URL вида "https://swapi.dev/api/films/1/"
     */
    suspend fun execute(filmUrls: List<String>): Result<List<SWFilmSummary>> {
        // Извлекаем ID: "https://swapi.dev/api/films/1/" -> "1"
        val filmIds = filmUrls.map { it.trimEnd('/').substringAfterLast("/") }
            .filter { it.isNotEmpty() }

        return if (filmIds.isNotEmpty()) {
            repository.getFilmsSummariesByIds(filmIds)
        } else {
            Result.Success(emptyList())
        }
    }
}