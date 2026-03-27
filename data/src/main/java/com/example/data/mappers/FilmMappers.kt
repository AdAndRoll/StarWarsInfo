package com.example.data.mappers

import com.example.data.local.entity.SWFilmEntity
import com.example.data.remote.dto.FilmDto
import com.example.domain.model.SWFilm
import com.example.domain.model.SWCharacterSummary
import com.example.domain.model.SWFilmSummary

/**
 * Преобразует [FilmDto] (сеть) в [SWFilmEntity] (база).
 */
fun FilmDto.toFilmEntity(): SWFilmEntity {
    return SWFilmEntity(
        id = this.url.toSwapiId(),
        title = this.title,
        episodeId = this.episodeId,
        openingCrawl = this.openingCrawl,
        director = this.director,
        producer = this.producer,
        releaseDate = this.releaseDate,
        characterUrls = this.characterUrls,
        url = this.url
    )
}

/**
 * Преобразует [SWFilmEntity] (база) в [SWFilm] (домен).
 * Принимает готовый список героев, преобразованных из их URL.
 */
fun SWFilmEntity.toSWFilm(characters: List<SWCharacterSummary>): SWFilm {
    return SWFilm(
        id = this.id,
        title = this.title,
        episodeId = this.episodeId,
        openingCrawl = this.openingCrawl,
        director = this.director,
        producer = this.producer,
        releaseDate = this.releaseDate,
        characters = characters,
        url = this.url
    )
}

/**
 * Преобразует [FilmDto] (сеть) в [SWFilm] (домен).
 */
fun FilmDto.toSWFilm(characters: List<SWCharacterSummary>): SWFilm {
    return SWFilm(
        id = this.url.toSwapiId(),
        title = this.title,
        episodeId = this.episodeId,
        openingCrawl = this.openingCrawl,
        director = this.director,
        producer = this.producer,
        releaseDate = this.releaseDate,
        characters = characters,
        url = this.url
    )
}

/**
 * Преобразует [FilmDto] (сеть) в [SWFilmSummary] (краткая инфа для списков).
 * Это то, что мы будем показывать в деталях персонажа.
 */
fun FilmDto.toFilmSummary(): SWFilmSummary {
    return SWFilmSummary(
        id = this.url.toSwapiId(),
        title = this.title,
        url = this.url
    )
}

/**
 * Аналогично для [SWFilmEntity] (база) -> [SWFilmSummary] (домен).
 */
fun SWFilmEntity.toFilmSummary(): SWFilmSummary {
    return SWFilmSummary(
        id = this.id,
        title = this.title,
        url = this.url
    )
}