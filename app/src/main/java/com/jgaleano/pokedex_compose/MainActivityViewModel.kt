package com.jgaleano.pokedex_compose

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.jgaleano.domain.model.UiTheme
import com.jgaleano.domain.model.UserData
import com.jgaleano.domain.usecase.GetUserDataUseCase
import com.jgaleano.presentation.ui.state.PokedexUiState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import javax.inject.Inject

@HiltViewModel
class MainActivityViewModel @Inject constructor(
    getUserDataUseCase: GetUserDataUseCase
) : ViewModel() {

    // started eagerly so the splash screen is released as soon as the preferences are read
    val uiState: StateFlow<PokedexUiState<UserData>> = getUserDataUseCase()
        .map<UserData, PokedexUiState<UserData>> { PokedexUiState.Success(it) }
        .stateIn(viewModelScope, SharingStarted.Eagerly, PokedexUiState.Loading)
}

fun PokedexUiState<UserData>.shouldKeepSplashScreen(): Boolean = this is PokedexUiState.Loading

fun PokedexUiState<UserData>.shouldUseDarkTheme(isSystemDarkTheme: Boolean): Boolean =
    when ((this as? PokedexUiState.Success)?.data?.uiTheme) {
        UiTheme.LIGHT -> false
        UiTheme.DARK -> true
        UiTheme.FOLLOW_SYSTEM, null -> isSystemDarkTheme
    }
