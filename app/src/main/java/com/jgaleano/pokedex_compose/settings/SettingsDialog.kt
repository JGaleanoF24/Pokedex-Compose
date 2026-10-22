package com.jgaleano.pokedex_compose.settings

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.selection.selectable
import androidx.compose.foundation.selection.selectableGroup
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.RadioButton
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.jgaleano.domain.model.UiTheme
import com.jgaleano.pokedex_compose.R
import com.jgaleano.presentation.ui.component.PokedexLoading
import com.jgaleano.presentation.ui.component.PokedexMessage
import com.jgaleano.presentation.ui.state.PokedexUiState

@Composable
fun SettingsDialog(
    onDismiss: () -> Unit,
    viewModel: SettingsViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    LaunchedEffect(viewModel) {
        viewModel.effect.collect { effect ->
            when (effect) {
                SettingsEffect.Dismiss -> onDismiss()
            }
        }
    }

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .background(MaterialTheme.colorScheme.surface, RoundedCornerShape(28.dp))
            .padding(24.dp)
            .testTag("SettingsDialog"),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        Text(
            text = stringResource(R.string.settings),
            color = MaterialTheme.colorScheme.onSurface,
            fontSize = 22.sp
        )
        HorizontalDivider()
        Text(
            text = stringResource(R.string.settings_theme),
            color = MaterialTheme.colorScheme.onSurface,
            fontSize = 16.sp,
            fontWeight = FontWeight.Medium
        )

        when (val state = uiState) {
            PokedexUiState.Loading -> PokedexLoading(Modifier.height(120.dp))
            is PokedexUiState.Error -> PokedexMessage(modifier = Modifier.height(120.dp), message = state.error)
            is PokedexUiState.Empty -> PokedexMessage(modifier = Modifier.height(120.dp), message = state.message)
            is PokedexUiState.Success -> ThemeOptions(
                selected = state.data,
                onSelect = { viewModel.onIntent(SettingsIntent.ChangeTheme(it)) }
            )
        }

        HorizontalDivider()
        TextButton(
            modifier = Modifier.align(Alignment.End),
            onClick = { viewModel.onIntent(SettingsIntent.Dismiss) }
        ) {
            Text(text = stringResource(R.string.settings_ok), color = MaterialTheme.colorScheme.primary)
        }
    }
}

@Composable
private fun ThemeOptions(selected: UiTheme, onSelect: (UiTheme) -> Unit) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .selectableGroup(),
        verticalArrangement = Arrangement.spacedBy(4.dp)
    ) {
        UiTheme.entries.forEach { uiTheme ->
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .selectable(
                        selected = uiTheme == selected,
                        role = Role.RadioButton,
                        onClick = { onSelect(uiTheme) }
                    )
                    .padding(vertical = 8.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                RadioButton(selected = uiTheme == selected, onClick = null)
                Text(
                    text = stringResource(uiTheme.labelRes),
                    color = MaterialTheme.colorScheme.onSurface
                )
            }
        }
    }
}

private val UiTheme.labelRes: Int
    get() = when (this) {
        UiTheme.FOLLOW_SYSTEM -> R.string.settings_theme_follow_system
        UiTheme.LIGHT -> R.string.settings_theme_light
        UiTheme.DARK -> R.string.settings_theme_dark
    }
