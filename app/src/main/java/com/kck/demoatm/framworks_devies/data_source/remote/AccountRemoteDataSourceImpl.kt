package com.kck.demoatm.framworks_devies.data_source.remote

import com.kck.demoatm.ERROR_MSG_REMOTE_NOT_FOUND
import com.kck.demoatm.entities.Account

class AccountRemoteDataSourceImpl : IAccountRemoteDataSource {
    override suspend fun getAccount(
        serialNumber: String, password: String
    ): Result<Account> = Result.failure(Throwable(ERROR_MSG_REMOTE_NOT_FOUND))

    override suspend fun login(
        serialNumber: String, password: String
    ): Result<Account> = Result.failure(Throwable(ERROR_MSG_REMOTE_NOT_FOUND))

    override suspend fun updateAccount(
        serialNumber: String,
        password: String,
        balance: Int
    ): Result<Boolean> = Result.failure(Throwable(ERROR_MSG_REMOTE_NOT_FOUND))
}