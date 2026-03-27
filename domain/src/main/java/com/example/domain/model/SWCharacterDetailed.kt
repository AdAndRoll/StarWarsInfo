package com.example.domain.model

data class SWCharacterDetailed(
    val character: SWCharacter, // Базовая инфа (имя, пол и т.д.)
    val height: String,
    val birthYear: String,
    val hairColor: String,
    val skinColor: String,
    val eyeColor: String,
    val homeworld: SWPlanetSummary,    // Теперь это объект с именем и URL
    val films: List<SWFilmSummary>,     // Список объектов (ID + Название)
    val species: List<String>,          // Пока оставим строками
    val vehicles: List<String>,
    val starships: List<String>,
    val created: String,
    val edited: String
)