package com.jgaleano.framework.datasource

import com.jgaleano.data.datasource.PokemonRemoteDataSource
import com.jgaleano.domain.model.PokedexException
import com.jgaleano.domain.model.PokemonInfo
import com.jgaleano.domain.model.PokemonPage
import com.jgaleano.framework.network.mapper.asDomain
import com.jgaleano.framework.network.service.PokedexService
import com.skydoves.sandwich.ApiResponse
import com.skydoves.sandwich.message
import javax.inject.Inject

class PokemonRemoteDataSourceImpl @Inject constructor(
    private val pokedexService: PokedexService
) : PokemonRemoteDataSource {

    override suspend fun fetchPokemonList(page: Int): PokemonPage {
        val response = pokedexService.fetchPokemonList(
            limit = PokedexService.PAGE_SIZE,
            offset = page * PokedexService.PAGE_SIZE
        )
        val body = response.getOrThrowPokedexException()
        return PokemonPage(
            pokemonList = body.results.map { it.asDomain(page) },
            isLastPage = body.next == null
        )
    }

    override suspend fun fetchPokemonInfo(name: String): PokemonInfo =
        pokedexService.fetchPokemonInfo(name).getOrThrowPokedexException().asDomain()

    private fun <T> ApiResponse<T>.getOrThrowPokedexException(): T = when (this) {
        is ApiResponse.Success -> data
        is ApiResponse.Failure -> throw PokedexException(message())
    }
}
