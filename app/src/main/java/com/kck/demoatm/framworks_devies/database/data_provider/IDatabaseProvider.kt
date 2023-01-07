package com.kck.demoatm.framworks_devies.database.data_provider

import com.kck.demoatm.framworks_devies.database.models.AccountDB

interface IDatabaseProvider {
    suspend fun initialize()

    suspend fun getAllAccount(): List<AccountDB>
    suspend fun getAccount(serialNumber: String, password: String): AccountDB
    suspend fun login(serialNumber: String, password: String): AccountDB
    suspend fun updateAccount(serialNumber: String, password: String, balance: Int): Boolean

}