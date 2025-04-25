package com.jgaleano.pokedex_compose.pokemon.ui

import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.jgaleano.presentation.ui.component.card.PokemonCard
import com.jgaleano.presentation.ui.state.PokedexUiState

@Composable
fun PokemonList(
    viewModel: PokemonListViewModel = hiltViewModel()
) {
    val pokedexUiState by viewModel.pokedexUiState.collectAsState()

    when (val state = pokedexUiState) {
        is PokedexUiState.Loading -> {
            CircularProgressIndicator()
        }
        is PokedexUiState.Success -> {
            LazyVerticalGrid(
                modifier = Modifier.padding(6.dp),
                columns = GridCells.Fixed(2)
            ) {
                items(state.data) { pokemon ->
                    PokemonItem(pokemon)
                }
            }
        }
        is PokedexUiState.Error -> {
            Text(state.error)
        }
        else -> {
            CircularProgressIndicator()
        }
    }
}

@Composable
private fun PokemonItem(pokemon: Triple<String, String, Color>) {
    PokemonCard(
        name = pokemon.first,
        image = pokemon.second,
        backgroundColor = pokemon.third
    )
}
