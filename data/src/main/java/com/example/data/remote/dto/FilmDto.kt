package com.example.data.remote.dto

import com.squareup.moshi.Json

data class FilmDto(
    @Json(name = "title") val title: String,
    @Json(name = "episode_id") val episodeId: Int,
    @Json(name = "opening_crawl") val openingCrawl: String,
    @Json(name = "director") val director: String,
    @Json(name = "producer") val producer: String,
    @Json(name = "release_date") val releaseDate: String,
    @Json(name = "characters") val characterUrls: List<String>,
    @Json(name = "url") val url: String,
    @Json(name = "created") val created: String
)