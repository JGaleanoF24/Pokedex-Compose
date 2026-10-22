package com.jgaleano.domain.usecase

import com.jgaleano.domain.model.UiTheme
import com.jgaleano.domain.repository.UserDataRepository
import javax.inject.Inject

class SetUiThemeUseCase @Inject constructor(
    private val userDataRepository: UserDataRepository
) {
    suspend operator fun invoke(uiTheme: UiTheme) = userDataRepository.setUiTheme(uiTheme)
}
