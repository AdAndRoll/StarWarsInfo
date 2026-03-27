package com.example.data.remote.datasources

import android.util.Log
import com.example.data.remote.api.PlanetApiService
import com.example.data.remote.dto.PlanetDto
import com.example.data.utils.safeApiCall
import com.example.data.utils.NetworkResult
import javax.inject.Inject

/**
 * Удаленный источник данных для планет (вместо локаций).
 * Отвечает за выполнение сетевых запросов к SWAPI.
 */
class PlanetRemoteDataSource @Inject constructor(
    private val api: PlanetApiService
) {
    private val TAG = "PlanetRemoteDataSource"

    suspend fun getPlanetDetails(planetId: String): NetworkResult<PlanetDto> {
        Log.d(TAG, "Запрос деталей планеты с ID: $planetId")
        return safeApiCall {
            api.getPlanetDetails(planetId) // Просто передаем вызов API
        }
    }
}