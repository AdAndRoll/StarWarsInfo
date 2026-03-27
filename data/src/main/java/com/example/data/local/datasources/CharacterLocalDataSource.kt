package com.example.data.local.datasources

import androidx.paging.PagingSource
import com.example.data.local.dao.CharacterDao
import com.example.data.local.dao.RemoteKeyDao
import com.example.data.local.entity.CharacterEntity
import com.example.data.local.entity.RemoteKeyEntity
import com.example.domain.model.CharacterFilter
import javax.inject.Inject

/**
 * Локальный источник данных для управления списком персонажей и ключами пагинации.
 */
class CharacterLocalDataSource @Inject constructor(
    private val characterDao: CharacterDao,
    private val remoteKeysDao: RemoteKeyDao
) {
    /**
     * Вставляет список персонажей в БД.
     */
    suspend fun insertCharacters(characters: List<CharacterEntity>) {
        characterDao.insertCharacters(characters)
    }

    /**
     * Получает PagingSource для отображения списка.
     * В SWAPI фильтруем только по имени.
     */
    fun getCharactersPagingSource(filter: CharacterFilter): PagingSource<Int, CharacterEntity> {
        return characterDao.getCharactersPagingSource(
            name = filter.name?.takeIf { it.isNotBlank() }
        )
    }

    /**
     * Очищает таблицу персонажей (например, при REFRESH).
     */
    suspend fun clearAllCharacters() {
        characterDao.clearAllCharacters()
    }

    /**
     * Сохраняет ключ пагинации.
     */
    suspend fun insertRemoteKey(remoteKey: RemoteKeyEntity) {
        remoteKeysDao.insertOrReplace(remoteKey)
    }

    /**
     * Получает текущий ключ пагинации для определения следующей страницы.
     */
    suspend fun getRemoteKey(): RemoteKeyEntity? {
        return remoteKeysDao.getRemoteKey()
    }

    /**
     * Очищает ключи пагинации.
     */
    suspend fun clearAllRemoteKeys() {
        remoteKeysDao.clearAllRemoteKeys()
    }

    /**
     * Проверяет количество закэшированных записей.
     */
    suspend fun getAllCharactersCount(): Int {
        return characterDao.getAllCharactersCount()
    }
}