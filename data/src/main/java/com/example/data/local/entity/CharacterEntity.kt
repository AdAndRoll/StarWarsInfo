package com.example.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

/**
 * Сущность для краткого списка персонажей (свернутый вид).
 * Хранит только те данные, которые нужны для карточки в общем списке.
 */
@Entity(tableName = "characters")
data class CharacterEntity(
    @PrimaryKey
    val id: String, // Уникальный ID из URL (например, "1")
    val name: String,
    val species: String,
    val gender: String,
    val mass: String,
    val height: String,
    val birthYear: String,
    val homeworldUrl: String,

    // Поле для стабильной сортировки в БД
    val orderInList: Int = 0
)