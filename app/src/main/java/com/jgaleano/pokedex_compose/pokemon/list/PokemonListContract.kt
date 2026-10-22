package com.jgaleano.pokedex_compose.pokemon.list

import com.jgaleano.domain.model.Pokemon
import com.jgaleano.presentation.mvi.UiEffect
import com.jgaleano.presentation.mvi.UiIntent
import com.jgaleano.presentation.ui.state.PokedexUiState

typealias PokemonListUiState = PokedexUiState<PokemonListData>

data class PokemonListData(
    val pokemonList: List<Pokemon>,
    val isLoadingMore: Boolean = false,
    val isLastPage: Boolean = false
)

sealed interface PokemonListIntent : UiIntent {
    /** The list scrolled close to its end. */
    data object LoadNextPage : PokemonListIntent

    /** The first page failed and the user asked to try again. */
    data object Retry : PokemonListIntent

    data class PokemonClicked(val pokemon: Pokemon) : PokemonListIntent

    data object SettingsClicked : PokemonListIntent
}

sealed interface PokemonListEffect : UiEffect {
    data class ShowError(val message: String?) : PokemonListEffect

    data class NavigateToDetail(val pokemon: Pokemon) : PokemonListEffect

    data object NavigateToSettings : PokemonListEffect
}
