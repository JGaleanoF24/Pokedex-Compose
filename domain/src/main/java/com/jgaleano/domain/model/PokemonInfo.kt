package com.jgaleano.domain.model

import java.util.Locale

data class PokemonInfo(
    val id: Int,
    val name: String,
    val height: Int,
    val weight: Int,
    val experience: Int,
    val types: List<String>,
    val stats: List<PokemonStat>
) {
    fun getImageUrl(): String = officialArtworkUrl(id)

    fun getIdString(): String = String.format(Locale.US, "#%03d", id)

    /** The API returns the weight in hectograms. */
    fun getWeightString(): String = String.format(Locale.US, "%.1f KG", weight.toFloat() / 10)

    /** The API returns the height in decimeters. */
    fun getHeightString(): String = String.format(Locale.US, "%.1f M", height.toFloat() / 10)

    fun getStat(name: String): Int = stats.firstOrNull { it.name == name }?.baseStat ?: 0

    companion object {
        const val MAX_STAT = 300
        const val MAX_EXPERIENCE = 1000
    }
}

data class PokemonStat(
    val name: String,
    val baseStat: Int
)
