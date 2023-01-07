package com.kck.demoatm.framworks_devies.data_source.local

import android.util.Log
import com.kck.demoatm.entities.Account
import com.kck.demoatm.framworks_devies.database.data_provider.IDatabaseProvider
import com.kck.demoatm.interface_adapters.mappers.toEntity
import org.koin.core.context.GlobalContext

class AccountLocalDataSourceImpl : IAccountLocalDataSource {
    private val dataProvider: IDatabaseProvider by GlobalContext.get().inject()

    override suspend fun getAllAccount(): List<Account> {
        dataProvider.getAllAccount().let {
            val list: MutableList<Account> = mutableListOf()
            it.forEach { accountDB ->
                list.add(accountDB.toEntity())
            }
            return list
        }
    }

    override suspend fun getAccount(
        serialNumber: String,
        password: String
    ): Result<Account> = dataProvider.getAccount(serialNumber, password).let {
        Log.e("AccountLocalDataSourceImpl", "getAccount: accountDB: $it")
        Result.success(it.toEntity())
    }

    override suspend fun login(
        serialNumber: String,
        password: String
    ): Result<Account> =
        dataProvider.login(serialNumber, password).let {
            Log.e("AccountLocalDataSourceImpl", "login: accountDB: $it")
            Result.success(it.toEntity())
        }

    override suspend fun updateAccount(
        serialNumber: String,
        password: String,
        balance: Int
    ): Result<Boolean> = Result.success(dataProvider.updateAccount(serialNumber, password, balance))

}