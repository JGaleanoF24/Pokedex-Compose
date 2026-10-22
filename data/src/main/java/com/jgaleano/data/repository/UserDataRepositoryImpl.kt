package com.jgaleano.data.repository

import com.jgaleano.data.datasource.UserPreferencesDataSource
import com.jgaleano.domain.model.UiTheme
import com.jgaleano.domain.model.UserData
import com.jgaleano.domain.repository.UserDataRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class UserDataRepositoryImpl @Inject constructor(
    private val preferencesDataSource: UserPreferencesDataSource
) : UserDataRepository {

    override val userData: Flow<UserData> = preferencesDataSource.userData

    override suspend fun setUiTheme(uiTheme: UiTheme) = preferencesDataSource.setUiTheme(uiTheme)
}
