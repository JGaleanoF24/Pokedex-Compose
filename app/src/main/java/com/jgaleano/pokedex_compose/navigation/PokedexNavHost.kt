package com.jgaleano.pokedex_compose.navigation

import androidx.compose.animation.ExperimentalSharedTransitionApi
import androidx.compose.animation.SharedTransitionLayout
import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.dialog
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.jgaleano.domain.model.officialArtworkUrl
import com.jgaleano.pokedex_compose.pokemon.detail.PokemonDetailScreen
import com.jgaleano.pokedex_compose.pokemon.list.PokemonListScreen
import com.jgaleano.pokedex_compose.settings.SettingsDialog

@OptIn(ExperimentalSharedTransitionApi::class)
@Composable
fun PokedexNavHost(navController: NavHostController = rememberNavController()) {
    SharedTransitionLayout {
        NavHost(navController = navController, startDestination = PokedexRoute.List.route) {
            composable(PokedexRoute.List.route) {
                this@SharedTransitionLayout.PokemonListScreen(
                    animatedVisibilityScope = this,
                    onNavigateToDetail = { pokemon ->
                        navController.navigate(PokedexRoute.Detail.create(pokemon))
                    },
                    onNavigateToSettings = { navController.navigate(PokedexRoute.Settings.route) }
                )
            }

            composable(
                route = PokedexRoute.Detail.route,
                arguments = listOf(
                    navArgument(PokedexRoute.Detail.ARG_NAME) { type = NavType.StringType },
                    navArgument(PokedexRoute.Detail.ARG_ID) { type = NavType.IntType }
                )
            ) { backStackEntry ->
                val arguments = requireNotNull(backStackEntry.arguments)
                this@SharedTransitionLayout.PokemonDetailScreen(
                    animatedVisibilityScope = this,
                    pokemonName = requireNotNull(arguments.getString(PokedexRoute.Detail.ARG_NAME)),
                    imageUrl = officialArtworkUrl(arguments.getInt(PokedexRoute.Detail.ARG_ID)),
                    onNavigateBack = { navController.popBackStack() }
                )
            }

            dialog(PokedexRoute.Settings.route) {
                SettingsDialog(onDismiss = { navController.popBackStack() })
            }
        }
    }
}
