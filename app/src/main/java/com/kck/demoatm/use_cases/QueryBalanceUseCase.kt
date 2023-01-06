package com.kck.demoatm.use_cases

import com.kck.demoatm.SourceType
import com.kck.demoatm.entities.Account
import com.kck.demoatm.interface_adapters.repositories.IAccountRepository
import org.koin.core.context.GlobalContext

class QueryBalanceUseCase {
    private val repository: IAccountRepository by GlobalContext.get().inject()

    suspend fun invoke(
        serialNumber: String,
        password: String
    ): Int {
        val account = repository.login(SourceType.LOCAL, serialNumber, password)
            .getOrElse { Account.defaultAccount }
        return account.balance
    }
}