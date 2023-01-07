package com.kck.demoatm.use_cases

import com.kck.demoatm.application.ERROR_MSG_UPDATE
import com.kck.demoatm.application.MyApplication
import com.kck.demoatm.application.SourceType
import com.kck.demoatm.entities.Account
import com.kck.demoatm.interface_adapters.repositories.IAccountRepository

class DepositUseCase {
    private val repository: IAccountRepository = MyApplication().repository

    suspend fun invoke(
        serialNumber: String,
        password: String,
        money: Int
    ): Result<Int> {
        // 1. login (get entity)
        val account: Account =
            repository.login(SourceType.LOCAL, serialNumber, password).getOrElse {
                return Result.failure(it)
            }
        // 2. deposit
        account.deposit(money)
        // 3. generate AccountDB to update db.
        val updateSuccess =
            repository.updateAccount(
                SourceType.LOCAL,
                serialNumber,
                password,
                account.queryBalance()
            )
        return if (updateSuccess) {
            Result.success(money)
        } else {
            Result.failure(Throwable(ERROR_MSG_UPDATE))
        }
        // 4. success - get money to present Ui.

    }
}