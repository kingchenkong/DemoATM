package com.kck.demoatm.use_cases

import com.kck.demoatm.SourceType
import com.kck.demoatm.entities.Account
import com.kck.demoatm.interface_adapters.repositories.IAccountRepository
import org.koin.core.context.GlobalContext

class LoginUseCase(
) {
    private val repository: IAccountRepository by GlobalContext.get().inject()

    suspend fun login(
        serialNumber: String,
        password: String
    ): Result<Account> {
        return repository.login(SourceType.LOCAL, serialNumber, password)
    }
}