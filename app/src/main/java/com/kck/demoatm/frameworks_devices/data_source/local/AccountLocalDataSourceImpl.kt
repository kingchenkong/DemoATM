package com.kck.demoatm.frameworks_devices.data_source.local

import android.util.Log
import com.kck.demoatm.application.ERROR_MSG_ACC_NOT_FOUND
import com.kck.demoatm.application.ERROR_MSG_LOGIN
import com.kck.demoatm.application.ERROR_MSG_UPDATE
import com.kck.demoatm.application.MyApplication
import com.kck.demoatm.entities.Account
import com.kck.demoatm.frameworks_devices.database.data_provider.IDatabaseProvider
import com.kck.demoatm.frameworks_devices.database.models.AccountDB
import com.kck.demoatm.interface_adapters.mappers.toDB
import com.kck.demoatm.interface_adapters.mappers.toEntity

class AccountLocalDataSourceImpl : IAccountLocalDataSource {
    private val TAG: String = AccountLocalDataSourceImpl::class.java.simpleName
    private val databaseProvider: IDatabaseProvider = MyApplication().databaseProvider

    override suspend fun getAllAccount(): List<Account> {
        databaseProvider.getAllAccount().let {
            val list: MutableList<Account> = mutableListOf()
            it.forEach { accountDB ->
                list.add(accountDB.toEntity())
            }
            return list
        }
    }

    override suspend fun getAccount(
        serialNumber: String
    ): Result<Account> {
        val account = databaseProvider.getAccount(serialNumber)
        return if (account == null) {
            Log.e(TAG, "getAccount: fail: ERROR_MSG_ACC_NOT_FOUND")
            Result.failure(Throwable(ERROR_MSG_ACC_NOT_FOUND))
        } else {
            Result.success(account.toEntity())
        }
    }

    override suspend fun login(
        serialNumber: String,
        password: String
    ): Result<Account> {
        val accountDB = databaseProvider.login(serialNumber, password)
        return if (accountDB == null) {
            Log.e(TAG, "login: fail: ERROR_MSG_LOGIN")
            Result.failure(Throwable(ERROR_MSG_LOGIN))
        } else {
            Result.success(accountDB.toEntity())
        }
    }

    override suspend fun insertAccount(account: Account) {
        val accountDB = account.toDB()
        databaseProvider.insertAccount(accountDB)
    }

    override suspend fun updateAccount(
        serialNumber: String,
        balance: Int
    ): Result<Account> {
        return when (
            val accountDB = databaseProvider.updateAccount(serialNumber, balance)
        ) {
            null -> {
                Log.e(TAG, "updateAccount: fail: ERROR_MSG_UPDATE")
                Result.failure(Throwable(ERROR_MSG_UPDATE))
            }
            else -> Result.success(accountDB.toEntity())
        }
    }

}