package com.kck.demoatm.entities

import android.util.Log
import com.kck.demoatm.application.ACC_BALANCE_DEF
import com.kck.demoatm.application.ACC_PWD_DEF
import com.kck.demoatm.application.ACC_SN_DEF

class Account(
    val id: Int,
    val serialNumber: String,
    val password: String,
    private var balance: Int,
) {
    private val TAG: String = Account::class.java.simpleName

    companion object {
        enum class Action { SUB, ADD }

        val defaultAccount
            get() = Account(
                id = 0,
                serialNumber = ACC_SN_DEF,
                password = ACC_PWD_DEF,
                balance = ACC_BALANCE_DEF,
            )
    }

    fun modifyBalance(money: Int, action: Action) {
        Log.d(TAG, "calculateBalance: action: $action, money: $money, balance $balance")
        when (action) {
            Action.SUB -> balance -= money
            Action.ADD -> balance += money
        }
    }

    fun queryBalance(): Int {
        Log.d(TAG, "queryBalance: $balance")
        return balance
    }

    override fun toString(): String {
        return "Account (id=$id," +
                " serialNumber='$serialNumber'," +
                " password='$password'," +
                " balance=$balance," +
                ")"
    }

}

