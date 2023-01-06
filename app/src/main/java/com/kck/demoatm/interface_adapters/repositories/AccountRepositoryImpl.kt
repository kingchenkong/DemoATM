package com.kck.demoatm.interface_adapters.repositories

import com.kck.demoatm.SourceType
import com.kck.demoatm.entities.Account
import com.kck.demoatm.framworks_devies.data_source.local.IAccountLocalDataSource

class AccountRepositoryImpl(
    private val serialNumber: String,
    private val password: String,
    private val localDataSource: IAccountLocalDataSource,
) : IAccountRepository {

    override suspend fun getAccount(sourceType: SourceType): Account {
        return if (sourceType == SourceType.LOCAL) {
            val accountList: List<Account> = localDataSource.getAccount(serialNumber, password)
                .getOrDefault(listOf(Account.defaultAccount))
            accountList[0]
        } else {
            Account.defaultAccount
        }
    }
}