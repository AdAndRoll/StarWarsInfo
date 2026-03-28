package com.example.domain.model



data class SWCharacterDetailsRaw(
    val character: SWCharacter,
    val hairColor: String,
    val skinColor: String,
    val eyeColor: String,
    val homeworldUrl: String,
    val birthYear: String,
    val filmUrls: List<String>,
    val speciesUrls: List<String>,
    val vehicleUrls: List<String>,
    val starshipUrls: List<String>,
    val created: String,
    val edited: String
)
