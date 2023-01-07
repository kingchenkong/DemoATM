package com.kck.demoatm.frameworks_devices.data_source

import com.kck.demoatm.entities.Account

interface IAccountDataSource {
    suspend fun getAllAccount(): List<Account>

    suspend fun login(
        serialNumber: String,
        password: String,
    ): Result<Account>

    suspend fun insertAccount(
        account: Account
    )

    suspend fun updateAccount(
        serialNumber: String,
        password: String,
        balance: Int
    ): Boolean
}