package com.jgaleano.pokedex_compose

import androidx.lifecycle.SavedStateHandle
import com.jgaleano.domain.model.PokemonInfo
import com.jgaleano.domain.usecase.GetPokemonInfoUseCase
import com.jgaleano.pokedex_compose.navigation.PokedexRoute
import com.jgaleano.pokedex_compose.pokemon.detail.PokemonDetailViewModel
import com.jgaleano.presentation.ui.state.PokedexUiState
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Rule
import org.junit.Test

class PokemonDetailViewModelTest {

    @get:Rule
    val mainDispatcherRule = MainDispatcherRule()

    private val savedStateHandle = SavedStateHandle(mapOf(PokedexRoute.Detail.ARG_NAME to "bulbasaur"))
    private val info = PokemonInfo(
        id = 1, name = "bulbasaur", height = 7, weight = 69, experience = 64,
        types = listOf("grass"), stats = emptyList()
    )

    @Test
    fun infoIsLoadedFromTheRouteName() {
        val viewModel = PokemonDetailViewModel(GetPokemonInfoUseCase(FakePokemonRepository(info = info)), savedStateHandle)

        assertEquals(PokedexUiState.Success(info), viewModel.uiState.value)
    }

    @Test
    fun failureShowsError() {
        val viewModel = PokemonDetailViewModel(GetPokemonInfoUseCase(FakePokemonRepository()), savedStateHandle)

        assertTrue(viewModel.uiState.value is PokedexUiState.Error)
    }
}
