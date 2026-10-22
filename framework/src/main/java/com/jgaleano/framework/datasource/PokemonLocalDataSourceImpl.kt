package com.jgaleano.framework.datasource

import com.jgaleano.data.datasource.PokemonLocalDataSource
import com.jgaleano.domain.model.Pokemon
import com.jgaleano.domain.model.PokemonInfo
import com.jgaleano.framework.database.PokemonDao
import com.jgaleano.framework.database.entity.asDomain
import com.jgaleano.framework.database.entity.asEntity
import javax.inject.Inject

class PokemonLocalDataSourceImpl @Inject constructor(
    private val pokemonDao: PokemonDao
) : PokemonLocalDataSource {

    override suspend fun getPokemonList(page: Int): List<Pokemon> =
        pokemonDao.getPokemonList(page).map { it.asDomain() }

    override suspend fun getAllPokemonList(page: Int): List<Pokemon> =
        pokemonDao.getAllPokemonList(page).map { it.asDomain() }

    override suspend fun insertPokemonList(pokemonList: List<Pokemon>) =
        pokemonDao.insertPokemonList(pokemonList.map { it.asEntity() })

    override suspend fun getPokemonInfo(name: String): PokemonInfo? =
        pokemonDao.getPokemonInfo(name)?.asDomain()

    override suspend fun insertPokemonInfo(pokemonInfo: PokemonInfo) =
        pokemonDao.insertPokemonInfo(pokemonInfo.asEntity())
}
