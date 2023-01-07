package com.kck.demoatm.interface_adapters.repositories

import com.kck.demoatm.ERROR_MSG_REMOTE_NOT_FOUND
import com.kck.demoatm.SourceType
import com.kck.demoatm.entities.Account
import com.kck.demoatm.framworks_devies.data_source.local.IAccountLocalDataSource
import org.koin.core.context.GlobalContext

class AccountRepositoryImpl : IAccountRepository {
    private val localDataSource: IAccountLocalDataSource by GlobalContext.get().inject()

    override suspend fun getAll(): List<Account> {
        return localDataSource.getAllAccount()
    }

    override suspend fun getAccount(
        sourceType: SourceType,
        serialNumber: String,
        password: String
    ): Account {
        return if (sourceType == SourceType.LOCAL) {
            localDataSource.getAccount(serialNumber, password)
                .getOrDefault(Account.defaultAccount)
        } else {
            Account.defaultAccount
        }
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

    override suspend fun updateAccount(
        sourceType: SourceType,
        serialNumber: String,
        password: String,
        balance: Int
    ): Boolean { // update accountDB by appoint account entity
        return if (sourceType == SourceType.LOCAL) {
            localDataSource.updateAccount(serialNumber, password, balance).getOrElse { false }
        } else {
            false
        }
    }
}