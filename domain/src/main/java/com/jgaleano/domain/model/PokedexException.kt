package com.jgaleano.domain.model

/**
 * Error emitted by the repositories when the data can't be loaded.
 */
class PokedexException(override val message: String?) : Exception(message)
