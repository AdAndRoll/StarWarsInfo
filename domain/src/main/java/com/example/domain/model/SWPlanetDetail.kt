package com.example.domain.model

data class SWPlanetDetail(
    val id: String,
    val name: String,
    val diameter: String,
    val climate: String,
    val gravity: String,
    val terrain: String,
    val population: String,
    val residents: List<SWCharacterSummary>,
    val url: String// Список тех, кто живет на планете
)