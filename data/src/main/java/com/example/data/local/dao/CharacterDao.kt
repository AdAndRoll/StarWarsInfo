package com.example.data.local.dao

import androidx.paging.PagingSource
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.data.local.entity.CharacterEntity

/**
 * DAO для работы с персонажами Star Wars.
 * Упрощено под возможности SWAPI (фильтрация только по имени).
 */
@Dao
interface CharacterDao {

    /**
     * Получает PagingSource для персонажей.
     * Оставляем только фильтр по имени, так как остальные данные (status, type) в SWAPI отсутствуют.
     */
    @Query(
        """
        SELECT * FROM characters 
        WHERE (:name IS NULL OR LOWER(name) LIKE '%' || LOWER(:name) || '%')
        """
    )
    fun getCharactersPagingSource(name: String?): PagingSource<Int, CharacterEntity>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertCharacters(characters: List<CharacterEntity>)

    /**
     * Метод для получения всех персонажей без фильтрации.
     */
    @Query("SELECT * FROM characters")
    fun getAllCharacters(): PagingSource<Int, CharacterEntity>

    /**
     * Очистка таблицы при REFRESH в RemoteMediator.
     */
    @Query("DELETE FROM characters")
    suspend fun clearAllCharacters()

    /**
     * Подсчет количества записей для проверки состояния кэша.
     */
    @Query("SELECT COUNT(*) FROM characters")
    suspend fun getAllCharactersCount(): Int
}