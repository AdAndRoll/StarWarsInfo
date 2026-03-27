package com.example.data.local.datasources

import com.example.data.local.dao.CharacterDetailsDao
import com.example.data.local.entity.CharacterDetailsEntity
import javax.inject.Inject

/**
 * Локальный источник данных для работы с детальной информацией о персонажах Star Wars.
 */
class CharacterDetailsLocalDataSource @Inject constructor(
    private val characterDetailsDao: CharacterDetailsDao
) {
    /**
     * Получает детали персонажа из БД по его строковому ID.
     *
     * @param characterId ID персонажа (например, "1").
     * @return [CharacterDetailsEntity] или null, если в кэше ничего нет.
     */
    suspend fun getCharacterDetails(characterId: String): CharacterDetailsEntity? {
        return characterDetailsDao.getCharacterDetails(characterId)
    }

    /**
     * Сохраняет или обновляет детали персонажа в кэше.
     */
    suspend fun insertCharacterDetails(characterDetails: CharacterDetailsEntity) {
        characterDetailsDao.insertCharacterDetails(characterDetails)
    }

    /**
     * Удаляет закэшированные детали персонажа.
     */
    suspend fun deleteCharacterDetails(characterId: String) {
        characterDetailsDao.deleteCharacterDetails(characterId)
    }
}