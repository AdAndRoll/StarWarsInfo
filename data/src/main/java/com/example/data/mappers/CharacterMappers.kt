package com.example.data.mappers

import com.example.data.local.entity.CharacterDetailsEntity
import com.example.data.local.entity.CharacterEntity
import com.example.data.remote.dto.CharacterDto
import com.example.domain.model.SWCharacter
import com.example.domain.model.SWCharacterDetailsRaw

/**
 * Утилита для извлечения ID из URL SWAPI.
 * Например: "https://swapi.dev/api/people/1/" -> "1"
 */
fun String.toSwapiId(): String {
    return this.trimEnd('/').substringAfterLast("/")
}

/**
 * DTO -> CharacterEntity (Легкая модель для списка/Paging)
 */
fun CharacterDto.toCharacterEntity(): CharacterEntity {
    return CharacterEntity(
        id = this.url.toSwapiId(),
        name = this.name,
        // Берем ID первой расы или "1" (Human)
        species = this.speciesUrls.firstOrNull()?.toSwapiId() ?: "1",
        gender = this.gender,
        mass = this.mass, // Добавили для свернутого вида
        birthYear = this.birthYear,
        homeworldUrl = this.homeworldUrl,
        height = this.height
    )
}

/**
 * CharacterEntity -> SWCharacter (Доменная модель для списка)
 */
fun CharacterEntity.toCharacter(): SWCharacter {
    return SWCharacter(
        id = this.id,
        name = this.name,
        species = this.species,
        gender = this.gender,
        mass = this.mass,
        height = this.height
    )
}

/**
 * DTO -> SWCharacter (Прямой маппинг в домен, минуя БД)
 */
fun CharacterDto.toCharacter(): SWCharacter {
    return SWCharacter(
        id = this.url.toSwapiId(),
        name = this.name,
        species = this.speciesUrls.firstOrNull()?.toSwapiId() ?: "1",
        gender = this.gender,
        mass = this.mass,
        height = this.height
    )
}

/**
 * DTO -> CharacterDetailsEntity (Полный кэш для БД)
 */
fun CharacterDto.toCharacterDetailsEntity(): CharacterDetailsEntity {
    return CharacterDetailsEntity(
        id = this.url.toSwapiId(),
        name = this.name,
        height = this.height,
        mass = this.mass,
        hairColor = this.hairColor,
        skinColor = this.skinColor,
        eyeColor = this.eyeColor,
        birthYear = this.birthYear,
        gender = this.gender,
        homeworldUrl = this.homeworldUrl,
        filmUrls = this.filmUrls,
        speciesUrls = this.speciesUrls,
        vehicleUrls = this.vehicleUrls,
        starshipUrls = this.starshipUrls,
        created = this.created,
        edited = this.edited
    )
}

/**
 * DTO -> SWCharacterDetailsRaw (Для UseCase напрямую из сети)
 */
fun CharacterDto.toCharacterDetailsRaw(): SWCharacterDetailsRaw {
    return SWCharacterDetailsRaw(
        character = this.toCharacter(), // Здесь создается SWCharacter (5 полей)

        hairColor = this.hairColor,
        skinColor = this.skinColor,
        eyeColor = this.eyeColor,
        birthYear = this.birthYear, // Добавляем проброс года рождения
        homeworldUrl = this.homeworldUrl,
        filmUrls = this.filmUrls,
        speciesUrls = this.speciesUrls,
        vehicleUrls = this.vehicleUrls,
        starshipUrls = this.starshipUrls,
        created = this.created,
        edited = this.edited
    )
}

/**
 * CharacterDetailsEntity -> SWCharacterDetailsRaw (Для UseCase из кэша)
 */
fun CharacterDetailsEntity.toCharacterDetailsRaw(): SWCharacterDetailsRaw {
    return SWCharacterDetailsRaw(
        character = SWCharacter(
            id = this.id,
            name = this.name,
            species = this.speciesUrls.firstOrNull()?.toSwapiId() ?: "1",
            gender = this.gender,
            mass = this.mass,
            height = this.height
        ),

        hairColor = this.hairColor,
        skinColor = this.skinColor,
        eyeColor = this.eyeColor,
        homeworldUrl = this.homeworldUrl,
        filmUrls = this.filmUrls,
        speciesUrls = this.speciesUrls,
        vehicleUrls = this.vehicleUrls,
        starshipUrls = this.starshipUrls,
        created = this.created,
        edited = this.edited,
        birthYear = this.birthYear
    )
}