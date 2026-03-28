package com.example.data.local.database

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.example.data.local.converters.CharacterTypeConverters
import com.example.data.local.dao.CharacterDao
import com.example.data.local.dao.CharacterDetailsDao
import com.example.data.local.dao.FilmDao
import com.example.data.local.dao.PlanetDao
import com.example.data.local.dao.RemoteKeyDao
import com.example.data.local.entity.CharacterDetailsEntity
import com.example.data.local.entity.CharacterEntity
import com.example.data.local.entity.PlanetDetailEntity
import com.example.data.local.entity.SWFilmEntity
import com.example.data.local.entity.RemoteKeyEntity

/**
 * База данных Room для приложения Star Wars.
 * Интегрирует все сущности и предоставляет доступ к DAO.
 */
@Database(
    entities = [
        CharacterEntity::class,
        RemoteKeyEntity::class,
        CharacterDetailsEntity::class,
        PlanetDetailEntity::class, // Заменили LocationDetailEntity
        SWFilmEntity::class        // Заменили RMEpisodeEntity
    ],
    version = 4, // Сбрасываем версию, так как схема изменилась кардинально
    exportSchema = false
)
@TypeConverters(CharacterTypeConverters::class)
abstract class StarWarsDatabase : RoomDatabase() {

    abstract fun characterDao(): CharacterDao

    abstract fun remoteKeysDao(): RemoteKeyDao

    abstract fun characterDetailsDao(): CharacterDetailsDao

    abstract fun planetDao(): PlanetDao // Заменили locationDao

    abstract fun filmDao(): FilmDao     // Заменили episodeDao
}