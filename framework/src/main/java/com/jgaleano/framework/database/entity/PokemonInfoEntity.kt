package com.jgaleano.framework.database.entity

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.jgaleano.domain.model.PokemonInfo
import com.jgaleano.domain.model.PokemonStat

@Entity(tableName = "pokemon_info")
data class PokemonInfoEntity(
    @PrimaryKey val name: String,
    val id: Int,
    val height: Int,
    val weight: Int,
    val experience: Int,
    val types: List<String>,
    val stats: List<PokemonStat>
)

fun PokemonInfoEntity.asDomain() = PokemonInfo(
    id = id,
    name = name,
    height = height,
    weight = weight,
    experience = experience,
    types = types,
    stats = stats
)

fun PokemonInfo.asEntity() = PokemonInfoEntity(
    name = name,
    id = id,
    height = height,
    weight = weight,
    experience = experience,
    types = types,
    stats = stats
)
