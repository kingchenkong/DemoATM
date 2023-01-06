package com.kck.demoatm.framworks_devies.database.data_provider

import android.content.Context
import android.util.Log
import androidx.annotation.VisibleForTesting
import androidx.room.Room
import com.kck.demoatm.framworks_devies.database.dao.DemoDatabase
import com.kck.demoatm.framworks_devies.database.models.AccountDB
import com.kck.demoatm.getAccountDBMock1
import com.kck.demoatm.getAccountDBMock2
import com.kck.demoatm.getAccountDBMock3
import org.koin.core.context.GlobalContext

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
            database.accountDao().addAccount(getAccountDBMock1())
            database.accountDao().addAccount(getAccountDBMock2())
            database.accountDao().addAccount(getAccountDBMock3())
            Log.e("DatabaseProviderImpl", "initialize: execute init db.")
        } else {

            Log.e("DatabaseProviderImpl", "initialize: stop init db.")
        }
    }

    override suspend fun getAllAccount(): List<AccountDB> = database.accountDao().getAll()

    override suspend fun getAccount(
        serialNumber: String,
        password: String
    ): AccountDB =
        database.accountDao().getAccount(
            serialNumber = serialNumber,
            password = password
        )

    override suspend fun login(
        serialNumber: String,
        password: String
    ): AccountDB =
        database.accountDao().getAccount(
            serialNumber = serialNumber,
            password = password
        )


}