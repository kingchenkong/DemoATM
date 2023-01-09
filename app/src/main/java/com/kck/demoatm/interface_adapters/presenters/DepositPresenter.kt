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
    val messageLiveData: MutableLiveData<String> = MutableLiveData()
    val inputAmountLiveData: MutableLiveData<String> = MutableLiveData()
    val inputAmountInt
        get() = (inputAmountLiveData.value ?: "0").toInt()

    var nowAccount = Account.defaultAccount
    val nowBalance
        get() = nowAccount.queryBalance()
    val textBalance
        get() = nowAccount.queryBalance().toString()
    var textMessage: String = ""

    fun getAccount(serialNumber: String) {
        viewModelScope.launch {
            getAccountUseCase.invoke(serialNumber)
                .onSuccess {
                    accountLiveData.postValue(it)
                    nowAccount = it
                }
                .onFailure {
                    Log.e(TAG, "getAccount: ${it.message}")
                    messageLiveData.postValue(it.message)
                }
        }
    }

    fun deposit(account: Account, money: Int) {
        viewModelScope.launch {
            Log.e(TAG, "deposit: acc: $account")
            depositUseCase.invoke(account.serialNumber, money)
                .onSuccess {
                    Log.e(TAG, "deposit: money: $it")
                    getAccount(account.serialNumber)
                }
                .onFailure {
                    textMessage = it.message ?: "throwable no message"
                    Log.e(TAG, "deposit: nowMessage: $textMessage")
                    messageLiveData.postValue(it.message)
                }
        }
    }

}