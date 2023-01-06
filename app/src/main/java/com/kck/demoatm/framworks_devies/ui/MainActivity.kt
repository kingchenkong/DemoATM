package com.kck.demoatm.framworks_devies.ui

import android.os.Bundle
import android.util.Log
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.lifecycleScope
import com.kck.demoatm.*
import com.kck.demoatm.databinding.ActivityMainBinding
import com.kck.demoatm.framworks_devies.data_source.local.IAccountLocalDataSource
import com.kck.demoatm.framworks_devies.database.data_provider.IDatabaseProvider
import com.kck.demoatm.interface_adapters.presenters.AccountViewModel
import com.kck.demoatm.interface_adapters.repositories.IAccountRepository
import com.kck.demoatm.use_cases.LoginUseCase
import com.kck.demoatm.use_cases.QueryBalanceUseCase
import org.koin.core.context.GlobalContext

class MainActivity : AppCompatActivity() {
    private val TAG: String = MainActivity::class.java.simpleName

    private lateinit var binding: ActivityMainBinding

    private val accountViewModel: AccountViewModel by viewModels()


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_main)
        binding.lifecycleOwner = this

        lifecycleScope.launchWhenCreated { test() }
    }

    private suspend fun test() {
//        val accountRepository = AccountRepositoryImpl(
//            ACCOUNT_SERIAL_NUM_DEF, ACCOUNT_PASSWORD_DEF,
//            AccountLocalDataSourceImpl(DatabaseProviderImpl(this))
//        )
//        val account = accountRepository.getAccount(SourceType.LOCAL)
//        Log.e(TAG, "test: account: $account")

        val sn = ACC_SN_DEF
        val pwd = ACC_PWD_DEF
        Log.e(TAG, "test: sn: $sn, pwd: $pwd")

        // data provider
        val dataProvider: IDatabaseProvider by GlobalContext.get().inject()
//        val accountDBList = dataProvider.getAccountList(serialNum, password)
//        Log.e(TAG, "test: accountDB list size: ${accountDBList.size}")
//        Log.e(TAG, "test: accountDB list: $accountDBList")

        // data source
        val localDataSource: IAccountLocalDataSource by GlobalContext.get().inject()
//        val result = localDataSource.getAccount(sn, pwd)
//        Log.e(TAG, "test: result: $result")

        // repository
        val repository: IAccountRepository by GlobalContext.get().inject()
//        val result = repository.getAccount(SourceType.LOCAL)
//        Log.e(TAG, "test: result: $result")
        val allAcc = repository.getAll()
        var count = 0
        allAcc.forEach {
            count += 1
            Log.e(TAG, "test: account: $count, $it")
        }

        // useCase
        val loginUseCase: LoginUseCase by GlobalContext.get().inject()
        val queryBalanceUseCase: QueryBalanceUseCase by GlobalContext.get().inject()

        val accDef = queryBalanceUseCase.invoke( ACC_SN_DEF, ACC_PWD_DEF)
        Log.e(TAG, "test: accDef: $accDef")
        val accMock1 = queryBalanceUseCase.invoke( MOCK_1_ACC_SN, MOCK_1_ACC_PWD)
        Log.e(TAG, "test: accMock1: $accMock1")
        val accMock2 = queryBalanceUseCase.invoke(MOCK_2_ACC_SN, MOCK_2_ACC_PWD)
        Log.e(TAG, "test: accMock2 $accMock2")
        val accMock3 = queryBalanceUseCase.invoke(MOCK_3_ACC_SN, MOCK_3_ACC_PWD)
        Log.e(TAG, "test: accMock3: $accMock3")

    }
}