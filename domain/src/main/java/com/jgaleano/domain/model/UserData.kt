package com.jgaleano.domain.model

data class UserData(
    val uiTheme: UiTheme
)

enum class UiTheme {
    FOLLOW_SYSTEM,
    LIGHT,
    DARK
}
