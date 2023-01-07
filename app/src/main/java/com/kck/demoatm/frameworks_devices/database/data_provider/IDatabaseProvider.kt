package com.kck.demoatm.frameworks_devices.database.data_provider

import com.kck.demoatm.frameworks_devices.database.models.AccountDB

interface IDatabaseProvider {
    suspend fun getAllAccount(): List<AccountDB>
    suspend fun getAccount(serialNumber: String, password: String): AccountDB?
    suspend fun insertAccount(accountDB: AccountDB)
    suspend fun updateAccount(serialNumber: String, password: String, balance: Int): Boolean
}