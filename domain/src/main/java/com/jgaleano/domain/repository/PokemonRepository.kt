package com.jgaleano.domain.repository

import com.jgaleano.domain.model.PokemonInfo
import com.jgaleano.domain.model.PokemonPage
import kotlinx.coroutines.flow.Flow

interface PokemonRepository {

    /** Emits every Pokemon up to [page]. Failures are emitted as a PokedexException. */
    fun getPokemonList(page: Int): Flow<PokemonPage>

    /** Emits the details of the Pokemon called [name]. Failures are emitted as a PokedexException. */
    fun getPokemonInfo(name: String): Flow<PokemonInfo>
}
