package com.jgaleano.framework.network.mapper

import com.jgaleano.domain.model.Pokemon
import com.jgaleano.domain.model.PokemonInfo
import com.jgaleano.domain.model.PokemonStat
import com.jgaleano.framework.network.model.PokemonInfoResponse
import com.jgaleano.framework.network.model.PokemonItemResponse

fun PokemonItemResponse.asDomain(page: Int): Pokemon = Pokemon(page = page, name = name, url = url)

fun PokemonInfoResponse.asDomain(): PokemonInfo = PokemonInfo(
    id = id,
    name = name,
    height = height,
    weight = weight,
    experience = experience ?: 0,
    types = types.sortedBy { it.slot }.map { it.type.name },
    stats = stats.map { PokemonStat(name = it.stat.name, baseStat = it.baseStat) }
)
