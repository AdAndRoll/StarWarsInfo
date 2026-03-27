package com.example.starwarsinfo.presentation.planet_detail

import com.example.domain.model.SWPlanetDetail

/**
 * Состояния экрана деталей планеты.
 * Используем SWPlanetDetail, где уже есть климат, диаметр и население.
 */
sealed class PlanetDetailState {
    /**
     * Состояние ожидания ответа от SWAPI.
     */
    object Loading : PlanetDetailState()

    /**
     * Состояние успешной загрузки данных о планете.
     * @param planet Объект [SWPlanetDetail] со всеми характеристиками и жителями.
     */
    data class Success(val planet: SWPlanetDetail) : PlanetDetailState()

    /**
     * Состояние ошибки (например, если Татуин внезапно исчез из архивов).
     * @param message Понятное описание ошибки.
     */
    data class Error(val message: String) : PlanetDetailState()
}