package com.jgaleano.framework.network.service

import com.jgaleano.framework.network.model.PokemonInfoResponse
import com.jgaleano.framework.network.model.PokemonListResponse
import com.skydoves.sandwich.ApiResponse
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface PokedexService {

    @GET("pokemon")
    suspend fun fetchPokemonList(
        @Query("limit") limit: Int = PAGE_SIZE,
        @Query("offset") offset: Int = 0,
    ): ApiResponse<PokemonListResponse>

    @GET("pokemon/{name}")
    suspend fun fetchPokemonInfo(
        @Path("name") name: String,
    ): ApiResponse<PokemonInfoResponse>

    companion object {
        const val BASE_URL = "https://pokeapi.co/api/v2/"
        const val PAGE_SIZE = 20
    }
}
