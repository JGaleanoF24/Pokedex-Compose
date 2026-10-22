package com.jgaleano.domain.usecase

import com.jgaleano.domain.model.PokemonPage
import com.jgaleano.domain.repository.PokemonRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetPokemonListUseCase @Inject constructor(
    private val pokemonRepository: PokemonRepository
) {
    operator fun invoke(page: Int): Flow<PokemonPage> {
        require(page >= 0) { "page($page) can't be negative" }
        return pokemonRepository.getPokemonList(page)
    }
}
