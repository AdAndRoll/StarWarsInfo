package com.example.data.local.entity

import androidx.room.Embedded
import androidx.room.Entity
import androidx.room.PrimaryKey

/**
 * Сущность Room для хранения ПОЛНОЙ информации о персонаже.
 * Представляет строку в таблице "character_details".
 * Используется для кэширования данных, полученных с экрана деталей.
 *
 * @param id Уникальный идентификатор персонажа, является первичным ключом.
 * @param name Имя персонажа.
 * @param status Статус персонажа (e.g., "Alive", "Dead").
 * @param species Вид персонажа (e.g., "Human", "Alien").
 * @param type Тип персонажа.
 * @param gender Пол персонажа.
 * @param imageUrl URL изображения персонажа.
 * @param origin Информация о месте происхождения персонажа.
 * @param location Информация о текущем местоположении персонажа.
 * @param episodeUrls Список URL-адресов эпизодов, в которых появлялся персонаж.
 */
@Entity(tableName = "character_details")
data class CharacterDetailsEntity(
    @PrimaryKey val id: String,
    val name: String,
    val height: String,
    val mass: String,
    val hairColor: String,
    val skinColor: String,
    val eyeColor: String,
    val birthYear: String,
    val gender: String,
    val homeworldUrl: String,
    val filmUrls: List<String>,
    val speciesUrls: List<String>,
    val vehicleUrls: List<String>,
    val starshipUrls: List<String>,
    val created: String,
    val edited: String
)
