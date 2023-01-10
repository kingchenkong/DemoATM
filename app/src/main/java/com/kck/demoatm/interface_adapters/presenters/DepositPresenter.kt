package com.kck.demoatm.interface_adapters.presenters

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.kck.demoatm.application.MyApplication
import com.kck.demoatm.entities.Account
import com.kck.demoatm.use_cases.DepositUseCase
import com.kck.demoatm.use_cases.GetAccountUseCase
import kotlinx.coroutines.launch

class DepositPresenter : ViewModel() {
    private val TAG: String = DepositPresenter::class.java.simpleName

    private val getAccountUseCase: GetAccountUseCase by lazy { MyApplication().getAccountUseCase }
    private val depositUseCase: DepositUseCase by lazy { MyApplication().depositUseCase }

    val accountLiveData: MutableLiveData<Account> = MutableLiveData()
    var nowAccount = Account.defaultAccount
    val textNowBalance
        get() = nowAccount.queryBalance().toString()

    val inputAmountLiveData: MutableLiveData<String> = MutableLiveData()
    val inputAmountInt
        get() = (inputAmountLiveData.value ?: "0").toInt()

    fun getAccount(serialNumber: String) {
        viewModelScope.launch {
            getAccountUseCase.invoke(serialNumber)
                .onSuccess {
                    Log.d(TAG, "getAccount: $it")
                    accountLiveData.postValue(it)
                    nowAccount = it
                }
                .onFailure {
                    Log.e(TAG, "getAccount: ${it.message}")
                }
        }
    }

    fun deposit(account: Account, amount: Int) {
        viewModelScope.launch {
            Log.e(TAG, "deposit: acc: $account")
            depositUseCase.invoke(account.serialNumber, amount)
                .onSuccess {
                    Log.d(TAG, "deposit: account: $it")
                    getAccount(account.serialNumber)
                }
                .onFailure {
                    Log.e(TAG, "deposit: fail: ${it.message}")
                }
        }
    }

}