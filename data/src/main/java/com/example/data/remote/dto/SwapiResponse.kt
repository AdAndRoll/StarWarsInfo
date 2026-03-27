package com.example.data.remote.dto

import com.squareup.moshi.Json

data class SwapiResponse<T>(
    @Json(name = "count") val count: Int,
    @Json(name = "next") val next: String?,
    @Json(name = "previous") val previous: String?,
    @Json(name = "results") val results: List<T>
)