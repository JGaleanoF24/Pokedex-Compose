package com.jgaleano.pokedex_compose

import com.jgaleano.domain.model.UiTheme
import com.jgaleano.domain.usecase.GetUserDataUseCase
import com.jgaleano.domain.usecase.SetUiThemeUseCase
import com.jgaleano.pokedex_compose.settings.SettingsIntent
import com.jgaleano.pokedex_compose.settings.SettingsViewModel
import com.jgaleano.presentation.ui.state.PokedexUiState
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Rule
import org.junit.Test

class SettingsViewModelTest {

    @get:Rule
    val mainDispatcherRule = MainDispatcherRule()

    @Test
    fun changeThemeIntentUpdatesState() {
        val repository = FakeUserDataRepository(UiTheme.LIGHT)
        val viewModel = SettingsViewModel(GetUserDataUseCase(repository), SetUiThemeUseCase(repository))
        assertEquals(PokedexUiState.Success(UiTheme.LIGHT), viewModel.uiState.value)

        viewModel.onIntent(SettingsIntent.ChangeTheme(UiTheme.DARK))

        assertEquals(PokedexUiState.Success(UiTheme.DARK), viewModel.uiState.value)
    }

    @Test
    fun darkThemeFollowsTheStoredPreference() {
        val viewModel = MainActivityViewModel(GetUserDataUseCase(FakeUserDataRepository(UiTheme.DARK)))

        assertFalse(viewModel.uiState.value.shouldKeepSplashScreen())
        assertTrue(viewModel.uiState.value.shouldUseDarkTheme(isSystemDarkTheme = false))
    }
}
