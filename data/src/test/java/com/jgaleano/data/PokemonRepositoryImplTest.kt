package com.jgaleano.data

import app.cash.turbine.test
import com.jgaleano.data.datasource.PokemonLocalDataSource
import com.jgaleano.data.datasource.PokemonRemoteDataSource
import com.jgaleano.data.repository.PokemonRepositoryImpl
import com.jgaleano.domain.model.Pokemon
import com.jgaleano.domain.model.PokedexException
import com.jgaleano.domain.model.PokemonInfo
import com.jgaleano.domain.model.PokemonPage
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Test

class PokemonRepositoryImplTest {

    private val bulbasaur = Pokemon(page = 0, name = "bulbasaur", url = "https://pokeapi.co/api/v2/pokemon/1/")
    private val ivysaur = Pokemon(page = 1, name = "ivysaur", url = "https://pokeapi.co/api/v2/pokemon/2/")
    private val bulbasaurInfo = PokemonInfo(
        id = 1, name = "bulbasaur", height = 7, weight = 69, experience = 64,
        types = listOf("grass", "poison"), stats = emptyList()
    )

    private class FakeRemote(
        private val pages: Map<Int, PokemonPage> = emptyMap(),
        private val info: PokemonInfo? = null
    ) : PokemonRemoteDataSource {
        var listCalls = 0
        var infoCalls = 0

        override suspend fun fetchPokemonList(page: Int): PokemonPage {
            listCalls++
            return pages[page] ?: throw PokedexException("no page $page")
        }

        override suspend fun fetchPokemonInfo(name: String): PokemonInfo {
            infoCalls++
            return info ?: throw PokedexException("no info for $name")
        }
    }

    private class FakeLocal : PokemonLocalDataSource {
        val pokemon = mutableListOf<Pokemon>()
        val info = mutableMapOf<String, PokemonInfo>()

        override suspend fun getPokemonList(page: Int) = pokemon.filter { it.page == page }
        override suspend fun getAllPokemonList(page: Int) = pokemon.filter { it.page <= page }
        override suspend fun insertPokemonList(pokemonList: List<Pokemon>) {
            pokemon += pokemonList
        }
        override suspend fun getPokemonInfo(name: String) = info[name]
        override suspend fun insertPokemonInfo(pokemonInfo: PokemonInfo) {
            info[pokemonInfo.name] = pokemonInfo
        }
    }

    @Test
    fun listFromNetworkIsCachedAndAccumulated() = runTest {
        val remote = FakeRemote(
            pages = mapOf(
                0 to PokemonPage(listOf(bulbasaur), isLastPage = false),
                1 to PokemonPage(listOf(ivysaur), isLastPage = true)
            )
        )
        val local = FakeLocal()
        val repository = PokemonRepositoryImpl(remote, local)

        repository.getPokemonList(0).test {
            assertEquals(PokemonPage(listOf(bulbasaur), isLastPage = false), awaitItem())
            awaitComplete()
        }
        repository.getPokemonList(1).test {
            assertEquals(PokemonPage(listOf(bulbasaur, ivysaur), isLastPage = true), awaitItem())
            awaitComplete()
        }
        assertEquals(listOf(bulbasaur, ivysaur), local.pokemon)
    }

    @Test
    fun cachedListSkipsNetwork() = runTest {
        val remote = FakeRemote()
        val local = FakeLocal().apply { pokemon += bulbasaur }

        PokemonRepositoryImpl(remote, local).getPokemonList(0).test {
            val page = awaitItem()
            assertEquals(listOf(bulbasaur), page.pokemonList)
            assertFalse(page.isLastPage)
            awaitComplete()
        }
        assertEquals(0, remote.listCalls)
    }

    @Test
    fun networkFailureIsEmittedAsError() = runTest {
        PokemonRepositoryImpl(FakeRemote(), FakeLocal()).getPokemonList(0).test {
            assertTrue(awaitError() is PokedexException)
        }
    }

    @Test
    fun infoIsFetchedOnceAndThenServedFromCache() = runTest {
        val remote = FakeRemote(info = bulbasaurInfo)
        val repository = PokemonRepositoryImpl(remote, FakeLocal())

        repeat(2) {
            repository.getPokemonInfo("bulbasaur").test {
                assertEquals(bulbasaurInfo, awaitItem())
                awaitComplete()
            }
        }
        assertEquals(1, remote.infoCalls)
    }
}
