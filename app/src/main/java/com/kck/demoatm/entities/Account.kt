package com.kck.demoatm.entities

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

