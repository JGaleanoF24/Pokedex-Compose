package com.jgaleano.data.datasource

import com.jgaleano.domain.model.Pokemon
import com.jgaleano.domain.model.PokemonInfo

interface PokemonLocalDataSource {

    /** Pokemon stored for exactly [page]. */
    suspend fun getPokemonList(page: Int): List<Pokemon>

    /** Pokemon stored from the first page up to [page]. */
    suspend fun getAllPokemonList(page: Int): List<Pokemon>

    suspend fun insertPokemonList(pokemonList: List<Pokemon>)

    suspend fun getPokemonInfo(name: String): PokemonInfo?

    suspend fun insertPokemonInfo(pokemonInfo: PokemonInfo)
}
