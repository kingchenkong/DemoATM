package com.kck.demoatm.framworks_devies.data_source

import com.kck.demoatm.entities.Account

interface IAccountDataSource {
    suspend fun getAccount(
        serialNumber: String,
        password: String,
    ): Result<Account>

    suspend fun login(
        serialNumber: String,
        password: String,
    ): Result<Account>

    suspend fun updateAccount(
        serialNumber: String,
        password: String,
        account: Account
    ): Result<Boolean>
}