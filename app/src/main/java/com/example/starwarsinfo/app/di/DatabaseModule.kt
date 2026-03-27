package com.example.starwarsinfo.app.di

import android.content.Context
import androidx.room.Room
import com.example.data.local.converters.CharacterTypeConverters
import com.example.data.local.dao.* // Импортируем все новые DAO
import com.example.data.local.database.StarWarsDatabase // Новое имя класса БД
import com.squareup.moshi.Moshi
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DatabaseModule {

    @Provides
    @Singleton
    fun provideDatabase(
        @ApplicationContext context: Context,
        characterTypeConverters: CharacterTypeConverters,
    ): StarWarsDatabase {
        return Room.databaseBuilder(
            context,
            StarWarsDatabase::class.java,
            "star_wars_db" // Переименовали файл БД
        )
            .addTypeConverter(characterTypeConverters)
            .fallbackToDestructiveMigration(true)
            .build()
    }

    @Provides
    @Singleton
    fun provideCharacterTypeConverters(moshi: Moshi): CharacterTypeConverters {
        return CharacterTypeConverters(moshi)
    }

    @Provides
    @Singleton
    fun provideCharacterDao(database: StarWarsDatabase): CharacterDao {
        return database.characterDao()
    }

    @Provides
    @Singleton
    fun provideRemoteKeysDao(database: StarWarsDatabase): RemoteKeyDao {
        return database.remoteKeysDao()
    }

    @Provides
    @Singleton
    fun provideCharacterDetailsDao(database: StarWarsDatabase): CharacterDetailsDao {
        return database.characterDetailsDao()
    }

    // Заменяем LocationDao на PlanetDao
    @Provides
    @Singleton
    fun providePlanetDao(database: StarWarsDatabase): PlanetDao {
        return database.planetDao()
    }

    // Заменяем EpisodeDao на FilmDao
    @Provides
    @Singleton
    fun provideFilmDao(database: StarWarsDatabase): FilmDao {
        return database.filmDao()
    }
}