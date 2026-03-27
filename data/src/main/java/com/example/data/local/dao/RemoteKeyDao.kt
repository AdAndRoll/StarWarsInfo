package com.example.data.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.data.local.entity.RemoteKeyEntity

@Dao
interface RemoteKeyDao {

    /**
     * Сохраняет ключ пагинации.
     * Мы используем REPLACE, чтобы всегда иметь актуальное состояние
     * текущей страницы и примененных фильтров.
     */
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertOrReplace(remoteKey: RemoteKeyEntity)

    /**
     * Получает ключ пагинации.
     * В нашей реализации мы используем одну запись (id = 0) для управления
     * всем списком персонажей, так как пагинация линейная.
     */
    @Query("SELECT * FROM remote_keys WHERE id = 0")
    suspend fun getRemoteKey(): RemoteKeyEntity?

    /**
     * Полная очистка ключей.
     * Вызывается в RemoteMediator при LoadType.REFRESH.
     */
    @Query("DELETE FROM remote_keys")
    suspend fun clearAllRemoteKeys()
}