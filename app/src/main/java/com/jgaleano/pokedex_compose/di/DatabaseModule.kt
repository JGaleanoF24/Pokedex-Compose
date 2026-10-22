package com.jgaleano.pokedex_compose.di

import android.content.Context
import androidx.room.Room
import com.jgaleano.framework.database.PokedexDatabase
import com.jgaleano.framework.database.PokemonDao
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DatabaseModule {

    @Singleton
    @Provides
    fun provideDatabase(@ApplicationContext context: Context): PokedexDatabase =
        Room.databaseBuilder(context, PokedexDatabase::class.java, PokedexDatabase.NAME)
            .fallbackToDestructiveMigration(dropAllTables = true)
            .build()

    @Provides
    fun providePokemonDao(database: PokedexDatabase): PokemonDao = database.pokemonDao()
}
