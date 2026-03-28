package com.example.data.local.dao

import androidx.paging.PagingSource
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.data.local.entity.CharacterEntity

/**
 * DAO для работы с персонажами Star Wars.
 * Оптимизировано для работы с RemoteMediator.
 */
@Dao
interface CharacterDao {

    /**
     * Основной метод для Paging 3.
     * * ВАЖНО: Мы НЕ фильтруем здесь по имени через WHERE, так как RemoteMediator
     * сам заботится о том, чтобы в базе находились только нужные персонажи
     * (после очистки при REFRESH). Это предотвращает мерцание UI.
     * Сортировка по первичному ключу (или полю 'created') гарантирует стабильный порядок.
     */
    @Query("SELECT * FROM characters ORDER BY CAST(id AS INTEGER) ASC")
    fun getCharactersPagingSource(): PagingSource<Int, CharacterEntity>

    /**
     * Вставка персонажей.
     * REPLACE критически важен, чтобы RemoteMediator мог обновлять данные
     * без удаления всей таблицы, если ID совпадает.
     */
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertCharacters(characters: List<CharacterEntity>)

    /**
     * Очистка таблицы.
     * Вызывается в RemoteMediator ТОЛЬКО при LoadType.REFRESH и только если
     * данные реально устарели или изменился поисковый запрос.
     */
    @Query("DELETE FROM characters")
    suspend fun clearAllCharacters()

    /**
     * Проверка количества записей.
     */
    @Query("SELECT COUNT(*) FROM characters")
    suspend fun getAllCharactersCount(): Int

    /**
     * Дополнительный метод: получение конкретного персонажа (например, для Detail Screen).
     */
    @Query("SELECT * FROM characters WHERE id = :characterId")
    suspend fun getCharacterById(characterId: String): CharacterEntity?
}