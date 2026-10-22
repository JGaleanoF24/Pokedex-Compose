package com.jgaleano.pokedex_compose.settings

import androidx.lifecycle.viewModelScope
import com.jgaleano.domain.usecase.GetUserDataUseCase
import com.jgaleano.domain.usecase.SetUiThemeUseCase
import com.jgaleano.presentation.mvi.MviViewModel
import com.jgaleano.presentation.ui.state.PokedexUiState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SettingsViewModel @Inject constructor(
    getUserDataUseCase: GetUserDataUseCase,
    private val setUiThemeUseCase: SetUiThemeUseCase
) : MviViewModel<SettingsUiState, SettingsIntent, SettingsEffect>(PokedexUiState.Loading) {

    init {
        getUserDataUseCase()
            .onEach { userData -> setState { PokedexUiState.Success(userData.uiTheme) } }
            .catch { error -> setState { PokedexUiState.Error(error.message ?: UNKNOWN_ERROR) } }
            .launchIn(viewModelScope)
    }

    override fun onIntent(intent: SettingsIntent) {
        when (intent) {
            is SettingsIntent.ChangeTheme -> viewModelScope.launch { setUiThemeUseCase(intent.uiTheme) }
            SettingsIntent.Dismiss -> sendEffect(SettingsEffect.Dismiss)
        }
    }

    companion object {
        const val UNKNOWN_ERROR = "Something went wrong"
    }
}
