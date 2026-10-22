package com.jgaleano.presentation.ui.theme

import androidx.compose.ui.graphics.Color

fun pokemonTypeColor(type: String): Color = when (type) {
    "bug" -> Color(0xFF92BC2C)
    "dark" -> Color(0xFF595761)
    "dragon" -> Color(0xFF0C69C8)
    "electric" -> Color(0xFFF2D94E)
    "fairy" -> Color(0xFFEE90E6)
    "fire" -> Color(0xFFFBA54C)
    "fighting" -> Color(0xFFD3425F)
    "flying" -> Color(0xFFA1BBEC)
    "ghost" -> Color(0xFF5F6DBC)
    "grass" -> Color(0xFF5FBD58)
    "ground" -> Color(0xFFDA7C4D)
    "ice" -> Color(0xFF75D0C1)
    "poison" -> Color(0xFFB763CF)
    "psychic" -> Color(0xFFFA8581)
    "rock" -> Color(0xFFC9BB8A)
    "steel" -> Color(0xFF5695A3)
    "water" -> Color(0xFF539DDF)
    else -> Color(0xFFA0A29F)
}
