package com.jgaleano.framework.database

import androidx.room.TypeConverter
import com.jgaleano.domain.model.PokemonStat

/**
 * Stores the small lists of [PokemonInfoEntity] as plain text:
 * types as `grass,poison` and stats as `hp:45,attack:49`.
 */
class PokedexConverters {

    @TypeConverter
    fun fromTypes(types: List<String>): String = types.joinToString(SEPARATOR)

    @TypeConverter
    fun toTypes(value: String): List<String> =
        if (value.isEmpty()) emptyList() else value.split(SEPARATOR)

    @TypeConverter
    fun fromStats(stats: List<PokemonStat>): String =
        stats.joinToString(SEPARATOR) { "${it.name}$PAIR_SEPARATOR${it.baseStat}" }

    @TypeConverter
    fun toStats(value: String): List<PokemonStat> =
        if (value.isEmpty()) {
            emptyList()
        } else {
            value.split(SEPARATOR).map { pair ->
                val (name, baseStat) = pair.split(PAIR_SEPARATOR)
                PokemonStat(name = name, baseStat = baseStat.toInt())
            }
        }

    private companion object {
        const val SEPARATOR = ","
        const val PAIR_SEPARATOR = ":"
    }
}
