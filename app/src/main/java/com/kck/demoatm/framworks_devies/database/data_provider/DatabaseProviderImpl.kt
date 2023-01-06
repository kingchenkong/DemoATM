package com.kck.demoatm.framworks_devies.database.data_provider

import android.content.Context
import android.util.Log
import androidx.annotation.VisibleForTesting
import androidx.room.Room
import com.kck.demoatm.ACCOUNT_BALANCE_DEF
import com.kck.demoatm.ACCOUNT_DISPLAY_NAME_DEF
import com.kck.demoatm.ACCOUNT_PASSWORD_DEF
import com.kck.demoatm.ACCOUNT_SERIAL_NUM_DEF
import com.kck.demoatm.framworks_devies.database.dao.DemoDatabase
import com.kck.demoatm.framworks_devies.database.models.AccountDB

class DatabaseProviderImpl(context: Context) : IDatabaseProvider {

    private val database: DemoDatabase by lazy { buildDataBase(context) }

    @VisibleForTesting(otherwise = VisibleForTesting.PRIVATE)
    internal fun buildDataBase(
        context: Context,
    ): DemoDatabase {
        val name = "db_demo"
        return Room.databaseBuilder(
            context,
            DemoDatabase::class.java,
            name
        ).build()
    }

    // for init db table
    override suspend fun initialize() {
        val allAccount = database.accountDao().getAll()
        Log.e("DatabaseProviderImpl", "initialize: all Account size: ${allAccount.size}")
        if (allAccount.isEmpty()) {
            database.accountDao().addAccount(AccountDB.defaultAccountDB)
        }
    }

    override suspend fun getAllAccount(): List<AccountDB> = database.accountDao().getAll()

    override suspend fun getAccountList(
        serialNumber: String,
        password: String
    ): List<AccountDB> =
        database.accountDao().getAccountList(
            serialNumber = serialNumber,
            password = password
        )

}