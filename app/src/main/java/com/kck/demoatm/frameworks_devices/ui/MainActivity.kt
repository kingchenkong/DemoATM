package com.kck.demoatm.frameworks_devices.ui

import android.os.Bundle
import android.util.Log
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.lifecycleScope
import com.kck.demoatm.*
import com.kck.demoatm.application.*
import com.kck.demoatm.databinding.ActivityMainBinding
import com.kck.demoatm.frameworks_devices.data_source.local.IAccountLocalDataSource
import com.kck.demoatm.frameworks_devices.database.data_provider.IDatabaseProvider
import com.kck.demoatm.interface_adapters.presenters.AccountViewModel
import com.kck.demoatm.interface_adapters.repositories.IAccountRepository
import com.kck.demoatm.use_cases.*
import org.koin.core.context.GlobalContext

class MainActivity : AppCompatActivity() {
    private val TAG: String = MainActivity::class.java.simpleName

    private lateinit var binding: ActivityMainBinding

    private val accountViewModel: AccountViewModel by viewModels()


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_main)
        binding.lifecycleOwner = this

        lifecycleScope.launchWhenStarted { test() }
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
            Log.e(TAG, "test: $count, account: $it")
        }

        // useCase
        val loginUseCase: LoginUseCase by GlobalContext.get().inject()
        val queryBalanceUseCase: QueryBalanceUseCase by GlobalContext.get().inject()
        val withdrawUseCase: WithdrawUseCase by GlobalContext.get().inject()
        val depositUseCase: DepositUseCase by GlobalContext.get().inject()

//        val accDef = depositUseCase.invoke(ACC_SN_DEF, ACC_PWD_DEF, 100)
//        Log.e(TAG, "test: accDef: $accDef")
        val accMock1 = depositUseCase.invoke(MOCK_1_ACC_SN, MOCK_1_ACC_PWD, 100)
        Log.e(TAG, "test: accMock1: $accMock1")
        val accMock2 = depositUseCase.invoke(MOCK_2_ACC_SN, MOCK_2_ACC_PWD, 100)
        Log.e(TAG, "test: accMock2 $accMock2")
        val accMock3 = depositUseCase.invoke(MOCK_3_ACC_SN, MOCK_3_ACC_PWD, 100)
        Log.e(TAG, "test: accMock3: $accMock3")
        val accMock4 = depositUseCase.invoke(MOCK_4_ACC_SN, MOCK_4_ACC_PWD, 100)
        Log.e(TAG, "test: accMock4: $accMock4")

        val transferUseCase: TransferUseCase by GlobalContext.get().inject()

//        val transferMoney1 = transferUseCase.invoke(
//            MOCK_1_ACC_SN, MOCK_1_ACC_PWD,
//            MOCK_2_ACC_SN, MOCK_2_ACC_PWD,
//            100
//        )
//        Log.e(TAG, "test: accDef: $transferMoney1")
//
//        val transferMoney2 = transferUseCase.invoke(
//            MOCK_3_ACC_SN, MOCK_3_ACC_PWD,
//            MOCK_4_ACC_SN, MOCK_4_ACC_PWD,
//            100
//        )
//        Log.e(TAG, "test: accMock2 $transferMoney2")

        // check
        val checkAllAcc = repository.getAll()
        var count2 = 0
        checkAllAcc.forEach {
            count2 += 1
            Log.e(TAG, "test: $count2, account: $it")
        }

    }
}