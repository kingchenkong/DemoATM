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
//        password: String,
        money: Int
    ): Result<Account> {
        // 1. login (get entity)
        val account: Account =
            repository.getAccount(SourceType.LOCAL, serialNumber).getOrElse {
                return Result.failure(it)
            }
        // 2. deposit
        account.modifyBalance(money, Account.Companion.Action.ADD)
        // 3. generate AccountDB to update db.
        val updateSuccess =
            repository.updateAccount(
                SourceType.LOCAL,
                serialNumber,
                account.queryBalance()
            )
        return if (updateSuccess) {
            Result.success(account)
        } else {
            Result.failure(Throwable(ERROR_MSG_UPDATE))
        }
        // 4. success - get money to present Ui.

    }
}