package com.example.starwarsinfo.presentation.film_detail

import com.example.domain.model.SWFilm

sealed class FilmDetailState {
    object Loading : FilmDetailState()
    data class Success(val film: SWFilm) : FilmDetailState()
    data class Error(val message: String) : FilmDetailState()
}