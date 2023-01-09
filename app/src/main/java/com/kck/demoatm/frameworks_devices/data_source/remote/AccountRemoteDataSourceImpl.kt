package com.kck.demoatm.frameworks_devices.data_source.remote

import android.util.Log
import com.kck.demoatm.application.ERROR_MSG_REMOTE_NOT_FOUND
import com.kck.demoatm.entities.Account
import com.kck.demoatm.frameworks_devices.database.models.AccountDB
import com.kck.demoatm.interface_adapters.mappers.toEntity

class AccountRemoteDataSourceImpl : IAccountRemoteDataSource {
    override suspend fun getAllAccount():
            List<Account> = listOf(AccountDB.defaultAccountDB.toEntity())

    override suspend fun getAccount(serialNumber: String): Result<Account> =
        Result.failure(Throwable(ERROR_MSG_REMOTE_NOT_FOUND))

    override suspend fun login(
        serialNumber: String, password: String
    ): Result<Account> = Result.failure(Throwable(ERROR_MSG_REMOTE_NOT_FOUND))

    override suspend fun insertAccount(account: Account) {
        Log.e("AccountRemoteDataSourceImpl", "insertAccount: do nothing")
    }

    override suspend fun updateAccount(
        serialNumber: String, balance: Int
    ): Boolean = false
}