package com.jgaleano.domain.usecase

import com.jgaleano.domain.model.PokemonInfo
import com.jgaleano.domain.repository.PokemonRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetPokemonInfoUseCase @Inject constructor(
    private val pokemonRepository: PokemonRepository
) {
    operator fun invoke(name: String): Flow<PokemonInfo> =
        pokemonRepository.getPokemonInfo(name.lowercase())
}
