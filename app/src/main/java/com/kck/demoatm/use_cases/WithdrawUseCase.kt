package com.kck.demoatm.use_cases

import com.kck.demoatm.application.ERROR_MSG_BALANCE
import com.kck.demoatm.application.ERROR_MSG_UPDATE
import com.kck.demoatm.application.MyApplication
import com.kck.demoatm.application.SourceType
import com.kck.demoatm.entities.Account
import com.kck.demoatm.interface_adapters.repositories.IAccountRepository

class WithdrawUseCase {
    private val repository: IAccountRepository = MyApplication().repository
    private val checkBalanceEnoughUseCase: CheckBalanceEnoughUseCase =
        MyApplication().checkBalanceEnoughUseCase

    suspend fun invoke(
        serialNumber: String,
        money: Int
    ): Result<Account> {
        // 1. login (get entity)
        val account: Account =
            repository.getAccount(SourceType.LOCAL, serialNumber).getOrElse {
                return Result.failure(it)
            }

        // 2. Account can withdraw? (check login's entity)
        // 3. Account withdraw. (calculate balance - login's entity)
        val canWithdraw: Boolean = checkBalanceEnoughUseCase.invoke(account, money)
        if (!canWithdraw) {
            return Result.failure(Throwable(ERROR_MSG_BALANCE))
        }
        account.modifyBalance(money, Account.Companion.Action.SUB)

        // 4. generate AccountDB to update db.
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
        // 5. success - get money to present Ui.
    }
}