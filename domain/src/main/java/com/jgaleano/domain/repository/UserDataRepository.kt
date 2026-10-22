package com.jgaleano.domain.repository

import com.jgaleano.domain.model.UiTheme
import com.jgaleano.domain.model.UserData
import kotlinx.coroutines.flow.Flow

interface UserDataRepository {

    val userData: Flow<UserData>

    suspend fun setUiTheme(uiTheme: UiTheme)
}
