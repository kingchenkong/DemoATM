package com.kck.demoatm.use_cases

import android.util.Log
import com.kck.demoatm.application.ERROR_MSG_AMOUNT_INVALID
import com.kck.demoatm.application.ERROR_MSG_BALANCE_INVALID
import com.kck.demoatm.application.MyApplication
import com.kck.demoatm.application.SourceType
import com.kck.demoatm.entities.Account
import com.kck.demoatm.interface_adapters.repositories.IAccountRepository

class WithdrawUseCase {
    private val TAG: String = WithdrawUseCase::class.java.simpleName
    private val repository: IAccountRepository = MyApplication().repository

    suspend fun invoke(
        serialNumber: String,
        amount: Int
    ): Result<Account> {
        Log.d("WithdrawUseCase", "invoke: sn: $serialNumber, amount: $amount")

        // 1. Get account
        val account: Account = repository.getAccount(SourceType.LOCAL, serialNumber)
            .getOrElse { return Result.failure(it) }

        // 2. Validating Account
        validating(account.queryBalance(), amount)
            .onFailure { return Result.failure(it) }

        // 3. Account withdraw. (calculate balance - login's entity)
        account.modifyBalance(amount, Account.Companion.Action.SUB)

        // 4. generate AccountDB to update db.
        return repository.updateAccount(
            SourceType.LOCAL,
            serialNumber,
            account.queryBalance()
        )
    }

    private fun validating(
        balance: Int, amount: Int
    ): Result<Boolean> {
        // 1. Validating amount > 0
        validatingAmount(amount).onFailure { return Result.failure(it) }
        // 2. Validating source account balance
        validatingBalance(balance, amount).onFailure { return Result.failure(it) }

        return Result.success(true)
    }

    private fun validatingAmount(
        amount: Int
    ): Result<Boolean> =
        if (amount > 0) {
            Result.success(true)
        } else {
            Log.e(TAG, "validatingAmount: ERROR_MSG_AMOUNT_INVALID, amount: $amount")
            Result.failure(Throwable(ERROR_MSG_AMOUNT_INVALID))
        }

    private fun validatingBalance(
        balance: Int,
        amount: Int
    ): Result<Boolean> =
        if (balance > 0 && balance >= amount) {
            Result.success(true)
        } else {
            Log.e(
                TAG,
                "validatingBalance: ERROR_MSG_BALANCE_INVALID, balance: $balance, amount: $amount"
            )
            Result.failure(Throwable(ERROR_MSG_BALANCE_INVALID))
        }

}