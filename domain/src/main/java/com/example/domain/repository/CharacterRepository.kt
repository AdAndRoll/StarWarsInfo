package com.example.domain.repository

import androidx.paging.PagingData
import com.example.domain.model.CharacterFilter
import com.example.domain.model.SWCharacter
import com.example.domain.model.SWCharacterDetailsRaw
import com.example.domain.utils.Result
import kotlinx.coroutines.flow.Flow

/**
 * Репозиторий для работы с данными о персонажах
 */

interface CharacterRepository {

    /**
     * Возвращает поток с текущим списком персонажей.
     * Поддерживает пагинацию и фильтрацию через CharacterFilter.
     */
    fun getCharacters(
        filter: CharacterFilter
    ): Flow<PagingData<SWCharacter>>

    /**
     * Обновить список персонажей (например, при Pull-to-Refresh).
     */
    suspend fun refreshCharacters()

    /**
     * Получает детальную информацию о персонаже по его ID.
     * Возвращает "сырые" данные, включая список URL-адресов эпизодов.
     *
     * @param characterId ID персонажа.
     * @return [Result] с [RMCharacterDetailsRaw] в случае успеха, или [Throwable] в случае ошибки.
     */
    suspend fun getCharacterDetails(characterId: String): Result<SWCharacterDetailsRaw>
}
