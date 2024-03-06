package com.jgaleano.pokedex_compose.pokemon.ui

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import coil.compose.AsyncImage
import com.jgaleano.pokedex_compose.R
import com.jgaleano.presentation.ui.theme.PokedexComposeTheme

@Composable
fun PokemonList(
    viewModel: PokemonListViewModel = hiltViewModel()
) {
    val pokedex = listOf(
        Pair("Bulbasaur", "https://raw.githubusercontent.com/PokeAPI/sprites/master/sprites/pokemon/other/official-artwork/1.png"),
        Pair("Ivysaur", "https://raw.githubusercontent.com/PokeAPI/sprites/master/sprites/pokemon/other/official-artwork/2.png"),
        Pair("Venusaur", "https://raw.githubusercontent.com/PokeAPI/sprites/master/sprites/pokemon/other/official-artwork/3.png"),
        Pair("Charmander", "https://raw.githubusercontent.com/PokeAPI/sprites/master/sprites/pokemon/other/official-artwork/4.png"),
        Pair("Charmeleon", "https://raw.githubusercontent.com/PokeAPI/sprites/master/sprites/pokemon/other/official-artwork/5.png"),
        Pair("Charizard", "https://raw.githubusercontent.com/PokeAPI/sprites/master/sprites/pokemon/other/official-artwork/6.png"),

        )

    LazyVerticalGrid(
        modifier = Modifier.padding(6.dp),
        columns = GridCells.Fixed(2)
    ) {
        items(pokedex) { pokemon ->
            PokemonItem(pokemon)
        }
    }
}

@Composable
private fun PokemonItem(pokemon: Pair<String, String>) {
    Card(
        modifier = Modifier.padding(6.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp),
        shape = RoundedCornerShape(14.dp)
    ) {
        Column(
            modifier = Modifier
                .padding(20.dp)
                .fillMaxWidth(),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            if (pokemon.second.isNotEmpty()) {
                AsyncImage(
                    model = pokemon.second,
                    contentDescription = pokemon.first
                )
            } else {
                Image(
                    modifier = Modifier.size(120.dp),
                    alignment = Alignment.Center,
                    painter = painterResource(id = R.drawable.images),
                    contentDescription = "Bulbasaur image"
                )
            }
            Text(
                text = pokemon.first,
                textAlign = TextAlign.Center
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
fun PokemonPreview() {
    PokedexComposeTheme {
        PokemonItem(
            Pair("Bulbasaur", "")
        )
    }
}
