package com.jgaleano.pokedex_compose.navigation

import com.jgaleano.domain.model.Pokemon

sealed class PokedexRoute(val route: String) {

    data object List : PokedexRoute("pokemon_list")

    data object Detail : PokedexRoute("pokemon_detail/{name}/{id}") {
        const val ARG_NAME = "name"
        const val ARG_ID = "id"

        fun create(pokemon: Pokemon) = "pokemon_detail/${pokemon.name}/${pokemon.id}"
    }

    data object Settings : PokedexRoute("settings")
}

/** Shared element key that links a Pokemon image in the list with the one in its detail. */
fun pokemonImageKey(name: String) = "pokemon-image-$name"
