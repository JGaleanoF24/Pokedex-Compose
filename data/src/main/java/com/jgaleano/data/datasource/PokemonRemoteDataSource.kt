package com.jgaleano.data.datasource

import com.jgaleano.domain.model.PokemonInfo
import com.jgaleano.domain.model.PokemonPage

interface PokemonRemoteDataSource {

    /** Returns only the Pokemon of [page]. Throws a PokedexException when the request fails. */
    suspend fun fetchPokemonList(page: Int): PokemonPage

    /** Throws a PokedexException when the request fails. */
    suspend fun fetchPokemonInfo(name: String): PokemonInfo
}
