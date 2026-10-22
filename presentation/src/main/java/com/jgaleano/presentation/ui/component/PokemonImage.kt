package com.jgaleano.presentation.ui.component

import android.graphics.drawable.BitmapDrawable
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.palette.graphics.Palette
import coil.compose.AsyncImage
import coil.request.ImageRequest

/**
 * Loads a Pokemon artwork and reports its dominant color, used to tint the backgrounds.
 */
@Composable
fun PokemonImage(
    imageUrl: String,
    contentDescription: String,
    modifier: Modifier = Modifier,
    onDominantColor: (Color) -> Unit = {}
) {
    AsyncImage(
        modifier = modifier,
        model = ImageRequest.Builder(LocalContext.current)
            .data(imageUrl)
            // Palette needs to read the pixels, which hardware bitmaps don't allow
            .allowHardware(false)
            .build(),
        contentDescription = contentDescription,
        contentScale = ContentScale.Fit,
        onSuccess = { state ->
            val bitmap = (state.result.drawable as? BitmapDrawable)?.bitmap ?: return@AsyncImage
            Palette.from(bitmap).generate { palette ->
                palette?.dominantSwatch?.let { onDominantColor(Color(it.rgb)) }
            }
        }
    )
}
