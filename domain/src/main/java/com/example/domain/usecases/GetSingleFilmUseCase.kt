package com.example.domain.usecases

import com.example.domain.model.SWFilm
import com.example.domain.repository.FilmRepository
import com.example.domain.utils.Result
import kotlinx.coroutines.flow.Flow

/**
 * Use Case для получения детальной информации об одном фильме.
 */
class GetSingleFilmUseCase(private val repository: FilmRepository) {

    fun execute(filmId: String): Flow<Result<SWFilm>> {
        return repository.getFilmDetails(filmId)
    }
}