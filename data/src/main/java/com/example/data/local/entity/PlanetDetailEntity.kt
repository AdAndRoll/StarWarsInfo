package com.example.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

/**
 * Сущность Room для хранения детальной информации о локации.
 *
 * Эта сущность представляет таблицу `location_details` в базе данных.
 *
 * @param id Уникальный идентификатор локации.
 * @param name Название локации.
 * @param type Тип локации.
 * @param dimension Измерение, к которому относится локация.
 * @param residents Список URL-адресов персонажей, проживающих в локации.
 * @param url URL локации.
 * @param created Дата создания локации.
 */
@Entity(tableName = "planet_details")
data class PlanetDetailEntity(
    @PrimaryKey val id: String,
    val name: String,
    val diameter: String,
    val climate: String,
    val gravity: String,
    val terrain: String,
    val population: String,
    val residentUrls: List<String>,
    val url: String
) {
    fun getResidentIds(): List<String> {
        return residentUrls.map { it.trimEnd('/').substringAfterLast("/") }
            .filter { it.isNotEmpty() }
    }
}
