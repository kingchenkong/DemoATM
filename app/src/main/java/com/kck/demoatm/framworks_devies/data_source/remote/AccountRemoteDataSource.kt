package com.kck.demoatm.framworks_devies.data_source.remote

import com.kck.demoatm.entities.Account

class AccountRemoteDataSource() : IAccountRemoteDataSource {
    override suspend fun getAccount(
        serialNumber: String, password: String
    ): Result<List<Account>> = Result.success(listOf(Account.defaultAccount))
}