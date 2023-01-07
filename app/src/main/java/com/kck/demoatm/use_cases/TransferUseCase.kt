package com.kck.demoatm.use_cases

import com.kck.demoatm.application.ERROR_MSG_BALANCE
import com.kck.demoatm.application.ERROR_MSG_UPDATE
import com.kck.demoatm.application.MyApplication
import com.kck.demoatm.application.SourceType
import com.kck.demoatm.entities.Account
import com.kck.demoatm.interface_adapters.repositories.IAccountRepository

class TransferUseCase {
    private val repository: IAccountRepository = MyApplication().repository

    suspend fun invoke(
        sourceSN: String, sourcePWD: String,
        destSN: String, destPWD: String,
        money: Int
    ): Result<Int> {
        // ===== Source =====
        val sourceResult = sourcePart(sourceSN, sourcePWD, money)
        if (sourceResult.isFailure) {
            return sourceResult
        }
        // ===== Destination =====
        return destinationPart(destSN, destPWD, money)

        //  success - get money to present Ui.
    }

    private suspend fun sourcePart(sn: String, pwd: String, money: Int): Result<Int> {
        // 1. login source Account
        val sourceAcc: Account =
            repository.login(SourceType.LOCAL, sn, pwd).getOrElse {
                return Result.failure(it)
            }
        // 2. Account can withdraw? (check login's entity)
        // 3. Account withdraw. (calculate balance - login's entity)
        val canWithdraw: Boolean = sourceAcc.withdraw(money)
        if (!canWithdraw) {
            return Result.failure(Throwable(ERROR_MSG_BALANCE))
        }
        // 4. generate AccountDB to update db.
        val sourceUpdateSuccess =
            repository.updateAccount(
                SourceType.LOCAL,
                sn,
                pwd,
                sourceAcc.queryBalance()
            )
        return if (sourceUpdateSuccess) {
            Result.success(money)
        } else {
            Result.failure(Throwable(ERROR_MSG_UPDATE))
        }
    }

    private suspend fun destinationPart(sn: String, pwd: String, money: Int): Result<Int> {
        // 1. login destination Account
        val destAcc: Account =
            repository.login(SourceType.LOCAL, sn, pwd).getOrElse {
                return Result.failure(it)
            }
        // 2. deposit
        destAcc.deposit(money)
        // 3. generate AccountDB to update db.
        val destUpdateSuccess =
            repository.updateAccount(
                SourceType.LOCAL,
                sn,
                pwd,
                destAcc.queryBalance()
            )
        return if (destUpdateSuccess) {
            Result.success(money)
        } else {
            Result.failure(Throwable(ERROR_MSG_UPDATE))
        }
    }
}