package com.example.domain.usecases

import androidx.paging.PagingData
import com.example.domain.model.CharacterFilter
import com.example.domain.model.SWCharacter
import com.example.domain.repository.CharacterRepository
import kotlinx.coroutines.flow.Flow

/**
 * Use Case для получения списка персонажей с пагинацией.
 * Координирует работу с репозиторием, предоставляя данные UI в формате PagingData.
 */
class GetCharactersUseCase(
    private val repository: CharacterRepository
) {
    /**
     * Выполняет получение списка персонажей.
     *
     * @param filter Параметры фильтрации.
     * @return [Flow] из [PagingData] с доменными моделями [SWCharacter].
     */
    fun execute(filter: CharacterFilter): Flow<PagingData<SWCharacter>> {
        return repository.getCharacters(filter)
    }
}
