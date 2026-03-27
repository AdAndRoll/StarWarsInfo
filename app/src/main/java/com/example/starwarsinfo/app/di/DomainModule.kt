package com.example.starwarsinfo.app.di

import com.example.domain.repository.CharacterRepository
import com.example.domain.repository.FilmRepository
import com.example.domain.repository.PlanetRepository
import com.example.domain.usecases.*
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DomainModule {

    @Provides
    @Singleton
    fun provideGetCharactersUseCase(
        repository: CharacterRepository
    ): GetCharactersUseCase = GetCharactersUseCase(repository)

    @Provides
    @Singleton
    fun provideGetFilmsUseCase(
        repository: FilmRepository
    ): GetFilmsUseCase = GetFilmsUseCase(repository)

    @Provides
    @Singleton
    fun provideGetCharacterDetailsUseCase(
        characterRepository: CharacterRepository,
        planetRepository: PlanetRepository,
        getFilmsUseCase: GetFilmsUseCase
    ): GetCharacterDetailsUseCase {
        return GetCharacterDetailsUseCase(
            characterRepository,
            planetRepository,
            getFilmsUseCase
        )
    }

    @Provides
    @Singleton
    fun provideGetPlanetDetailsUseCase(
        repository: PlanetRepository
    ): GetPlanetDetailsUseCase = GetPlanetDetailsUseCase(repository)

    @Provides
    @Singleton
    fun provideGetSingleFilmUseCase(
        repository: FilmRepository
    ): GetSingleFilmUseCase = GetSingleFilmUseCase(repository)
}