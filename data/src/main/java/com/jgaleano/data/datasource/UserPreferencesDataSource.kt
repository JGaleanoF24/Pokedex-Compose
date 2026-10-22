package com.jgaleano.data.datasource

import com.jgaleano.domain.model.UiTheme
import com.jgaleano.domain.model.UserData
import kotlinx.coroutines.flow.Flow

interface UserPreferencesDataSource {

    val userData: Flow<UserData>

    suspend fun setUiTheme(uiTheme: UiTheme)
}
