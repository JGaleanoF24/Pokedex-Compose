package com.jgaleano.framework.preferences

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import com.jgaleano.data.datasource.UserPreferencesDataSource
import com.jgaleano.domain.model.UiTheme
import com.jgaleano.domain.model.UserData
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class UserPreferencesDataSourceImpl @Inject constructor(
    private val dataStore: DataStore<Preferences>
) : UserPreferencesDataSource {

    override val userData: Flow<UserData> = dataStore.data.map { preferences ->
        UserData(uiTheme = preferences[KEY_UI_THEME].asUiTheme())
    }

    override suspend fun setUiTheme(uiTheme: UiTheme) {
        dataStore.edit { preferences -> preferences[KEY_UI_THEME] = uiTheme.name }
    }

    companion object {
        const val FILE_NAME = "user_preferences"
        private val KEY_UI_THEME = stringPreferencesKey("ui_theme")
    }
}

/** Missing or unknown values fall back to [UiTheme.FOLLOW_SYSTEM]. */
internal fun String?.asUiTheme(): UiTheme =
    UiTheme.entries.firstOrNull { it.name == this } ?: UiTheme.FOLLOW_SYSTEM
