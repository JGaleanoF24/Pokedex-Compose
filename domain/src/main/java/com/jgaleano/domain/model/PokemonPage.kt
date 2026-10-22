package com.jgaleano.domain.model

/**
 * Every [Pokemon] loaded up to a page, and whether the API has no more pages.
 */
data class PokemonPage(
    val pokemonList: List<Pokemon>,
    val isLastPage: Boolean
)
