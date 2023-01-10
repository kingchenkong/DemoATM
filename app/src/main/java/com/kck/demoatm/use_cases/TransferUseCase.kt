package com.kck.demoatm.use_cases

import android.util.Log
import com.kck.demoatm.application.ERROR_MSG_ACC_NOT_FOUND
import com.kck.demoatm.application.MyApplication
import com.kck.demoatm.application.SourceType
import com.kck.demoatm.entities.Account
import com.kck.demoatm.interface_adapters.repositories.IAccountRepository

class TransferUseCase {
    private val repository: IAccountRepository = MyApplication().repository
    private val checkBalanceEnoughUseCase: CheckBalanceEnoughUseCase =
        MyApplication().checkBalanceEnoughUseCase

    suspend fun invoke(
        sourceSN: String,
        destSN: String,
        amount: Int
    ): Result<Account> {
        Log.d("TransferUseCase", "invoke: sourceSN: $sourceSN, destSN: $destSN, amount: $amount")

        // 1. check source, dest SN is duplicate?
        // 2. check amount is ok~
        // 3. ===== Source =====
        val sourceResult = sourcePart(sourceSN, amount)
        if (sourceResult.isFailure) {
            return sourceResult
        }

        // 4. ===== Destination =====
        return destinationPart(destSN, amount)
    }

    private suspend fun sourcePart(sn: String, amount: Int): Result<Account> {
        // 1. login source Account
        val account: Account =
            repository.getAccount(SourceType.LOCAL, sn).getOrElse {
                return Result.failure(Throwable(ERROR_MSG_ACC_NOT_FOUND))
            }
        // 2. Account can withdraw? (check login's entity)
        // 3. Account withdraw. (calculate balance - login's entity)
        account.modifyBalance(amount, Account.Companion.Action.SUB)

        // 4. generate AccountDB to update db.
        return repository.updateAccount(
            SourceType.LOCAL,
            sn,
            account.queryBalance()
        )
    }

    private suspend fun destinationPart(sn: String, amount: Int): Result<Account> {
        // 1. login destination Account
        val account: Account =
            repository.getAccount(SourceType.LOCAL, sn).getOrElse {
                return Result.failure(Throwable(ERROR_MSG_ACC_NOT_FOUND))
            }
        // 2. deposit
        account.modifyBalance(amount, Account.Companion.Action.ADD)
        // 3. generate AccountDB to update db.
        return repository.updateAccount(
            SourceType.LOCAL,
            sn,
            account.queryBalance()
        )
    }
}