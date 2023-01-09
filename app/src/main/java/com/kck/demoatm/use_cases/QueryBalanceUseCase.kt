package com.kck.demoatm.use_cases

import com.kck.demoatm.application.MyApplication
import com.kck.demoatm.application.SourceType
import com.kck.demoatm.interface_adapters.repositories.IAccountRepository

class QueryBalanceUseCase {
    private val repository: IAccountRepository = MyApplication().repository

    suspend fun invoke(
        serialNumber: String
    ): Result<Int> {
        val account = repository.getAccount(SourceType.LOCAL, serialNumber).getOrElse {
            return Result.failure(it)
        }
        return Result.success(account.queryBalance())
    }

}