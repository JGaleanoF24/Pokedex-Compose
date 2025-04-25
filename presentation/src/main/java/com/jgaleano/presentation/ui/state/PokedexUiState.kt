package com.jgaleano.presentation.ui.state

sealed class PokedexUiState<out T: Any> {

    data object Loading: PokedexUiState<Nothing>()
    data class Error<T: Any>(val error: String): PokedexUiState<T>()
    data class Success<T: Any>(val data: T): PokedexUiState<T>()
    data class Empty<T: Any>(val message: String): PokedexUiState<T>()
}
