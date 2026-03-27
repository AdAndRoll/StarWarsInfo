package com.example.domain.model

/**
 * Легковесная модель данных для эпизода, используемая на экране деталей персонажа.
 * Содержит только ID и название, что достаточно для списка и навигации.
 *
 * @property id Уникальный идентификатор эпизода.
 * @property name Название эпизода.
 */
data class SWFilmSummary(
    val id: String,
    val title: String,
    val url: String
)