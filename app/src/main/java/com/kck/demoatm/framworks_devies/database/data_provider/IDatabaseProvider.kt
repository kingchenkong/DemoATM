package com.kck.demoatm.framworks_devies.database.data_provider

import com.kck.demoatm.framworks_devies.database.models.AccountDB

interface IDatabaseProvider {
    suspend fun initialize()

    suspend fun getAllAccount(): List<AccountDB>
    suspend fun getAccountList(serialNumber: String, password: String): List<AccountDB>
}