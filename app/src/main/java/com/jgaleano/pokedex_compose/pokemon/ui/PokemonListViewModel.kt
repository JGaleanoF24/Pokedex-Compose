package com.jgaleano.pokedex_compose.pokemon.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.jgaleano.presentation.ui.state.PokedexUiState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class PokemonListViewModel @Inject constructor() : ViewModel() {

    private val pokemonList = listOf(
        Pair(
            "Bulbasaur",
            "https://raw.githubusercontent.com/PokeAPI/sprites/master/sprites/pokemon/other/official-artwork/1.png"
        ),
        Pair(
            "Ivysaur",
            "https://raw.githubusercontent.com/PokeAPI/sprites/master/sprites/pokemon/other/official-artwork/2.png"
        ),
        Pair(
            "Venusaur",
            "https://raw.githubusercontent.com/PokeAPI/sprites/master/sprites/pokemon/other/official-artwork/3.png"
        ),
        Pair(
            "Charmander",
            "https://raw.githubusercontent.com/PokeAPI/sprites/master/sprites/pokemon/other/official-artwork/4.png"
        ),
        Pair(
            "Charmeleon",
            "https://raw.githubusercontent.com/PokeAPI/sprites/master/sprites/pokemon/other/official-artwork/5.png"
        ),
        Pair(
            "Charizard",
            "https://raw.githubusercontent.com/PokeAPI/sprites/master/sprites/pokemon/other/official-artwork/6.png"
        )
    )

    private val _pokedexUiState = MutableStateFlow<PokedexUiState<List<Pair<String, String>>>>(PokedexUiState.Loading)
    val pokedexUiState = _pokedexUiState.asStateFlow()

    fun getPokemonList() {
        viewModelScope.launch {
            _pokedexUiState.value = PokedexUiState.Success(pokemonList)
        }
    }
}
