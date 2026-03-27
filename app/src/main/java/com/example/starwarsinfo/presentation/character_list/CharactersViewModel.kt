package com.example.starwarsinfo.presentation.character_list

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.example.domain.model.CharacterFilter
import com.example.domain.model.SWCharacter // Наша новая модель Star Wars
import com.example.domain.repository.CharacterRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.flatMapLatest
import javax.inject.Inject

@HiltViewModel
class CharactersViewModel @Inject constructor(
    private val characterRepository: CharacterRepository
) : ViewModel() {

    // Состояние фильтра (в нашем случае — только имя для поиска)
    private val _characterFilter = MutableStateFlow(CharacterFilter())
    val characterFilter: StateFlow<CharacterFilter> = _characterFilter.asStateFlow()

    /**
     * Поток данных пагинации.
     * flatMapLatest перезапускает запрос каждый раз, когда меняется фильтр.
     */
    @OptIn(ExperimentalCoroutinesApi::class)
    val characters: Flow<PagingData<SWCharacter>> = _characterFilter
        .flatMapLatest { filter ->
            characterRepository.getCharacters(filter)
        }
        .cachedIn(viewModelScope)

    /**
     * Вызывается из UI (SearchScreen), когда пользователь вводит имя и жмет "Найти".
     */
    fun onFilterApplied(newFilter: CharacterFilter) {
        _characterFilter.value = newFilter
    }
}