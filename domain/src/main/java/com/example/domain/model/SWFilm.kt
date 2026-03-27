package com.example.domain.model

/**
 * Полная информация о фильме
 */
data class SWFilm(
    val id: String,
    val title: String,            // В SWAPI это 'title', а не 'name'
    val episodeId: Int,           // Номер эпизода (например, 4)
    val openingCrawl: String,     // Те самые ползущие титры
    val director: String,
    val producer: String,
    val releaseDate: String,      // Дата выхода
    val characters: List<SWCharacterSummary>, // Список героев фильма
    val url: String
)