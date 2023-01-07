package com.kck.demoatm.use_cases

import com.kck.demoatm.application.MyApplication
import com.kck.demoatm.application.SourceType
import com.kck.demoatm.entities.Account
import com.kck.demoatm.interface_adapters.repositories.IAccountRepository

class LoginUseCase {
    private val repository: IAccountRepository = MyApplication().repository

    suspend fun invoke(
        serialNumber: String,
        password: String
    ): Result<Account> {
        val account = repository.login(SourceType.LOCAL, serialNumber, password).getOrElse {
            return Result.failure(it)
        }
        return Result.success(account)
    }
}