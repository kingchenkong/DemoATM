package com.kck.demoatm.use_cases

import com.kck.demoatm.entities.Account

class CheckBalanceEnoughUseCase {
    fun invoke(
        account: Account,
        money: Int
    ): Boolean {
        return when {
            account.queryBalance() == 0 -> false
            account.queryBalance() < money -> false
            else -> true
        }
    }
}