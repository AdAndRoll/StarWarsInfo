package com.example.data.local.datasources

import com.example.data.local.dao.PlanetDao
import com.example.data.local.entity.PlanetDetailEntity
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class PlanetLocalDataSource @Inject constructor(
    private val planetDao: PlanetDao
) {
    /**
     * Сохраняет детали планеты.
     */
    suspend fun savePlanetDetails(planet: PlanetDetailEntity) {
        planetDao.insertPlanet(planet)
    }

    /**
     * ПОТОКОВЫЙ метод (для UI).
     * Используется, если ViewModel хочет постоянно следить за изменениями планеты.
     */
    fun getPlanetDetailsFlow(planetId: String): Flow<PlanetDetailEntity?> {
        return planetDao.getPlanetById(planetId)
    }

    /**
     * РАЗОВЫЙ метод (для Репозитория).
     * Именно этот метод решит проблему "Condition is always true".
     * В PlanetDao у тебя должен быть аналогичный метод без Flow.
     */
    suspend fun getPlanetDetails(planetId: String): PlanetDetailEntity? {
        return planetDao.getPlanetByIdOnce(planetId)
    }

    suspend fun clearAllPlanets() {
        planetDao.clearAllPlanets()
    }
}