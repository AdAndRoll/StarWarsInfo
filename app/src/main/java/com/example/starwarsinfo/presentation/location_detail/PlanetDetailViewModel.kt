package com.example.starwarsinfo.presentation.planet_detail

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.domain.usecases.GetPlanetDetailsUseCase
import com.example.domain.utils.Result
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import java.net.URLDecoder // Добавляем импорт
import java.nio.charset.StandardCharsets // Добавляем импорт
import javax.inject.Inject

/**
 * ViewModel для экрана с детальной информацией о планете.
 * Автоматически загружает данные при инициализации, используя декодированный URL.
 */
@HiltViewModel
class PlanetDetailViewModel @Inject constructor(
    private val getSinglePlanetUseCase: GetPlanetDetailsUseCase,
    savedStateHandle: SavedStateHandle
) : ViewModel() {

    private val _planetDetailState = MutableStateFlow<PlanetDetailState>(PlanetDetailState.Loading)
    val planetDetailState: StateFlow<PlanetDetailState> = _planetDetailState.asStateFlow()

    init {
        // 1. Получаем закодированный planetId (URL) из навигации
        savedStateHandle.get<String>("planetId")?.let { encodedPlanetId ->
            try {
                // 2. Декодируем: %3A -> : , %2F -> /
                val decodedUrl = URLDecoder.decode(encodedPlanetId, StandardCharsets.UTF_8.toString())
                loadPlanetDetails(decodedUrl)
            } catch (e: Exception) {
                _planetDetailState.value = PlanetDetailState.Error("Ошибка обработки координат планеты")
            }
        }
    }

    /**
     * Загружает детали планеты по её строковому URL.
     */
    private fun loadPlanetDetails(planetId: String) {
        viewModelScope.launch {
            _planetDetailState.value = PlanetDetailState.Loading

            getSinglePlanetUseCase.execute(planetId).collect { result ->
                _planetDetailState.value = when (result) {
                    is Result.Success -> {
                        PlanetDetailState.Success(result.data)
                    }
                    is Result.Error -> {
                        PlanetDetailState.Error(
                            result.exception.message ?: "Ошибка архивов: планета не найдена"
                        )
                    }
                }
            }
        }
    }
}