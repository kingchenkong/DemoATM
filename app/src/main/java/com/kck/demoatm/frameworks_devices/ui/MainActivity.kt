package com.kck.demoatm.frameworks_devices.ui

import android.os.Bundle
import android.util.Log
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.lifecycleScope
import com.kck.demoatm.R
import com.kck.demoatm.application.*
import com.kck.demoatm.databinding.ActivityMainBinding
import com.kck.demoatm.frameworks_devices.data_source.local.IAccountLocalDataSource
import com.kck.demoatm.frameworks_devices.database.data_provider.IDatabaseProvider
import com.kck.demoatm.interface_adapters.presenters.AccountViewModel
import com.kck.demoatm.interface_adapters.repositories.IAccountRepository
import com.kck.demoatm.use_cases.*

class MainActivity : AppCompatActivity() {
    private val TAG: String = MainActivity::class.java.simpleName

    private val myApp by lazy { MyApplication() }

    private lateinit var binding: ActivityMainBinding

    private val accountViewModel: AccountViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_main)
        binding.lifecycleOwner = this

        initListener()

        lifecycleScope.launchWhenResumed {
//            test()
        }
    }

    private fun initListener() {
        binding.btnLogin.setOnClickListener {
            Log.e(TAG, "initListener: btnLogin")
            accountViewModel.login(
                binding.editSerial.text.toString(),
                binding.editPwd.text.toString()
            )
        }

        binding.btnMock1.setOnClickListener {
            Log.e(TAG, "initListener: btnMock1")
            accountViewModel.login(
                MOCK_1_ACC_SN,
                MOCK_1_ACC_PWD
            )
        }
        binding.btnMock2.setOnClickListener {
            Log.e(TAG, "initListener: btnMock2")
            accountViewModel.login(
                MOCK_2_ACC_SN,
                MOCK_2_ACC_PWD
            )
        }
        binding.btnMock3.setOnClickListener {
            Log.e(TAG, "initListener: btnMock3")
            accountViewModel.login(
                MOCK_3_ACC_SN,
                MOCK_3_ACC_PWD
            )
        }
        binding.btnMock4.setOnClickListener {
            Log.e(TAG, "initListener: btnMock4")
            accountViewModel.login(
                MOCK_4_ACC_SN,
                MOCK_4_ACC_PWD
            )
        }

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

        // database provider
        val databaseProvider: IDatabaseProvider = myApp.databaseProvider
//        val accountDBList = databaseProvider.getAccountList(serialNum, password)
//        Log.e(TAG, "test: accountDB list size: ${accountDBList.size}")
//        Log.e(TAG, "test: accountDB list: $accountDBList")

        // data source
        val localDataSource: IAccountLocalDataSource = myApp.localDataSource
//        val result = localDataSource.getAccount(sn, pwd)
//        Log.e(TAG, "test: result: $result")

        // repository
        val repository: IAccountRepository = myApp.repository
//        val result = repository.getAccount(SourceType.LOCAL)
//        Log.e(TAG, "test: result: $result")
        val allAcc = repository.getAll()
        var count = 0
        allAcc.forEach {
            count += 1
            Log.e(TAG, "test: $count, account: $it")
        }

        // useCase
        val loginUseCase: LoginUseCase = myApp.loginUseCase
        val queryBalanceUseCase: QueryBalanceUseCase = myApp.queryBalanceUseCase
        val withdrawUseCase: WithdrawUseCase = myApp.withdrawUseCase
        val depositUseCase: DepositUseCase = myApp.depositUseCase

//        val loginDef = loginUseCase.invoke(ACC_SN_DEF, ACC_PWD_DEF)
//        Log.e(TAG, "test: loginDef: $loginDef")
//        val loginMock1 = loginUseCase.invoke(MOCK_1_ACC_SN, MOCK_1_ACC_PWD)
//        Log.e(TAG, "test: login mock1: $loginMock1")
//        val queryMock1 = queryBalanceUseCase.invoke(MOCK_1_ACC_SN, MOCK_1_ACC_PWD)
//        Log.e(TAG, "test: query mock1: $queryMock1")
//
//        val accMock1 = withdrawUseCase.invoke(MOCK_1_ACC_SN, MOCK_1_ACC_PWD, 100)
//        Log.e(TAG, "test: accMock1: $accMock1")
//        val accMock2 = withdrawUseCase.invoke(MOCK_2_ACC_SN, MOCK_2_ACC_PWD, 100)
//        Log.e(TAG, "test: accMock2 $accMock2")
//        val accMock3 = depositUseCase.invoke(MOCK_3_ACC_SN, MOCK_3_ACC_PWD, 100)
//        Log.e(TAG, "test: accMock3: $accMock3")
//        val accMock4 = depositUseCase.invoke(MOCK_4_ACC_SN, MOCK_4_ACC_PWD, 100)
//        Log.e(TAG, "test: accMock4: $accMock4")

        val transferUseCase: TransferUseCase = myApp.transferUseCase

        val transferMoney1 = transferUseCase.invoke(
            MOCK_1_ACC_SN, MOCK_1_ACC_PWD,
            MOCK_2_ACC_SN, MOCK_2_ACC_PWD,
            100
        )
        Log.e(TAG, "test: accDef: $transferMoney1")

        val transferMoney2 = transferUseCase.invoke(
            MOCK_3_ACC_SN, MOCK_3_ACC_PWD,
            MOCK_4_ACC_SN, MOCK_4_ACC_PWD,
            100
        )
        Log.e(TAG, "test: accMock2 $transferMoney2")

        // check
        val checkAllAcc = repository.getAll()
        var count2 = 0
        checkAllAcc.forEach {
            count2 += 1
            Log.e(TAG, "test: $count2, account: $it")
        }

    }
}