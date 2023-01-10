package com.kck.demoatm.use_cases

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
        money: Int
    ): Result<Account> {
        // check source, dest SN is duplicate?
//        if (sourceSN == destSN) {
//            return Result.failure(Throwable(ERROR_MSG_SAME_ACC))
//        }

        // ===== Source =====
        val sourceResult = sourcePart(sourceSN, money)
        if (sourceResult.isFailure) {
            return sourceResult
        }

        // ===== Destination =====
        return destinationPart(destSN, money)

        //  success - get money to present Ui.
    }

    private suspend fun sourcePart(sn: String, money: Int): Result<Account> {
        // 1. login source Account
        val account: Account =
            repository.getAccount(SourceType.LOCAL, sn).getOrElse {
                return Result.failure(Throwable(ERROR_MSG_ACC_NOT_FOUND))
            }
        // 2. Account can withdraw? (check login's entity)
        // 3. Account withdraw. (calculate balance - login's entity)
//        val canWithdraw: Boolean = checkBalanceEnoughUseCase.invoke(account.queryBalance(), money)
//        if (!canWithdraw) {
//            return Result.failure(Throwable(ERROR_MSG_BALANCE))
//        }
        account.modifyBalance(money, Account.Companion.Action.SUB)

        // 4. generate AccountDB to update db.
        return repository.updateAccount(
            SourceType.LOCAL,
            sn,
            account.queryBalance()
        )
    }

    private suspend fun destinationPart(sn: String, money: Int): Result<Account> {
        // 1. login destination Account
        val account: Account =
            repository.getAccount(SourceType.LOCAL, sn).getOrElse {
                return Result.failure(Throwable(ERROR_MSG_ACC_NOT_FOUND))
            }
        // 2. deposit
        account.modifyBalance(money, Account.Companion.Action.ADD)
        // 3. generate AccountDB to update db.
        return repository.updateAccount(
            SourceType.LOCAL,
            sn,
            account.queryBalance()
        )
    }
}