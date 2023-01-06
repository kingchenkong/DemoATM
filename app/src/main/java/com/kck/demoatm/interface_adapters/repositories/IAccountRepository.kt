package com.kck.demoatm.interface_adapters.repositories

import com.kck.demoatm.SourceType
import com.kck.demoatm.entities.Account

interface IAccountRepository {
    suspend fun getAccount(sourceType: SourceType): Account
}