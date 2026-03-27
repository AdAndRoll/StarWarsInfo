package com.example.data.remote.dto

import com.squareup.moshi.Json

data class PlanetDto(
    @Json(name = "name") val name: String,
    @Json(name = "rotation_period") val rotationPeriod: String,
    @Json(name = "orbital_period") val orbitalPeriod: String,
    @Json(name = "diameter") val diameter: String,
    @Json(name = "climate") val climate: String,
    @Json(name = "gravity") val gravity: String,
    @Json(name = "terrain") val terrain: String,
    @Json(name = "population") val population: String,
    @Json(name = "residents") val residentUrls: List<String>,
    @Json(name = "url") val url: String
)