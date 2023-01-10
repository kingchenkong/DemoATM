package com.kck.demoatm.use_cases

import com.kck.demoatm.application.ERROR_MSG_BALANCE_NOT_ENOUGH
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
        val canWithdraw: Boolean = checkBalanceEnoughUseCase.invoke(account.queryBalance(), money)
        if (!canWithdraw) {
            return Result.failure(Throwable(ERROR_MSG_BALANCE_NOT_ENOUGH))
        }
        account.modifyBalance(money, Account.Companion.Action.SUB)

        // 4. generate AccountDB to update db.
        return repository.updateAccount(
            SourceType.LOCAL,
            serialNumber,
            account.queryBalance()
        )
        // 5. success - get money to present Ui.
    }
}