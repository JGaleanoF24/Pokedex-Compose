package com.jgaleano.pokedex_compose.pokemon.detail

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.viewModelScope
import com.jgaleano.domain.usecase.GetPokemonInfoUseCase
import com.jgaleano.pokedex_compose.navigation.PokedexRoute
import com.jgaleano.presentation.mvi.MviViewModel
import com.jgaleano.presentation.ui.state.PokedexUiState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import javax.inject.Inject

@HiltViewModel
class PokemonDetailViewModel @Inject constructor(
    private val getPokemonInfoUseCase: GetPokemonInfoUseCase,
    savedStateHandle: SavedStateHandle
) : MviViewModel<PokemonDetailUiState, PokemonDetailIntent, PokemonDetailEffect>(PokedexUiState.Loading) {

    private val pokemonName: String = checkNotNull(savedStateHandle[PokedexRoute.Detail.ARG_NAME])
    private var loadJob: Job? = null

    init {
        loadPokemonInfo()
    }

    override fun onIntent(intent: PokemonDetailIntent) {
        when (intent) {
            PokemonDetailIntent.Retry -> loadPokemonInfo()
            PokemonDetailIntent.BackClicked -> sendEffect(PokemonDetailEffect.NavigateBack)
        }
    }

    private fun loadPokemonInfo() {
        loadJob?.cancel()
        setState { PokedexUiState.Loading }
        loadJob = getPokemonInfoUseCase(pokemonName)
            .onEach { pokemonInfo -> setState { PokedexUiState.Success(pokemonInfo) } }
            .catch { error -> setState { PokedexUiState.Error(error.message ?: UNKNOWN_ERROR) } }
            .launchIn(viewModelScope)
    }

    companion object {
        const val UNKNOWN_ERROR = "Something went wrong"
    }
}
