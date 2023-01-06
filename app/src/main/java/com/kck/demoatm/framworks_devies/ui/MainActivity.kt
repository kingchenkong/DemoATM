package com.kck.demoatm.framworks_devies.ui

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.activity.viewModels
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.lifecycleScope
import com.kck.demoatm.ACCOUNT_PASSWORD_DEF
import com.kck.demoatm.ACCOUNT_SERIAL_NUM_DEF
import com.kck.demoatm.R
import com.kck.demoatm.SourceType
import com.kck.demoatm.databinding.ActivityMainBinding
import com.kck.demoatm.framworks_devies.data_source.local.AccountLocalDataSourceImpl
import com.kck.demoatm.framworks_devies.database.data_provider.DatabaseProviderImpl
import com.kck.demoatm.interface_adapters.presenters.AccountViewModel
import com.kck.demoatm.interface_adapters.repositories.AccountRepositoryImpl

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

        val sn = ACCOUNT_SERIAL_NUM_DEF
        val pwd = ACCOUNT_PASSWORD_DEF
        Log.e(TAG, "test: sn: $sn, pwd: $pwd")

        val dataProvider = DatabaseProviderImpl(this)
//        val accountDBList = dataProvider.getAccountList(serialNum, password)
//        Log.e(TAG, "test: accountDB list size: ${accountDBList.size}")
//        Log.e(TAG, "test: accountDB list: $accountDBList")

        val localDataSource = AccountLocalDataSourceImpl(dataProvider)
//        val result = localDataSource.getAccount(sn, pwd)
//        Log.e(TAG, "test: result: $result")

        val repository = AccountRepositoryImpl(sn, pwd, localDataSource)
        val result = repository.getAccount(SourceType.LOCAL)
        Log.e(TAG, "test: result: $result")

    }
}