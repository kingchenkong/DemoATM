package com.kck.demoatm.use_cases

import android.util.Log
import com.kck.demoatm.application.MyApplication
import com.kck.demoatm.application.SourceType
import com.kck.demoatm.entities.Account
import com.kck.demoatm.interface_adapters.repositories.IAccountRepository

class DepositUseCase {
    private val repository: IAccountRepository = MyApplication().repository

    suspend fun invoke(
        serialNumber: String,
        amount: Int
    ): Result<Account> {
        Log.d("DepositUseCase", "invoke: sn: $serialNumber, amount: $amount")
        // 1. get account
        val account: Account =
            repository.getAccount(SourceType.LOCAL, serialNumber).getOrElse {
                return Result.failure(it)
            }
        // 2. deposit
        account.modifyBalance(amount, Account.Companion.Action.ADD)
        // 3. generate AccountDB to update db.
        return repository.updateAccount(
            SourceType.LOCAL,
            serialNumber,
            account.queryBalance()
        )
    }
}