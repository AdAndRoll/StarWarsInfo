package com.example.domain.usecases

import com.example.domain.model.SWCharacterDetailed
import com.example.domain.model.SWPlanetSummary
import com.example.domain.model.SWFilmSummary
import com.example.domain.repository.CharacterRepository
import com.example.domain.repository.PlanetRepository
import com.example.domain.utils.Result
import kotlinx.coroutines.flow.first

class GetCharacterDetailsUseCase(
    private val characterRepository: CharacterRepository,
    private val planetRepository: PlanetRepository,
    private val getFilmsUseCase: GetFilmsUseCase
) {
    suspend fun execute(characterId: String): Result<SWCharacterDetailed> {
        // 1. Получаем данные персонажа
        val characterResult = characterRepository.getCharacterDetails(characterId)

        if (characterResult is Result.Error) {
            return Result.Error(characterResult.exception)
        }

        val rawData = (characterResult as Result.Success).data
        val planetId = extractId(rawData.homeworldUrl)

        // 2. Получаем фильмы (getFilmsUseCase должен возвращать Result<List<SWFilmSummary>>)
        val filmsResult = getFilmsUseCase.execute(rawData.filmUrls)

        // 3. Получаем планету
        val planetResult = try {
            planetRepository.getPlanetDetails(planetId).first()
        } catch (e: Exception) {
            Result.Error(e)
        }

        // Формируем объект SWPlanetSummary
        val homeworldSummary = if (planetResult is Result.Success) {
            SWPlanetSummary(
                name = planetResult.data.name,
                url = planetResult.data.url
            )
        } else {
            // Заглушка на случай ошибки загрузки планеты
            SWPlanetSummary(name = "Unknown Planet", url = "")
        }

        // Формируем список SWFilmSummary
        val filmsSummaries = if (filmsResult is Result.Success) {
            filmsResult.data // Здесь уже должен быть List<SWFilmSummary>
        } else {
            emptyList()
        }

        // 4. Собираем итоговую модель
        return Result.Success(
            SWCharacterDetailed(
                character = rawData.character,
                height = rawData.character.height,
                birthYear = rawData.birthYear,
                hairColor = rawData.hairColor,
                skinColor = rawData.skinColor,
                eyeColor = rawData.eyeColor,
                homeworld = homeworldSummary, // Передаем объект, а не строку
                films = filmsSummaries,       // Передаем список объектов, а не строк
                species = emptyList(),
                vehicles = emptyList(),
                starships = emptyList(),
                created = rawData.created,
                edited = rawData.edited
            )
        )
    }

    private fun extractId(url: String): String {
        return url.trimEnd('/').substringAfterLast("/")
    }
}