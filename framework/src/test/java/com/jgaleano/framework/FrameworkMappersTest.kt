package com.jgaleano.framework

import com.jgaleano.domain.model.PokemonStat
import com.jgaleano.domain.model.UiTheme
import com.jgaleano.framework.database.PokedexConverters
import com.jgaleano.framework.network.mapper.asDomain
import com.jgaleano.framework.network.model.PokemonInfoResponse
import com.jgaleano.framework.preferences.asUiTheme
import org.junit.Assert.assertEquals
import org.junit.Test

class FrameworkMappersTest {

    private val converters = PokedexConverters()

    @Test
    fun typesRoundTrip() {
        val types = listOf("grass", "poison")
        assertEquals(types, converters.toTypes(converters.fromTypes(types)))
        assertEquals(emptyList<String>(), converters.toTypes(converters.fromTypes(emptyList())))
    }

    @Test
    fun statsRoundTrip() {
        val stats = listOf(PokemonStat("hp", 45), PokemonStat("special-attack", 65))
        assertEquals(stats, converters.toStats(converters.fromStats(stats)))
    }

    @Test
    fun infoResponseMapsToDomain() {
        val response = PokemonInfoResponse(
            id = 1,
            name = "bulbasaur",
            height = 7,
            weight = 69,
            experience = null,
            types = listOf(
                PokemonInfoResponse.TypeResponse(2, PokemonInfoResponse.NamedResponse("poison")),
                PokemonInfoResponse.TypeResponse(1, PokemonInfoResponse.NamedResponse("grass"))
            ),
            stats = listOf(PokemonInfoResponse.StatResponse(45, PokemonInfoResponse.NamedResponse("hp")))
        )

        val info = response.asDomain()

        assertEquals(listOf("grass", "poison"), info.types)
        assertEquals(0, info.experience)
        assertEquals(45, info.getStat("hp"))
    }

    @Test
    fun unknownThemeFallsBackToFollowSystem() {
        assertEquals(UiTheme.DARK, "DARK".asUiTheme())
        assertEquals(UiTheme.FOLLOW_SYSTEM, "PURPLE".asUiTheme())
        assertEquals(UiTheme.FOLLOW_SYSTEM, null.asUiTheme())
    }
}
