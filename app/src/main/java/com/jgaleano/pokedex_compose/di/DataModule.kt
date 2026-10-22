package com.jgaleano.pokedex_compose.di

import com.jgaleano.data.datasource.PokemonLocalDataSource
import com.jgaleano.data.datasource.PokemonRemoteDataSource
import com.jgaleano.data.datasource.UserPreferencesDataSource
import com.jgaleano.data.repository.PokemonRepositoryImpl
import com.jgaleano.data.repository.UserDataRepositoryImpl
import com.jgaleano.domain.repository.PokemonRepository
import com.jgaleano.domain.repository.UserDataRepository
import com.jgaleano.framework.datasource.PokemonLocalDataSourceImpl
import com.jgaleano.framework.datasource.PokemonRemoteDataSourceImpl
import com.jgaleano.framework.preferences.UserPreferencesDataSourceImpl
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
interface DataModule {

    @Binds
    fun bindPokemonRemoteDataSource(impl: PokemonRemoteDataSourceImpl): PokemonRemoteDataSource

    @Binds
    fun bindPokemonLocalDataSource(impl: PokemonLocalDataSourceImpl): PokemonLocalDataSource

    @Binds
    fun bindUserPreferencesDataSource(impl: UserPreferencesDataSourceImpl): UserPreferencesDataSource

    @Singleton
    @Binds
    fun bindPokemonRepository(impl: PokemonRepositoryImpl): PokemonRepository

    @Singleton
    @Binds
    fun bindUserDataRepository(impl: UserDataRepositoryImpl): UserDataRepository
}
