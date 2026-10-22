package com.jgaleano.presentation.ui.component

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.jgaleano.presentation.ui.theme.pokemonTypeColor

@Composable
fun PokemonTypeChip(
    type: String,
    modifier: Modifier = Modifier
) {
    Text(
        modifier = modifier
            .background(color = pokemonTypeColor(type), shape = RoundedCornerShape(64.dp))
            .padding(horizontal = 32.dp, vertical = 6.dp),
        text = type,
        color = Color.White,
        fontSize = 16.sp,
        fontWeight = FontWeight.Bold
    )
}

@Preview
@Composable
private fun PokemonTypeChipPreview() {
    PokemonTypeChip(type = "grass")
}
