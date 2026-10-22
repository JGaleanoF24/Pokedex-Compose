package com.jgaleano.data.repository

import com.jgaleano.data.datasource.PokemonLocalDataSource
import com.jgaleano.data.datasource.PokemonRemoteDataSource
import com.jgaleano.domain.model.PokemonInfo
import com.jgaleano.domain.model.PokemonPage
import com.jgaleano.domain.repository.PokemonRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

/**
 * Reads from the local cache first and only goes to the network for what isn't stored yet.
 */
class PokemonRepositoryImpl @Inject constructor(
    private val remoteDataSource: PokemonRemoteDataSource,
    private val localDataSource: PokemonLocalDataSource
) : PokemonRepository {

    override fun getPokemonList(page: Int): Flow<PokemonPage> = flow {
        val isLastPage = if (localDataSource.getPokemonList(page).isEmpty()) {
            val remotePage = remoteDataSource.fetchPokemonList(page)
            localDataSource.insertPokemonList(remotePage.pokemonList)
            remotePage.isLastPage
        } else {
            // the cache doesn't know if more pages exist; fetching the next page will tell.
            false
        }
        emit(PokemonPage(localDataSource.getAllPokemonList(page), isLastPage))
    }

    override fun getPokemonInfo(name: String): Flow<PokemonInfo> = flow {
        val cachedInfo = localDataSource.getPokemonInfo(name)
        if (cachedInfo != null) {
            emit(cachedInfo)
        } else {
            val remoteInfo = remoteDataSource.fetchPokemonInfo(name)
            localDataSource.insertPokemonInfo(remoteInfo)
            emit(remoteInfo)
        }
    }
}
