package com.jgaleano.pokedex_compose.pokemon.detail

import com.jgaleano.domain.model.PokemonInfo
import com.jgaleano.presentation.mvi.UiEffect
import com.jgaleano.presentation.mvi.UiIntent
import com.jgaleano.presentation.ui.state.PokedexUiState

typealias PokemonDetailUiState = PokedexUiState<PokemonInfo>

sealed interface PokemonDetailIntent : UiIntent {
    data object Retry : PokemonDetailIntent

    data object BackClicked : PokemonDetailIntent
}

sealed interface PokemonDetailEffect : UiEffect {
    data object NavigateBack : PokemonDetailEffect
}
