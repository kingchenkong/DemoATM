package com.kck.demoatm.framworks_devies.data_source.local

import android.util.Log
import com.kck.demoatm.entities.Account
import com.kck.demoatm.framworks_devies.database.data_provider.IDatabaseProvider
import com.kck.demoatm.interface_adapters.mappers.toEntity

class AccountLocalDataSourceImpl(
    private val dataProvider: IDatabaseProvider
) : IAccountLocalDataSource {

    override suspend fun getAccount(
        serialNumber: String, password: String
    ): Result<List<Account>> = dataProvider.getAccountList(serialNumber, password).let {
        Result.success(it.map { accountDB ->
            Log.e("AccountLocalDataSourceImpl", "getAccount: accountDB: $accountDB")
            accountDB.toEntity()
        })
    }
}