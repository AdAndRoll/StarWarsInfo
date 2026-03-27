package com.example.data.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.data.local.entity.CharacterDetailsEntity

/**
 * DAO для работы с ПОЛНОЙ информацией о персонажах Star Wars.
 */
@Dao
interface CharacterDetailsDao {

    /**
     * Возвращает детали персонажа по его строковому ID.
     */
    @Query("SELECT * FROM character_details WHERE id = :characterId")
    suspend fun getCharacterDetails(characterId: String): CharacterDetailsEntity?

    /**
     * Вставляет или обновляет детали персонажа.
     */
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertCharacterDetails(characterDetails: CharacterDetailsEntity)

    /**
     * Удаляет детали персонажа.
     */
    @Query("DELETE FROM character_details WHERE id = :characterId")
    suspend fun deleteCharacterDetails(characterId: String): Int

    /**
     * Дублирующий метод (можно оставить или удалить, если используется getCharacterDetails).
     */
    @Query("SELECT * FROM character_details WHERE id = :characterId")
    suspend fun getCharacterDetailsById(characterId: String): CharacterDetailsEntity?
}