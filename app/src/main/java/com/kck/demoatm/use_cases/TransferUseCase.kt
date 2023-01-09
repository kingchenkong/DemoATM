package com.kck.demoatm.use_cases

import com.kck.demoatm.application.*
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
        if (sourceSN == destSN) {
            return Result.failure(Throwable(ERROR_MSG_SAME_ACC))
        }

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
        val canWithdraw: Boolean = checkBalanceEnoughUseCase.invoke(account, money)
        if (!canWithdraw) {
            return Result.failure(Throwable(ERROR_MSG_BALANCE))
        }
        account.modifyBalance(money, Account.Companion.Action.SUB)

        // 4. generate AccountDB to update db.
        val sourceUpdateSuccess =
            repository.updateAccount(
                SourceType.LOCAL,
                sn,
                account.queryBalance()
            )
        return if (sourceUpdateSuccess) {
            Result.success(account)
        } else {
            Result.failure(Throwable(ERROR_MSG_UPDATE))
        }
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
        val destUpdateSuccess =
            repository.updateAccount(
                SourceType.LOCAL,
                sn,
                account.queryBalance()
            )
        return if (destUpdateSuccess) {
            Result.success(account)
        } else {
            Result.failure(Throwable(ERROR_MSG_UPDATE))
        }
    }
}