package com.jgaleano.presentation.ui.component

import androidx.compose.animation.animateColorAsState
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.luminance
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.jgaleano.presentation.ui.theme.CardFallback
import com.jgaleano.presentation.ui.theme.PokedexComposeTheme

@Composable
fun PokemonCard(
    name: String,
    imageUrl: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    imageModifier: Modifier = Modifier
) {
    var dominantColor by rememberSaveable(imageUrl) { mutableStateOf(CardFallback.toArgb()) }
    val backgroundColor by animateColorAsState(Color(dominantColor), label = "cardBackground")

    Card(
        modifier = modifier
            .padding(6.dp)
            .fillMaxWidth()
            .clickable(onClick = onClick),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp),
        shape = RoundedCornerShape(14.dp),
        colors = CardDefaults.cardColors(containerColor = backgroundColor)
    ) {
        Column(
            modifier = Modifier
                .padding(16.dp)
                .fillMaxWidth(),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            PokemonImage(
                modifier = imageModifier
                    .fillMaxWidth()
                    .aspectRatio(1f),
                imageUrl = imageUrl,
                contentDescription = name,
                onDominantColor = { dominantColor = it.toArgb() }
            )
            Text(
                text = name,
                textAlign = TextAlign.Center,
                color = if (backgroundColor.luminance() > 0.5f) Color.Black else Color.White,
                fontSize = 16.sp,
                fontWeight = FontWeight.Bold
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun PokemonCardPreview() {
    PokedexComposeTheme {
        PokemonCard(name = "Pikachu", imageUrl = "", onClick = {})
    }
}
