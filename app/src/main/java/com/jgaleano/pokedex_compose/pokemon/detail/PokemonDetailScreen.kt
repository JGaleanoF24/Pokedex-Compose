package com.jgaleano.pokedex_compose.pokemon.detail

import androidx.compose.animation.AnimatedVisibilityScope
import androidx.compose.animation.ExperimentalSharedTransitionApi
import androidx.compose.animation.SharedTransitionScope
import androidx.compose.animation.animateColorAsState
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.luminance
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.jgaleano.domain.model.PokemonInfo
import com.jgaleano.pokedex_compose.R
import com.jgaleano.pokedex_compose.navigation.pokemonImageKey
import com.jgaleano.presentation.ui.component.PokedexAppBar
import com.jgaleano.presentation.ui.component.PokedexLoading
import com.jgaleano.presentation.ui.component.PokedexMessage
import com.jgaleano.presentation.ui.component.PokemonImage
import com.jgaleano.presentation.ui.component.PokemonStatBar
import com.jgaleano.presentation.ui.component.PokemonTypeChip
import com.jgaleano.presentation.ui.state.PokedexUiState
import com.jgaleano.presentation.ui.theme.CardFallback

@OptIn(ExperimentalSharedTransitionApi::class)
@Composable
fun SharedTransitionScope.PokemonDetailScreen(
    animatedVisibilityScope: AnimatedVisibilityScope,
    pokemonName: String,
    imageUrl: String,
    onNavigateBack: () -> Unit,
    viewModel: PokemonDetailViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    LaunchedEffect(viewModel) {
        viewModel.effect.collect { effect ->
            when (effect) {
                PokemonDetailEffect.NavigateBack -> onNavigateBack()
            }
        }
    }

    var dominantColor by rememberSaveable(imageUrl) { mutableIntStateOf(CardFallback.toArgb()) }
    val headerColor by animateColorAsState(Color(dominantColor), label = "headerColor")
    val headerContentColor = if (headerColor.luminance() > 0.5f) Color.Black else Color.White
    val displayName = pokemonName.replaceFirstChar { it.uppercase() }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
            .verticalScroll(rememberScrollState())
            .testTag("PokemonDetail")
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .background(headerColor, RoundedCornerShape(bottomStart = 48.dp, bottomEnd = 48.dp))
                .padding(bottom = 24.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            PokedexAppBar(
                title = displayName,
                containerColor = Color.Transparent,
                contentColor = headerContentColor,
                onBackClick = { viewModel.onIntent(PokemonDetailIntent.BackClicked) },
                actions = {
                    val info = (uiState as? PokedexUiState.Success)?.data
                    if (info != null) {
                        Text(
                            modifier = Modifier.padding(end = 16.dp),
                            text = info.getIdString(),
                            color = headerContentColor,
                            fontSize = 18.sp,
                            fontWeight = FontWeight.Bold
                        )
                    }
                }
            )
            PokemonImage(
                modifier = Modifier
                    .size(220.dp)
                    .sharedElement(
                        state = rememberSharedContentState(key = pokemonImageKey(pokemonName)),
                        animatedVisibilityScope = animatedVisibilityScope
                    ),
                imageUrl = imageUrl,
                contentDescription = displayName,
                onDominantColor = { dominantColor = it.toArgb() }
            )
        }

        when (val state = uiState) {
            PokedexUiState.Loading -> PokedexLoading(Modifier.height(240.dp))
            is PokedexUiState.Error -> PokedexMessage(
                modifier = Modifier.height(240.dp),
                message = state.error,
                onRetry = { viewModel.onIntent(PokemonDetailIntent.Retry) }
            )
            is PokedexUiState.Empty -> PokedexMessage(
                modifier = Modifier.height(240.dp),
                message = state.message
            )
            is PokedexUiState.Success -> PokemonInfoContent(state.data)
        }
    }
}

@Composable
private fun PokemonInfoContent(pokemonInfo: PokemonInfo) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 24.dp, vertical = 24.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(24.dp)
    ) {
        Text(
            text = pokemonInfo.name.replaceFirstChar { it.uppercase() },
            color = MaterialTheme.colorScheme.onBackground,
            fontSize = 36.sp,
            fontWeight = FontWeight.Bold
        )

        Row(horizontalArrangement = Arrangement.spacedBy(16.dp)) {
            pokemonInfo.types.forEach { type -> PokemonTypeChip(type = type) }
        }

        Row(modifier = Modifier.fillMaxWidth()) {
            PokemonMeasure(
                modifier = Modifier.weight(1f),
                value = pokemonInfo.getWeightString(),
                label = stringResource(R.string.detail_weight)
            )
            PokemonMeasure(
                modifier = Modifier.weight(1f),
                value = pokemonInfo.getHeightString(),
                label = stringResource(R.string.detail_height)
            )
        }

        Text(
            text = stringResource(R.string.detail_base_stats),
            color = MaterialTheme.colorScheme.onBackground,
            fontSize = 21.sp,
            fontWeight = FontWeight.Bold
        )

        Column(modifier = Modifier.fillMaxWidth()) {
            PokemonStatBar(
                label = stringResource(R.string.detail_hp),
                value = pokemonInfo.getStat("hp"),
                maxValue = PokemonInfo.MAX_STAT,
                color = Color(0xFFD53A47)
            )
            PokemonStatBar(
                label = stringResource(R.string.detail_attack),
                value = pokemonInfo.getStat("attack"),
                maxValue = PokemonInfo.MAX_STAT,
                color = Color(0xFFFFA726)
            )
            PokemonStatBar(
                label = stringResource(R.string.detail_defense),
                value = pokemonInfo.getStat("defense"),
                maxValue = PokemonInfo.MAX_STAT,
                color = Color(0xFF0091EA)
            )
            PokemonStatBar(
                label = stringResource(R.string.detail_speed),
                value = pokemonInfo.getStat("speed"),
                maxValue = PokemonInfo.MAX_STAT,
                color = Color(0xFF90B1C5)
            )
            PokemonStatBar(
                label = stringResource(R.string.detail_experience),
                value = pokemonInfo.experience,
                maxValue = PokemonInfo.MAX_EXPERIENCE,
                color = Color(0xFF388E3C)
            )
        }
    }
}

@Composable
private fun PokemonMeasure(value: String, label: String, modifier: Modifier = Modifier) {
    Column(modifier = modifier, horizontalAlignment = Alignment.CenterHorizontally) {
        Text(
            text = value,
            color = MaterialTheme.colorScheme.onBackground,
            fontSize = 21.sp,
            fontWeight = FontWeight.Bold
        )
        Box(modifier = Modifier.height(4.dp))
        Text(
            text = label,
            color = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.6f),
            fontSize = 12.sp
        )
    }
}
