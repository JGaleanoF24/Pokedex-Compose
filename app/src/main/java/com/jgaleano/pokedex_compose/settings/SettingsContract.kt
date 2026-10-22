package com.jgaleano.pokedex_compose.settings

import com.jgaleano.domain.model.UiTheme
import com.jgaleano.presentation.mvi.UiEffect
import com.jgaleano.presentation.mvi.UiIntent
import com.jgaleano.presentation.ui.state.PokedexUiState

typealias SettingsUiState = PokedexUiState<UiTheme>

sealed interface SettingsIntent : UiIntent {
    data class ChangeTheme(val uiTheme: UiTheme) : SettingsIntent

    data object Dismiss : SettingsIntent
}

sealed interface SettingsEffect : UiEffect {
    data object Dismiss : SettingsEffect
}
