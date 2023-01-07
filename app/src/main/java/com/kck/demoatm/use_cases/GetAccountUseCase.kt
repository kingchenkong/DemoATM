package com.kck.demoatm.use_cases

import com.kck.demoatm.application.MyApplication
import com.kck.demoatm.application.SourceType
import com.kck.demoatm.entities.Account
import com.kck.demoatm.interface_adapters.repositories.IAccountRepository

class GetAccountUseCase {
    private val repository: IAccountRepository = MyApplication().repository

    suspend fun invoke(sourceType: SourceType): List<Account> {
        return listOf()
    }

}