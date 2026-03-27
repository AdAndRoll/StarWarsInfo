package com.example.starwarsinfo.presentation.character_detail

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.domain.usecases.GetCharacterDetailsUseCase
import com.example.domain.utils.Result
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

/**
 * ViewModel для детального экрана персонажа Star Wars.
 */
@HiltViewModel
class CharacterDetailViewModel @Inject constructor(
    private val getCharacterDetailsUseCase: GetCharacterDetailsUseCase
) : ViewModel() {

    // Внутреннее изменяемое состояние
    private val _characterDetailState =
        MutableStateFlow<CharacterDetailState>(CharacterDetailState.Loading)

    // Публичное состояние только для чтения
    val characterDetailState: StateFlow<CharacterDetailState> = _characterDetailState.asStateFlow()

    /**
     * Загружает детали персонажа по его ID.
     * * @param characterId Уникальный ID персонажа (теперь String для SWAPI).
     */
    fun loadCharacterDetails(characterId: String) {
        viewModelScope.launch {
            _characterDetailState.value = CharacterDetailState.Loading

            // Вызываем UseCase (убедись, что его метод execute тоже принимает String)
            when (val result = getCharacterDetailsUseCase.execute(characterId)) {
                is Result.Success -> {
                    _characterDetailState.value = CharacterDetailState.Success(result.data)
                }

                is Result.Error -> {
                    _characterDetailState.value = CharacterDetailState.Error(
                        result.exception.message ?: "Произошла ошибка при загрузке данных"
                    )
                }
            }
        }
    }
}