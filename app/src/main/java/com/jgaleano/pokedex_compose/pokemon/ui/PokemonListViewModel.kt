package com.jgaleano.pokedex_compose.pokemon.ui

import androidx.compose.ui.graphics.Color
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
    private val test = mutableListOf<Triple<String, String, Color>>()

    private val _pokedexUiState = MutableStateFlow<PokedexUiState<List<Triple<String, String, Color>>>>(PokedexUiState.Loading)
    val pokedexUiState = _pokedexUiState.asStateFlow()

    fun updateBackground(
        item: Pair<String, String>,
        background: Color
    ) {
        viewModelScope.launch {
            test.add(
                Triple(item.first, item.second, background)
            )
            if (test.size == pokemonList.size) {
                _pokedexUiState.value = PokedexUiState.Success(test.sortedBy { it.first })
            }
        }
    }
}
