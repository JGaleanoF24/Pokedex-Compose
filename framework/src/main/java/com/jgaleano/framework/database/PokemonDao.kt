package com.jgaleano.framework.database

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.jgaleano.framework.database.entity.PokemonEntity
import com.jgaleano.framework.database.entity.PokemonInfoEntity

@Dao
interface PokemonDao {

    @Query("SELECT * FROM pokemon WHERE page = :page")
    suspend fun getPokemonList(page: Int): List<PokemonEntity>

    @Query("SELECT * FROM pokemon WHERE page <= :page ORDER BY page")
    suspend fun getAllPokemonList(page: Int): List<PokemonEntity>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertPokemonList(pokemonList: List<PokemonEntity>)

    @Query("SELECT * FROM pokemon_info WHERE name = :name")
    suspend fun getPokemonInfo(name: String): PokemonInfoEntity?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertPokemonInfo(pokemonInfo: PokemonInfoEntity)
}
