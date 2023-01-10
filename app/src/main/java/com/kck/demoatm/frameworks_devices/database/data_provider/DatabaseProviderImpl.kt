package com.kck.demoatm.frameworks_devices.database.data_provider

import android.content.Context
import android.util.Log
import com.kck.demoatm.frameworks_devices.database.dao.DemoDatabase
import com.kck.demoatm.frameworks_devices.database.models.AccountDB

class DatabaseProviderImpl(context: Context) : IDatabaseProvider {
    private val TAG: String = DatabaseProviderImpl::class.java.simpleName

    private val database: DemoDatabase by lazy { DemoDatabase.invoke(context) }

    override suspend fun getAllAccount(): List<AccountDB> = database.accountDao().getAll()

    override suspend fun getAccount(
        serialNumber: String
    ): AccountDB? =
        database.accountDao().getAccount(
            serialNumber = serialNumber
        ).let {
            Log.d(TAG, "login: $it")
            it
        }

    override suspend fun login(
        serialNumber: String,
        password: String
    ): AccountDB? =
        database.accountDao().login(
            serialNumber = serialNumber,
            password = password
        ).let {
            Log.d(TAG, "login: $it")
            it
        }

    override suspend fun insertAccount(accountDB: AccountDB) {
        Log.d(TAG, "insertAccount:")
        database.accountDao().addAccount(accountDB)
    }

    override suspend fun updateAccount(
        serialNumber: String,
        balance: Int
    ): AccountDB? {
        if (balance < 0) {
            Log.e(TAG, "updateAccount: Error: account.balance < 0")
            return null
        }
        return when (
            val getAccount = database.accountDao().getAccount(serialNumber)
        ) {
            null -> null
            else -> {
                getAccount.balance = balance
                database.accountDao().updateAccount(getAccount)
                Log.d(TAG, "updateAccount: [After Update] AccountDB: $getAccount")

                val reGetAccountAfterUpdate = database.accountDao().getAccount(serialNumber)
                Log.d(TAG, "updateAccount: [After Update] re-getAccount: $reGetAccountAfterUpdate")
                reGetAccountAfterUpdate
            }
        }
    }


}