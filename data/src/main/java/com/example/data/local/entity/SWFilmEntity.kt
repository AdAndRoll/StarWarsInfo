package com.example.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey


/**
 * Сущность (Entity) для хранения информации об эпизоде в локальной базе данных Room.
 */
@Entity(tableName = "films")
data class SWFilmEntity(
    @PrimaryKey val id: String,
    val title: String,
    val episodeId: Int,
    val openingCrawl: String,
    val director: String,
    val producer: String,
    val releaseDate: String,
    val characterUrls: List<String>,
    val url: String
)