package com.jgaleano.domain

import app.cash.turbine.test
import com.jgaleano.domain.model.Pokemon
import com.jgaleano.domain.model.PokemonInfo
import com.jgaleano.domain.model.PokemonPage
import com.jgaleano.domain.model.UiTheme
import com.jgaleano.domain.model.UserData
import com.jgaleano.domain.repository.PokemonRepository
import com.jgaleano.domain.repository.UserDataRepository
import com.jgaleano.domain.usecase.GetPokemonInfoUseCase
import com.jgaleano.domain.usecase.GetPokemonListUseCase
import com.jgaleano.domain.usecase.GetUserDataUseCase
import com.jgaleano.domain.usecase.SetUiThemeUseCase
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.emptyFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Test

class UseCasesTest {

    private val bulbasaur = Pokemon(name = "bulbasaur", url = "https://pokeapi.co/api/v2/pokemon/1/")

    private class FakePokemonRepository(
        private val page: PokemonPage = PokemonPage(emptyList(), isLastPage = false)
    ) : PokemonRepository {
        var requestedName: String? = null

        override fun getPokemonList(page: Int): Flow<PokemonPage> = flowOf(this.page)

        override fun getPokemonInfo(name: String): Flow<PokemonInfo> {
            requestedName = name
            return emptyFlow()
        }
    }

    private class FakeUserDataRepository : UserDataRepository {
        private val data = MutableStateFlow(UserData(UiTheme.FOLLOW_SYSTEM))
        override val userData: Flow<UserData> = data
        override suspend fun setUiTheme(uiTheme: UiTheme) {
            data.value = data.value.copy(uiTheme = uiTheme)
        }
    }

    @Test
    fun getPokemonListEmitsRepositoryPage() = runTest {
        val page = PokemonPage(listOf(bulbasaur), isLastPage = true)

        GetPokemonListUseCase(FakePokemonRepository(page))(0).test {
            assertEquals(page, awaitItem())
            awaitComplete()
        }
    }

    @Test(expected = IllegalArgumentException::class)
    fun getPokemonListRejectsNegativePage() {
        GetPokemonListUseCase(FakePokemonRepository())(-1)
    }

    @Test
    fun getPokemonInfoRequestsLowercaseName() = runTest {
        val repository = FakePokemonRepository()

        GetPokemonInfoUseCase(repository)("Bulbasaur").test { awaitComplete() }

        assertEquals("bulbasaur", repository.requestedName)
    }

    @Test
    fun setUiThemeIsReflectedInUserData() = runTest {
        val repository = FakeUserDataRepository()

        SetUiThemeUseCase(repository)(UiTheme.DARK)

        assertEquals(UiTheme.DARK, GetUserDataUseCase(repository)().first().uiTheme)
    }

    @Test
    fun pokemonExposesIdAndArtwork() {
        assertEquals(1, bulbasaur.id)
        assertEquals(
            "https://raw.githubusercontent.com/PokeAPI/sprites/master/sprites/pokemon/other/official-artwork/1.png",
            bulbasaur.getImageUrl()
        )
    }

    @Test
    fun pokemonInfoFormatsValues() {
        val info = PokemonInfo(
            id = 1, name = "bulbasaur", height = 7, weight = 69, experience = 64,
            types = listOf("grass"), stats = emptyList()
        )

        assertEquals("#001", info.getIdString())
        assertEquals("6.9 KG", info.getWeightString())
        assertEquals("0.7 M", info.getHeightString())
        assertEquals(0, info.getStat("hp"))
    }
}
