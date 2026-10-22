package com.jgaleano.pokedex_compose

import com.jgaleano.domain.model.PokedexException
import com.jgaleano.domain.model.Pokemon
import com.jgaleano.domain.model.PokemonInfo
import com.jgaleano.domain.model.PokemonPage
import com.jgaleano.domain.model.UiTheme
import com.jgaleano.domain.model.UserData
import com.jgaleano.domain.repository.PokemonRepository
import com.jgaleano.domain.repository.UserDataRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.flow

fun pokemon(id: Int, page: Int = 0) =
    Pokemon(page = page, name = "pokemon$id", url = "https://pokeapi.co/api/v2/pokemon/$id/")

class FakePokemonRepository(
    private val pages: Map<Int, PokemonPage> = emptyMap(),
    private val info: PokemonInfo? = null
) : PokemonRepository {
    val requestedPages = mutableListOf<Int>()

    override fun getPokemonList(page: Int): Flow<PokemonPage> = flow {
        requestedPages += page
        emit(pages[page] ?: throw PokedexException("page $page failed"))
    }

    override fun getPokemonInfo(name: String): Flow<PokemonInfo> = flow {
        emit(info ?: throw PokedexException("info failed"))
    }
}

class FakeUserDataRepository(uiTheme: UiTheme = UiTheme.FOLLOW_SYSTEM) : UserDataRepository {
    private val data = MutableStateFlow(UserData(uiTheme))
    override val userData: Flow<UserData> = data
    override suspend fun setUiTheme(uiTheme: UiTheme) {
        data.value = data.value.copy(uiTheme = uiTheme)
    }
}
