package com.kck.demoatm.interface_adapters.presenters

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.kck.demoatm.application.MyApplication
import com.kck.demoatm.entities.Account
import com.kck.demoatm.use_cases.GetAccountUseCase
import com.kck.demoatm.use_cases.WithdrawUseCase
import kotlinx.coroutines.launch

class WithdrawPresenter : ViewModel() {
    private val TAG: String = WithdrawPresenter::class.java.simpleName

    private val getAccountUseCase: GetAccountUseCase by lazy { MyApplication().getAccountUseCase }
    private val withdrawUseCase: WithdrawUseCase by lazy { MyApplication().withdrawUseCase }

    val accountLiveData: MutableLiveData<Account> = MutableLiveData()
    val messageLiveData: MutableLiveData<String> = MutableLiveData()
    val inputAmountLiveData: MutableLiveData<String> = MutableLiveData()
    val inputAmountInt
        get() = (inputAmountLiveData.value ?: "0").toInt()

    var nowAccount = Account.defaultAccount
    val textBalance
        get() = nowAccount.queryBalance().toString()
    val textMessage: String
        get() = messageLiveData.value ?: ""

    fun getAccount(serialNumber: String) {
        viewModelScope.launch {
            getAccountUseCase.invoke(serialNumber)
                .onSuccess {
                    Log.d(TAG, "getAccount: success: $it")
                    accountLiveData.postValue(it)
                    nowAccount = it
                }
                .onFailure {
                    Log.e(TAG, "getAccount: ${it.message}")
                    messageLiveData.postValue(it.message)
                }
        }
    }

    fun withdraw(account: Account, money: Int) {
        viewModelScope.launch {
            Log.d(TAG, "withdraw: acc: $account")
            withdrawUseCase.invoke(account.serialNumber, money)
                .onSuccess {
                    Log.d(TAG, "withdraw: success: money: $money")
                    getAccount(it.serialNumber)
                    messageLiveData.postValue("Success. Withdraw \$ $money")
                }
                .onFailure {
                    Log.e(TAG, "withdraw: fail: textMessage: $it")
                    messageLiveData.postValue(it.message)
                }
        }
    }
}