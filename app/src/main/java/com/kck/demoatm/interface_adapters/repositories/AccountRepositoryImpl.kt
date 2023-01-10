package com.kck.demoatm.interface_adapters.repositories

import com.kck.demoatm.application.ERROR_MSG_REMOTE_NOT_FOUND
import com.kck.demoatm.application.MyApplication
import com.kck.demoatm.application.SourceType
import com.kck.demoatm.entities.Account
import com.kck.demoatm.frameworks_devices.data_source.local.IAccountLocalDataSource

class AccountRepositoryImpl : IAccountRepository {
    private val localDataSource: IAccountLocalDataSource = MyApplication().localDataSource

    override suspend fun getAll(): List<Account> {
        return localDataSource.getAllAccount()
    }

    override suspend fun login(
        sourceType: SourceType,
        serialNumber: String,
        password: String
    ): Result<Account> {
        return if (sourceType == SourceType.LOCAL) {
            localDataSource.login(serialNumber, password)
        } else {
            Result.failure(Throwable(ERROR_MSG_REMOTE_NOT_FOUND))
        }
    }

    override suspend fun getAccount(sourceType: SourceType, serialNumber: String): Result<Account> {
        return if (sourceType == SourceType.LOCAL) {
            localDataSource.getAccount(serialNumber)
        } else {
            Result.failure(Throwable(ERROR_MSG_REMOTE_NOT_FOUND))
        }
    }


    override suspend fun updateAccount(
        sourceType: SourceType,
        serialNumber: String,
        balance: Int
    ): Result<Account> { // update accountDB by appoint account entity
        return if (sourceType == SourceType.LOCAL) {
            localDataSource.updateAccount(serialNumber, balance)
        } else {
            Result.failure(Throwable(ERROR_MSG_REMOTE_NOT_FOUND))
        }
    }
}