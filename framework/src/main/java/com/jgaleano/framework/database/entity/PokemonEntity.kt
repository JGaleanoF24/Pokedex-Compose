package com.jgaleano.framework.database.entity

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.jgaleano.domain.model.Pokemon

@Entity(tableName = "pokemon")
data class PokemonEntity(
    val page: Int,
    @PrimaryKey val name: String,
    val url: String
)

fun PokemonEntity.asDomain() = Pokemon(page = page, name = name, url = url)

fun Pokemon.asEntity() = PokemonEntity(page = page, name = name, url = url)
