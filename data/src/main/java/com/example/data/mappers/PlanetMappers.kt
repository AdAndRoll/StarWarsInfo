package com.example.data.mappers

import com.example.data.local.entity.PlanetDetailEntity
import com.example.data.remote.dto.PlanetDto
import com.example.data.remote.dto.PlanetSummaryDto
import com.example.domain.model.SWPlanetDetail
import com.example.domain.model.SWPlanetSummary
import com.example.domain.model.SWCharacterSummary

/**
 * 1. DTO -> Entity (Сохранение в локальный кэш)
 */
fun PlanetDto.toEntity(): PlanetDetailEntity {
    return PlanetDetailEntity(
        id = this.url.toSwapiId(),
        name = this.name,
        diameter = this.diameter,
        climate = this.climate,
        gravity = this.gravity,
        terrain = this.terrain,
        population = this.population,
        residentUrls = this.residentUrls,
        url = this.url
    )
}

/**
 * 2. Entity -> Domain (Детальная информация для экрана планеты)
 * Принимает уже загруженные данные о жителях.
 */
fun PlanetDetailEntity.toDomainModel(residents: List<SWCharacterSummary>): SWPlanetDetail {
    return SWPlanetDetail(
        id = this.id,
        name = this.name,
        diameter = this.diameter,
        climate = this.climate,
        gravity = this.gravity,
        terrain = this.terrain,
        population = this.population,
        residents = residents,
        url = this.url
    )
}

/**
 * 3. DTO -> Domain (Прямой маппинг из сети, если кэш не нужен)
 */
fun PlanetDto.toDomainModel(residents: List<SWCharacterSummary>): SWPlanetDetail {
    return SWPlanetDetail(
        id = this.url.toSwapiId(),
        name = this.name,
        diameter = this.diameter,
        climate = this.climate,
        gravity = this.gravity,
        terrain = this.terrain,
        population = this.population,
        residents = residents,
        url = this.url
    )
}

/**
 * 4. Summary DTO -> Summary Domain
 * Используется для отображения названия родной планеты в деталях персонажа.
 */
fun PlanetSummaryDto.toDomainSummary(): SWPlanetSummary {
    return SWPlanetSummary(
        name = this.name,
        url = this.url
    )
}