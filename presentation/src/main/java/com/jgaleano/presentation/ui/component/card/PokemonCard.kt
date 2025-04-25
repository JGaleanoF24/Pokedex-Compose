package com.jgaleano.presentation.ui.component.card

import android.graphics.drawable.BitmapDrawable
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.palette.graphics.Palette
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.jgaleano.presentation.R
import com.jgaleano.presentation.util.ImageUtils

@Composable
fun PokemonCard(
    image: String = "",
    name: String,
    backgroundColor: Color = Color.Black
) {
    var dynamicBackgroundColor by remember { mutableStateOf(backgroundColor) }

    Card(
        modifier = Modifier.padding(6.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp),
        shape = RoundedCornerShape(14.dp),
        colors = CardDefaults.cardColors().copy(
            containerColor = dynamicBackgroundColor
        )
    ) {
        Column(
            modifier = Modifier
                .padding(20.dp)
                .fillMaxWidth(),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            if (image.isNotEmpty()) {
                AsyncImage(
                    model = ImageRequest.Builder(LocalContext.current)
                        .data(image)
                        .crossfade(true)
                        .allowHardware(false)
                        .build(),
                    placeholder = painterResource(id = R.drawable.placeholder),
                    contentDescription = name,
                    onSuccess = { /* Do something on success */
                        val bitmap = (it.result.drawable as BitmapDrawable).bitmap
                        dynamicBackgroundColor = ImageUtils.parseColorSwatch(
                            Palette.from(bitmap).generate().dominantSwatch
                        )
                    }
                )
            } else {
                Image(
                    modifier = Modifier.size(120.dp),
                    alignment = Alignment.Center,
                    painter = painterResource(id = R.drawable.placeholder),
                    contentDescription = "Bulbasaur image"
                )
            }
            Text(
                text = name,
                textAlign = TextAlign.Center,
                color = Color.White
            )
        }
    }
}


@Preview(showBackground = true)
@Composable
private fun PokemonCardPlaceholderPreview() {
    PokemonCard(
        image = "",
        name = "Pikachu"
    )
}
