package com.example.data.remote.dto

import com.squareup.moshi.Json

data class CharacterDto(
    @Json(name = "name") val name: String,
    @Json(name = "height") val height: String,
    @Json(name = "mass") val mass: String,
    @Json(name = "hair_color") val hairColor: String,
    @Json(name = "skin_color") val skinColor: String,
    @Json(name = "eye_color") val eyeColor: String,
    @Json(name = "birth_year") val birthYear: String,
    @Json(name = "gender") val gender: String,
    @Json(name = "homeworld") val homeworldUrl: String,
    @Json(name = "films") val filmUrls: List<String>,
    @Json(name = "species") val speciesUrls: List<String>,
    @Json(name = "vehicles") val vehicleUrls: List<String>,   // Новое
    @Json(name = "starships") val starshipUrls: List<String>, // Новое
    @Json(name = "url") val url: String,
    @Json(name = "created") val created: String,
    @Json(name = "edited") val edited: String
)