package com.kck.demoatm.use_cases

import android.util.Log
import com.kck.demoatm.application.*
import com.kck.demoatm.entities.Account
import com.kck.demoatm.interface_adapters.repositories.IAccountRepository

class TransferUseCase {
    private val TAG: String = TransferUseCase::class.java.simpleName
    private val repository: IAccountRepository = MyApplication().repository

    suspend fun invoke(
        sourceSN: String,
        destSN: String,
        amount: Int
    ): Result<Account> {
        Log.d("TransferUseCase", "invoke: sourceSN: $sourceSN, destSN: $destSN, amount: $amount")

        // 1. Get source account
        val sourceAccount: Account = repository.getAccount(SourceType.LOCAL, sourceSN)
            .getOrElse { return Result.failure(Throwable(it)) }

        // 2. Get destination account
        val destAccount: Account = repository.getAccount(SourceType.LOCAL, destSN)
            .getOrElse { return Result.failure(Throwable(it)) }

        // 3. Validating
        validating(sourceSN, sourceAccount.queryBalance(), destSN, amount)
            .onFailure { return Result.failure(it) }

        // 4. Withdraw Source Account
        withdrawSourceAccount(sourceAccount, amount)
            .onFailure { return Result.failure(it) }

        // 4. Deposit Destination Account
        return depositDestAccount(destAccount, amount)
    }

    private suspend fun withdrawSourceAccount(account: Account, amount: Int): Result<Account> {
        // 1. Withdraw.
        account.modifyBalance(amount, Account.Companion.Action.SUB)

        // 2. Generate AccountDB to update db.
        return repository.updateAccount(
            SourceType.LOCAL,
            account.serialNumber,
            account.queryBalance()
        )
    }

    private suspend fun depositDestAccount(account: Account, amount: Int): Result<Account> {
        // 1. Deposit
        account.modifyBalance(amount, Account.Companion.Action.ADD)

        // 2. Generate AccountDB to update db.
        return repository.updateAccount(
            SourceType.LOCAL,
            account.serialNumber,
            account.queryBalance()
        )
    }

    fun validating(
        sourceSN: String, sourceBalance: Int, destSN: String, amount: Int
    ): Result<Boolean> {
        // 1. Validating source, dest SN is duplicate?
        validatingDestAccount(sourceSN, destSN)
            .onFailure { return Result.failure(it) }

        // 2. Validating amount > 0
        validatingAmount(amount)
            .onFailure { return Result.failure(it) }

        // 3. Validating source account balance
        validatingBalance(sourceBalance, amount)
            .onFailure { return Result.failure(it) }
        return Result.success(true)
    }

    private fun validatingDestAccount(
        sourceSN: String,
        destinationSN: String
    ): Result<Boolean> =
        if (sourceSN != destinationSN) {
            Result.success(true)
        } else {
            Log.e(TAG, "validatingDestAccount: ERROR_MSG_SAME_ACC")
            Result.failure(Throwable(ERROR_MSG_SAME_ACC))
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