package com.example.starwarsinfo.presentation.film_detail

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.domain.usecases.GetSingleFilmUseCase
import com.example.domain.utils.Result
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import java.net.URLDecoder // Импортируем декодер
import java.nio.charset.StandardCharsets // Импортируем стандарт кодировки
import javax.inject.Inject

@HiltViewModel
class FilmDetailViewModel @Inject constructor(
    private val getSingleFilmUseCase: GetSingleFilmUseCase,
    savedStateHandle: SavedStateHandle
) : ViewModel() {

    private val _filmDetailState = MutableStateFlow<FilmDetailState>(FilmDetailState.Loading)
    val filmDetailState: StateFlow<FilmDetailState> = _filmDetailState.asStateFlow()

    init {
        // 1. Достаем закодированный URL из аргументов навигации
        savedStateHandle.get<String>("filmId")?.let { encodedFilmId ->
            try {
                // 2. Декодируем обратно: %2F -> /
                val decodedUrl = URLDecoder.decode(encodedFilmId, StandardCharsets.UTF_8.toString())
                loadFilmDetails(decodedUrl)
            } catch (e: Exception) {
                _filmDetailState.value = FilmDetailState.Error("Ошибка обработки ссылки на фильм")
            }
        }
    }

    private fun loadFilmDetails(filmId: String) {
        viewModelScope.launch {
            _filmDetailState.value = FilmDetailState.Loading

            getSingleFilmUseCase.execute(filmId).collect { result ->
                when (result) {
                    is Result.Success -> {
                        _filmDetailState.value = FilmDetailState.Success(result.data)
                    }
                    is Result.Error -> {
                        _filmDetailState.value = FilmDetailState.Error(
                            result.exception.message ?: "Ошибка загрузки данных о фильме"
                        )
                    }
                }
            }
        }
    }
}