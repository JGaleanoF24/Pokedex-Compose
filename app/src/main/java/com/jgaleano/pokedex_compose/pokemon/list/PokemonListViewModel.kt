package com.jgaleano.pokedex_compose.pokemon.list

import androidx.lifecycle.viewModelScope
import com.jgaleano.domain.usecase.GetPokemonListUseCase
import com.jgaleano.presentation.mvi.MviViewModel
import com.jgaleano.presentation.ui.state.PokedexUiState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onCompletion
import kotlinx.coroutines.flow.onEach
import javax.inject.Inject

@HiltViewModel
class PokemonListViewModel @Inject constructor(
    private val getPokemonListUseCase: GetPokemonListUseCase
) : MviViewModel<PokemonListUiState, PokemonListIntent, PokemonListEffect>(PokedexUiState.Loading) {

    private var lastLoadedPage = -1
    private var isLastPage = false
    private var isLoading = false

    init {
        loadNextPage()
    }

    override fun onIntent(intent: PokemonListIntent) {
        when (intent) {
            PokemonListIntent.LoadNextPage, PokemonListIntent.Retry -> loadNextPage()
            is PokemonListIntent.PokemonClicked ->
                sendEffect(PokemonListEffect.NavigateToDetail(intent.pokemon))
            PokemonListIntent.SettingsClicked -> sendEffect(PokemonListEffect.NavigateToSettings)
        }
    }

    private fun loadNextPage() {
        if (isLoading || isLastPage) return
        isLoading = true

        val page = lastLoadedPage + 1
        setState { updateSuccess { copy(isLoadingMore = true) } ?: PokedexUiState.Loading }

        getPokemonListUseCase(page)
            .onEach { pokemonPage ->
                lastLoadedPage = page
                isLastPage = pokemonPage.isLastPage
                setState {
                    if (pokemonPage.pokemonList.isEmpty()) {
                        PokedexUiState.Empty(EMPTY_MESSAGE)
                    } else {
                        PokedexUiState.Success(
                            PokemonListData(
                                pokemonList = pokemonPage.pokemonList,
                                isLastPage = pokemonPage.isLastPage
                            )
                        )
                    }
                }
            }
            .catch { error ->
                // keep the loaded Pokemon on screen when only a later page fails
                setState {
                    updateSuccess { copy(isLoadingMore = false) }
                        ?: PokedexUiState.Error(error.message ?: UNKNOWN_ERROR)
                }
                sendEffect(PokemonListEffect.ShowError(error.message))
            }
            .onCompletion { isLoading = false }
            .launchIn(viewModelScope)
    }

    private fun PokemonListUiState.updateSuccess(
        reducer: PokemonListData.() -> PokemonListData
    ): PokemonListUiState? = (this as? PokedexUiState.Success)?.let { PokedexUiState.Success(it.data.reducer()) }

    companion object {
        const val EMPTY_MESSAGE = "No Pokémon found"
        const val UNKNOWN_ERROR = "Something went wrong"
    }
}
