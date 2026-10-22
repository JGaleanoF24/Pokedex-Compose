package com.jgaleano.pokedex_compose

import app.cash.turbine.test
import com.jgaleano.domain.model.PokemonPage
import com.jgaleano.domain.usecase.GetPokemonListUseCase
import com.jgaleano.pokedex_compose.pokemon.list.PokemonListEffect
import com.jgaleano.pokedex_compose.pokemon.list.PokemonListIntent
import com.jgaleano.pokedex_compose.pokemon.list.PokemonListViewModel
import com.jgaleano.presentation.ui.state.PokedexUiState
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Rule
import org.junit.Test

class PokemonListViewModelTest {

    @get:Rule
    val mainDispatcherRule = MainDispatcherRule()

    private val firstPage = PokemonPage(listOf(pokemon(1)), isLastPage = false)
    private val lastPage = PokemonPage(listOf(pokemon(1), pokemon(2, page = 1)), isLastPage = true)

    @Test
    fun firstPageIsLoadedOnStart() {
        val viewModel = PokemonListViewModel(GetPokemonListUseCase(FakePokemonRepository(mapOf(0 to firstPage))))

        val state = viewModel.uiState.value as PokedexUiState.Success
        assertEquals(firstPage.pokemonList, state.data.pokemonList)
    }

    @Test
    fun paginationStopsAtTheLastPage() {
        val repository = FakePokemonRepository(mapOf(0 to firstPage, 1 to lastPage))
        val viewModel = PokemonListViewModel(GetPokemonListUseCase(repository))

        viewModel.onIntent(PokemonListIntent.LoadNextPage)
        viewModel.onIntent(PokemonListIntent.LoadNextPage)

        val state = viewModel.uiState.value as PokedexUiState.Success
        assertEquals(lastPage.pokemonList, state.data.pokemonList)
        assertTrue(state.data.isLastPage)
        assertEquals(listOf(0, 1), repository.requestedPages)
    }

    @Test
    fun firstPageFailureShowsErrorAndToast() = runTest {
        val viewModel = PokemonListViewModel(GetPokemonListUseCase(FakePokemonRepository()))

        assertTrue(viewModel.uiState.value is PokedexUiState.Error)
        viewModel.effect.test {
            assertTrue(awaitItem() is PokemonListEffect.ShowError)
        }
    }

    @Test
    fun laterPageFailureKeepsLoadedPokemon() = runTest {
        val viewModel = PokemonListViewModel(GetPokemonListUseCase(FakePokemonRepository(mapOf(0 to firstPage))))

        viewModel.onIntent(PokemonListIntent.LoadNextPage)

        val state = viewModel.uiState.value as PokedexUiState.Success
        assertEquals(firstPage.pokemonList, state.data.pokemonList)
        assertEquals(false, state.data.isLoadingMore)
    }

    @Test
    fun clickingAPokemonNavigatesToItsDetail() = runTest {
        val viewModel = PokemonListViewModel(GetPokemonListUseCase(FakePokemonRepository(mapOf(0 to firstPage))))

        viewModel.effect.test {
            viewModel.onIntent(PokemonListIntent.PokemonClicked(pokemon(1)))
            assertEquals(PokemonListEffect.NavigateToDetail(pokemon(1)), awaitItem())
        }
    }
}
