package com.jgaleano.framework.database

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.jgaleano.framework.database.entity.PokemonEntity
import com.jgaleano.framework.database.entity.PokemonInfoEntity

@Database(
    entities = [PokemonEntity::class, PokemonInfoEntity::class],
    version = 1,
    exportSchema = false
)
@TypeConverters(PokedexConverters::class)
abstract class PokedexDatabase : RoomDatabase() {

    abstract fun pokemonDao(): PokemonDao

    companion object {
        const val NAME = "pokedex.db"
    }
}
