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
        serialNumber: String,
        password: String
    ): AccountDB? =
        database.accountDao().getAccount(
            serialNumber = serialNumber,
            password = password
        ).let {
            Log.e(TAG, "login: $it")
            it
        }

    override suspend fun insertAccount(accountDB: AccountDB) {
        Log.e(TAG, "insertAccount:")
        database.accountDao().addAccount(accountDB)
    }

    override suspend fun updateAccount(
        serialNumber: String,
        password: String,
        balance: Int
    ): Boolean {
        if (balance <= 0) {
            Log.e(TAG, "updateAccount: Error: account.balance <= 0")
            return false
        }
        val accDB = database.accountDao().getAccount(serialNumber, password)
            ?: return false

        accDB.balance = balance
        database.accountDao().updateAccount(accDB)
        Log.d(TAG, "updateAccount: [after Update] accDB: $accDB")

        val testAccountDB = database.accountDao().getAccount(serialNumber, password)
        Log.d(TAG, "updateAccount: [last test] accDB: $testAccountDB")

        return true
    }


}