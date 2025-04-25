package com.jgaleano.presentation.util

import androidx.compose.ui.graphics.Color
import androidx.palette.graphics.Palette

object ImageUtils {

    fun parseColorSwatch(color: Palette.Swatch?): Color {
        return color?.let {
            Color(it.rgb)
        } ?: Color.Black
    }
}
