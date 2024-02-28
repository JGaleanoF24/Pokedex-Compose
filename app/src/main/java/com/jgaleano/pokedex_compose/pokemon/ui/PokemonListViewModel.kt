package com.jgaleano.pokedex_compose.pokemon.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers.IO
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class PokemonListViewModel @Inject constructor(): ViewModel() {

    init {

    }

    private fun getPokemonList() {
        viewModelScope.launch(IO) {

        }
    }
}