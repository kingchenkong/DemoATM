package com.kck.demoatm.application

import android.app.Application
import android.util.Log
import com.kck.demoatm.entities.Account
import com.kck.demoatm.frameworks_devices.data_source.local.AccountLocalDataSourceImpl
import com.kck.demoatm.frameworks_devices.data_source.local.IAccountLocalDataSource
import com.kck.demoatm.frameworks_devices.database.data_provider.DatabaseProviderImpl
import com.kck.demoatm.frameworks_devices.database.data_provider.IDatabaseProvider
import com.kck.demoatm.frameworks_devices.database.models.AccountDB
import com.kck.demoatm.interface_adapters.repositories.AccountRepositoryImpl
import com.kck.demoatm.interface_adapters.repositories.IAccountRepository
import com.kck.demoatm.use_cases.*
import kotlinx.coroutines.DelicateCoroutinesApi
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

class MyApplication : Application() {
    private val TAG: String = MyApplication::class.java.simpleName

    val databaseProvider: IDatabaseProvider by lazy { DatabaseProviderImpl(this@MyApplication) }
    val localDataSource: IAccountLocalDataSource by lazy { AccountLocalDataSourceImpl() }
    val repository: IAccountRepository by lazy { AccountRepositoryImpl() }

    // UseCases
    val depositUseCase: DepositUseCase by lazy { DepositUseCase() }
    val loginUseCase: LoginUseCase by lazy { LoginUseCase() }
    val queryBalanceUseCase: QueryBalanceUseCase by lazy { QueryBalanceUseCase() }
    val transferUseCase: TransferUseCase by lazy { TransferUseCase() }
    val withdrawUseCase: WithdrawUseCase by lazy { WithdrawUseCase() }

    @OptIn(DelicateCoroutinesApi::class)
    override fun onCreate() {
        super.onCreate()
        Log.e("MyApplication", "onCreate: ")
        GlobalScope.launch {
            initDatabase()
        }
    }

    // for init db table
    private suspend fun initDatabase() {
        val allAccount = databaseProvider.getAllAccount()
        displayAccount(allAccount)

        if (allAccount.isEmpty()) {
            if (needMockData) {
                databaseProvider.insertAccount(getAccountDBMock1())
                databaseProvider.insertAccount(getAccountDBMock2())
                databaseProvider.insertAccount(getAccountDBMock3())
                databaseProvider.insertAccount(getAccountDBMock4())
            }
            Log.e(TAG, "initialize: execute init db.")
        } else {
            Log.e(TAG, "initialize: stop init db.")
        }
    }

    private fun displayAccount(list: List<AccountDB>) {
        Log.e(TAG, "displayAccount: all Account size: ${list.size}")
        var count = 0
        list.forEach {
            count += 1
            Log.e(TAG, "displayAccount: $count, account: $it")
        }

    }

//    fun koinStarter() {
//        startKoin {
//            androidLogger(Level.ERROR)
//            androidContext(this@MyApplication)
//
//            modules(
//                DataProviderModule,
//                DataSourceModule,
//                RepositoryModule,
//                UseCaseModule,
//            )
//        }
//    }

}