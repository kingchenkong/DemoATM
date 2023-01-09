package com.kck.demoatm.entities

import android.util.Log
import com.kck.demoatm.application.ACC_BALANCE_DEF
import com.kck.demoatm.application.ACC_PWD_DEF
import com.kck.demoatm.application.ACC_SN_DEF

class Account(
    val id: Int,
    val serialNumber: String,
    val password: String,
    // variable
    private var balance: Int,
) {
    companion object {
        enum class Action { WITHDRAW, DEPOSIT }

        val defaultAccount = Account(
            id = 0,
            serialNumber = ACC_SN_DEF,
            password = ACC_PWD_DEF,
            balance = ACC_BALANCE_DEF,
        )
    }

    fun login(password: String): Boolean {
        return this.password == password
    }


    private fun calculateBalance(money: Int, action: Action) {
        when (action) {
            Action.WITHDRAW -> balance -= money
            Action.DEPOSIT -> balance += money
        }
        Log.e("Account", "calculateBalance: money: $money, balance $balance")
    }

    fun queryBalance(): Int {
        return balance
    }

    fun canWithdraw(money: Int): Boolean {
        val bool = when {
            balance == 0 -> false
            balance < money -> false
            else -> true
        }
        Log.d("Account", "canWithdraw: $bool")
        return bool
    }

    fun withdraw(money: Int): Boolean {
        return when (canWithdraw(money)) {
            false -> false
            true -> {
                calculateBalance(money, Action.WITHDRAW)
                true
            }
        }
    }

    fun deposit(money: Int) {
        calculateBalance(money, Action.DEPOSIT)
    }

    override fun toString(): String {
        return "Account (id=$id," +
                " serialNumber='$serialNumber'," +
                " password='$password'," +
                " balance=$balance," +
                ")"
    }

}

