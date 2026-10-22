package com.jgaleano.pokedex_compose.pokemon.list

import android.widget.Toast
import androidx.compose.animation.AnimatedVisibilityScope
import androidx.compose.animation.ExperimentalSharedTransitionApi
import androidx.compose.animation.SharedTransitionScope
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.GridItemSpan
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.itemsIndexed
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.jgaleano.domain.model.Pokemon
import com.jgaleano.pokedex_compose.R
import com.jgaleano.pokedex_compose.navigation.pokemonImageKey
import com.jgaleano.presentation.ui.component.PokedexAppBar
import com.jgaleano.presentation.ui.component.PokedexLoading
import com.jgaleano.presentation.ui.component.PokedexMessage
import com.jgaleano.presentation.ui.component.PokemonCard
import com.jgaleano.presentation.ui.state.PokedexUiState

private const val LOAD_MORE_THRESHOLD = 8

@OptIn(ExperimentalSharedTransitionApi::class)
@Composable
fun SharedTransitionScope.PokemonListScreen(
    animatedVisibilityScope: AnimatedVisibilityScope,
    onNavigateToDetail: (Pokemon) -> Unit,
    onNavigateToSettings: () -> Unit,
    viewModel: PokemonListViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    val context = LocalContext.current

    LaunchedEffect(viewModel) {
        viewModel.effect.collect { effect ->
            when (effect) {
                is PokemonListEffect.ShowError -> Toast.makeText(
                    context,
                    effect.message ?: context.getString(R.string.error_unknown),
                    Toast.LENGTH_SHORT
                ).show()
                is PokemonListEffect.NavigateToDetail -> onNavigateToDetail(effect.pokemon)
                PokemonListEffect.NavigateToSettings -> onNavigateToSettings()
            }
        }
    }

    Column(modifier = Modifier.fillMaxSize()) {
        PokedexAppBar(
            title = stringResource(R.string.pokemon_list_title),
            actions = {
                IconButton(onClick = { viewModel.onIntent(PokemonListIntent.SettingsClicked) }) {
                    Icon(
                        imageVector = Icons.Default.Settings,
                        contentDescription = stringResource(R.string.settings),
                        tint = Color.White
                    )
                }
            }
        )

        when (val state = uiState) {
            PokedexUiState.Loading -> PokedexLoading()
            is PokedexUiState.Error -> PokedexMessage(
                message = state.error,
                onRetry = { viewModel.onIntent(PokemonListIntent.Retry) }
            )
            is PokedexUiState.Empty -> PokedexMessage(message = stringResource(R.string.pokemon_list_empty))
            is PokedexUiState.Success -> PokemonGrid(
                animatedVisibilityScope = animatedVisibilityScope,
                data = state.data,
                onIntent = viewModel::onIntent
            )
        }
    }
}

@OptIn(ExperimentalSharedTransitionApi::class)
@Composable
private fun SharedTransitionScope.PokemonGrid(
    animatedVisibilityScope: AnimatedVisibilityScope,
    data: PokemonListData,
    onIntent: (PokemonListIntent) -> Unit
) {
    val pokemonList = data.pokemonList

    LazyVerticalGrid(
        modifier = Modifier
            .fillMaxSize()
            .testTag("PokemonList"),
        columns = GridCells.Fixed(2),
        contentPadding = PaddingValues(6.dp)
    ) {
        itemsIndexed(items = pokemonList, key = { _, pokemon -> pokemon.name }) { index, pokemon ->
            if (index + LOAD_MORE_THRESHOLD >= pokemonList.size) {
                LaunchedEffect(pokemonList.size) { onIntent(PokemonListIntent.LoadNextPage) }
            }

            PokemonCard(
                name = pokemon.name.replaceFirstChar { it.uppercase() },
                imageUrl = pokemon.getImageUrl(),
                onClick = { onIntent(PokemonListIntent.PokemonClicked(pokemon)) },
                imageModifier = Modifier.sharedElement(
                    state = rememberSharedContentState(key = pokemonImageKey(pokemon.name)),
                    animatedVisibilityScope = animatedVisibilityScope
                )
            )
        }

        if (data.isLoadingMore) {
            item(span = { GridItemSpan(maxLineSpan) }) {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp),
                    contentAlignment = Alignment.Center
                ) {
                    CircularProgressIndicator(color = MaterialTheme.colorScheme.primary)
                }
            }
        }
    }
}
