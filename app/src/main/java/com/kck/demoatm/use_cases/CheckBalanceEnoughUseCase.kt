package com.kck.demoatm.use_cases

class CheckBalanceEnoughUseCase {

    fun invoke(
        balance: Int,
        money: Int
    ): Boolean = (balance > 0 && balance >= money)

}