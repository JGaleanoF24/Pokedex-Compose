package com.jgaleano.framework.network.model

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class PokemonInfoResponse(
    @Json(name = "id") val id: Int,
    @Json(name = "name") val name: String,
    @Json(name = "height") val height: Int,
    @Json(name = "weight") val weight: Int,
    @Json(name = "base_experience") val experience: Int? = null,
    @Json(name = "types") val types: List<TypeResponse>,
    @Json(name = "stats") val stats: List<StatResponse>,
) {
    @JsonClass(generateAdapter = true)
    data class TypeResponse(
        @Json(name = "slot") val slot: Int,
        @Json(name = "type") val type: NamedResponse,
    )

    @JsonClass(generateAdapter = true)
    data class StatResponse(
        @Json(name = "base_stat") val baseStat: Int,
        @Json(name = "stat") val stat: NamedResponse,
    )

    @JsonClass(generateAdapter = true)
    data class NamedResponse(
        @Json(name = "name") val name: String,
    )
}
