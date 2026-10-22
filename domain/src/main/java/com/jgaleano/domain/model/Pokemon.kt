package com.jgaleano.domain.model

data class Pokemon(
    var page: Int = 0,
    val name: String,
    val url: String
) {
    val id: Int
        get() = url.split("/".toRegex()).dropLast(1).last().toInt()

    fun getImageUrl(): String = officialArtworkUrl(id)
}

fun officialArtworkUrl(id: Int): String =
    "https://raw.githubusercontent.com/PokeAPI/sprites/master/sprites/" +
            "pokemon/other/official-artwork/$id.png"
