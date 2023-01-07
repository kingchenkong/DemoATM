package com.kck.demoatm.use_cases

import android.util.Log
import com.kck.demoatm.ERROR_MSG_BALANCE
import com.kck.demoatm.ERROR_MSG_LOGIN
import com.kck.demoatm.ERROR_MSG_UPDATE
import com.kck.demoatm.SourceType
import com.kck.demoatm.entities.Account
import com.kck.demoatm.interface_adapters.repositories.IAccountRepository
import org.koin.core.context.GlobalContext

class WithdrawUseCase {
    private val repository: IAccountRepository by GlobalContext.get().inject()

    suspend fun invoke(
        serialNumber: String,
        password: String,
        money: Int
    ): Result<Int> {
        // 1. login (get entity)
        val account: Account =
            repository.login(SourceType.LOCAL, serialNumber, password).getOrElse {
                return Result.failure(Throwable(ERROR_MSG_LOGIN))
            }

        // 2. Account can withdraw? (check login's entity)
        val canWithdraw: Boolean = account.withdraw(money)
        if (!canWithdraw) {
            return Result.failure(Throwable(ERROR_MSG_BALANCE))
        }

        // 3. Account withdraw. (calculate balance - login's entity)

        // 4. generate AccountDB to update db.
        val updateSuccess =
            repository.updateAccount(
                SourceType.LOCAL,
                serialNumber,
                password,
                account.queryBalance()
            )
        return if (updateSuccess) {
            Result.success(money)
        } else {
            Result.failure(Throwable(ERROR_MSG_UPDATE))
        }
        // 5. success - get money to present Ui.
    }
}