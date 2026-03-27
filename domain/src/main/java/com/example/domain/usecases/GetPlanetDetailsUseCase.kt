package com.example.domain.usecases

import com.example.domain.model.SWPlanetDetail
import com.example.domain.repository.PlanetRepository
import com.example.domain.utils.Result
import kotlinx.coroutines.flow.Flow

/**
 * Use Case для получения детальной информации о планете.
 */
class GetPlanetDetailsUseCase(
    private val planetRepository: PlanetRepository
) {
    fun execute(planetId: String): Flow<Result<SWPlanetDetail>> {
        return planetRepository.getPlanetDetails(planetId)
    }
}