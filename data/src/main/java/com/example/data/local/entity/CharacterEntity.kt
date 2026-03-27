package com.example.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

/**
 * Сущность для краткого списка персонажей (свернутый вид).
 * Хранит только те данные, которые нужны для карточки в общем списке.
 */
@Entity(tableName = "characters")
data class CharacterEntity(
    @PrimaryKey val id: String,
    val name: String,
    val species: String, // Храним ID расы или "Human"
    val gender: String,
    val mass: String,    // Добавили вес для отображения в списке
    val birthYear: String,
    val homeworldUrl: String
)