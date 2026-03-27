package com.example.domain.repository

import com.example.domain.model.SWPlanetDetail
import com.example.domain.utils.Result
import kotlinx.coroutines.flow.Flow

/**
 * Репозиторий для работы с данными о планетах (вместо локаций).
 */
interface PlanetRepository {

    /**
     * Получает полную информацию о планете по её ID.
     */
    fun getPlanetDetails(planetId: String): Flow<Result<SWPlanetDetail>>
}