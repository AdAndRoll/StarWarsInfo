package com.example.starwarsinfo.app.di

import com.example.data.local.dao.*
import com.example.data.local.database.StarWarsDatabase
import com.example.data.local.datasources.*
import com.example.data.remote.api.*
import com.example.data.remote.datasources.*
import com.example.data.repository.*
import com.example.domain.repository.*
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DataModule {

    // --- Data Sources ---

    @Provides
    @Singleton
    fun provideCharacterRemoteDataSource(api: SwapiApi): CharacterRemoteDataSource =
        CharacterRemoteDataSource(api)

    @Provides
    @Singleton
    fun provideCharacterLocalDataSource(dao: CharacterDao, keysDao: RemoteKeyDao): CharacterLocalDataSource =
        CharacterLocalDataSource(dao, keysDao)

    @Provides
    @Singleton
    fun provideFilmRemoteDataSource(api: FilmApiService): FilmRemoteDataSource =
        FilmRemoteDataSource(api)

    @Provides
    @Singleton
    fun provideFilmLocalDataSource(dao: FilmDao): FilmLocalDataSource =
        FilmLocalDataSource(dao)

    @Provides
    @Singleton
    fun providePlanetRemoteDataSource(api: PlanetApiService): PlanetRemoteDataSource =
        PlanetRemoteDataSource(api)

    @Provides
    @Singleton
    fun providePlanetLocalDataSource(dao: PlanetDao): PlanetLocalDataSource =
        PlanetLocalDataSource(dao)

    // --- Repositories Implementation ---

    @Provides
    @Singleton
    fun provideCharacterRepository(
        characterRemoteDataSource: CharacterRemoteDataSource,
        characterLocalDataSource: CharacterLocalDataSource,
        characterDetailsLocalDataSource: CharacterDetailsLocalDataSource,
        starWarsDatabase: StarWarsDatabase
    ): CharacterRepository {
        return CharacterRepositoryImpl(
            characterRemoteDataSource,
            characterLocalDataSource,
            characterDetailsLocalDataSource,
            starWarsDatabase
        )
    }

    @Provides
    @Singleton
    fun provideFilmRepository(
        remoteDataSource: FilmRemoteDataSource,
        localDataSource: FilmLocalDataSource,
        characterRemoteDataSource: CharacterRemoteDataSource, // Добавили
        characterDetailsDao: CharacterDetailsDao              // Добавили
    ): FilmRepository {
        return FilmRepositoryImpl(
            remoteDataSource,
            localDataSource,
            characterRemoteDataSource,
            characterDetailsDao
        )
    }

    @Provides
    @Singleton
    fun providePlanetRepository(
        remoteDataSource: PlanetRemoteDataSource,
        localDataSource: PlanetLocalDataSource,
        characterRemoteDataSource: CharacterRemoteDataSource, // Добавили
        characterDetailsDao: CharacterDetailsDao              // Добавили
    ): PlanetRepository {
        return PlanetRepositoryImpl(
            remoteDataSource,
            localDataSource,
            characterRemoteDataSource,
            characterDetailsDao
        )
    }
}