package com.example.data.remote.dto

import com.squareup.moshi.Json

/**
 * Замена для LocationDto.
 * Используется в CharacterDto для поля homeworld.
 */
data class PlanetSummaryDto(
    @Json(name = "name") val name: String,
    @Json(name = "url") val url: String
)