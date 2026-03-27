package com.example.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "remote_keys")
data class RemoteKeyEntity(
    @PrimaryKey(autoGenerate = false)
    val id: Int = 0, // У нас обычно один ключ для управления списком
    val prevKey: Int?,
    val nextKey: Int?,
    val createdAt: Long,
    val filterName: String? = null // Оставляем только то, что поддерживает API
)