package com.example.starwarsinfo.presentation.character_detail

import com.example.domain.model.SWCharacterDetailed

/**
 * Состояния экрана деталей персонажа.
 */
sealed class CharacterDetailState {
    object Loading : CharacterDetailState()

    data class Success(
        val character: SWCharacterDetailed
    ) : CharacterDetailState()

    data class Error(
        val message: String
    ) : CharacterDetailState()
}