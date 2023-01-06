package com.kck.demoatm.entities

import com.kck.demoatm.ACCOUNT_BALANCE_DEF
import com.kck.demoatm.ACCOUNT_DISPLAY_NAME_DEF
import com.kck.demoatm.ACCOUNT_PASSWORD_DEF
import com.kck.demoatm.ACCOUNT_SERIAL_NUM_DEF

class Account(
    val id: Int,
    val serialNumber: String,
    val password: String,
    val balance: Int,
    // variable
    var displayName: String,
) {
    companion object {
        enum class Action { WITHDRAW, DEPOSIT }

        val defaultAccount = Account(
            id = 0,
            serialNumber = ACCOUNT_SERIAL_NUM_DEF,
            password = ACCOUNT_PASSWORD_DEF,
            balance = ACCOUNT_BALANCE_DEF,
            displayName = ACCOUNT_DISPLAY_NAME_DEF,
        )
    }

    fun changeNum(newName: String) {
        displayName = newName
    }

    fun login(password: String): Boolean {
        return this.password == password
    }

    private fun canWithdraw(money: Int): Boolean {
        return when {
            balance == 0 -> false
            balance < money -> false
            else -> true
        }
    }

    private fun calculateBalance(money: Int, action: Action) {
        when (action) {
            Action.WITHDRAW -> balance - money
            Action.DEPOSIT -> balance + money
        }
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
                " displayName='$displayName' " +
                ")"
    }

}

