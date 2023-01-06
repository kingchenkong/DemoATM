package com.kck.demoatm.interface_adapters.repositories

import com.kck.demoatm.SourceType
import com.kck.demoatm.entities.Account

interface IAccountRepository {
    suspend fun getAll(): List<Account>
    suspend fun getAccount(
        sourceType: SourceType,
        serialNumber: String,
        password: String
    ): Account

    suspend fun login(
        sourceType: SourceType,
        serialNumber: String,
        password: String
    ): Result<Account>
}