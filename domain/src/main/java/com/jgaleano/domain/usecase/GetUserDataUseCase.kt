package com.jgaleano.domain.usecase

import com.jgaleano.domain.model.UserData
import com.jgaleano.domain.repository.UserDataRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetUserDataUseCase @Inject constructor(
    private val userDataRepository: UserDataRepository
) {
    operator fun invoke(): Flow<UserData> = userDataRepository.userData
}
