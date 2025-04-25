package com.jgaleano.pokedex_compose

import android.content.Context
import android.graphics.Bitmap
import android.graphics.drawable.BitmapDrawable
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Surface
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.lifecycle.lifecycleScope
import androidx.palette.graphics.Palette
import coil.ImageLoader
import coil.request.ErrorResult
import coil.request.ImageRequest
import coil.request.SuccessResult
import com.jgaleano.pokedex_compose.pokemon.ui.PokemonList
import com.jgaleano.pokedex_compose.pokemon.ui.PokemonListViewModel
import com.jgaleano.presentation.theme.PokedexComposeTheme
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.cancel
import kotlinx.coroutines.launch

@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    private val pokedex = listOf(
        Pair(
            "Bulbasaur",
            "https://raw.githubusercontent.com/PokeAPI/sprites/master/sprites/pokemon/other/official-artwork/1.png"
        ),
        Pair(
            "Ivysaur",
            "https://raw.githubusercontent.com/PokeAPI/sprites/master/sprites/pokemon/other/official-artwork/2.png"
        ),
        Pair(
            "Venusaur",
            "https://raw.githubusercontent.com/PokeAPI/sprites/master/sprites/pokemon/other/official-artwork/3.png"
        ),
        Pair(
            "Charmander",
            "https://raw.githubusercontent.com/PokeAPI/sprites/master/sprites/pokemon/other/official-artwork/4.png"
        ),
        Pair(
            "Charmeleon",
            "https://raw.githubusercontent.com/PokeAPI/sprites/master/sprites/pokemon/other/official-artwork/5.png"
        ),
        Pair(
            "Charizard",
            "https://raw.githubusercontent.com/PokeAPI/sprites/master/sprites/pokemon/other/official-artwork/6.png"
        )
    )

    private val viewModel: PokemonListViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        //enableEdgeToEdge()
        super.onCreate(savedInstanceState)
        handelViewModel()

        setContent {
            PokedexComposeTheme {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = Color.Black
                ) {
                    PokemonList()
                }
            }
        }
    }

    private fun handelViewModel() {
        lifecycleScope.launch {
            pokedex.forEach { item ->
                urlToBitmap(
                    scope = this,
                    context = this@MainActivity,
                    viewModel = viewModel,
                    item = item
                )
            }
        }
    }

    private fun urlToBitmap(
        scope: CoroutineScope,
        context: Context,
        viewModel: PokemonListViewModel,
        item: Pair<String, String>
    ) {
        var bitmap: Bitmap? = null
        val loadBitmap = scope.launch(Dispatchers.IO) {
            val loader = ImageLoader(context)
            val request = ImageRequest.Builder(context)
                .data(item.second)
                .allowHardware(false)
                .build()
            val result = loader.execute(request)
            if (result is SuccessResult) {
                bitmap = (result.drawable as BitmapDrawable).bitmap
            } else if (result is ErrorResult) {
                cancel(result.throwable.localizedMessage ?: "ErrorResult", result.throwable)
            }
        }
        loadBitmap.invokeOnCompletion { throwable ->
            bitmap?.let {
                val background = getBackgroundFromImage(it)
                viewModel.updateBackground(item, background)
            } ?: throwable?.let {
                viewModel.updateBackground(item, Color.Black)
            } ?: viewModel.updateBackground(item, Color.Black)
        }
    }

    private fun urlToBitmap2(
        scope: CoroutineScope,
        context: Context,
        viewModel: PokemonListViewModel,
        item: Pair<String, String>
    ) {
        scope.launch(Dispatchers.IO) {
            val loader = ImageLoader(context)
            val request = ImageRequest.Builder(context)
                .data(item.second)
                .allowHardware(false) // Disable hardware bitmaps.
                .build()

            val result = (loader.execute(request) as SuccessResult).drawable
            val bitmap = (result as BitmapDrawable).bitmap
            val background = getBackgroundFromImage(bitmap)
            viewModel.updateBackground(item, background)
        }
    }

    private fun getBackgroundFromImage(bitmap: Bitmap): Color {
        val palette = Palette.from(bitmap).generate()
        val dominantSwatch = palette.dominantSwatch
        return dominantSwatch?.let {
            Color(it.rgb)
        } ?: Color.Black
    }
}
