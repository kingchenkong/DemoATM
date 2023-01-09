package com.kck.demoatm.interface_adapters.presenters

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.kck.demoatm.application.MyApplication
import com.kck.demoatm.entities.Account
import com.kck.demoatm.use_cases.*
import kotlinx.coroutines.launch

class AccountViewModel : ViewModel() {
    private val TAG: String = AccountViewModel::class.java.simpleName

    private val loginUseCase: LoginUseCase by lazy { MyApplication().loginUseCase }
    private val queryBalanceUseCase: QueryBalanceUseCase by lazy { MyApplication().queryBalanceUseCase }
    private val withdrawUseCase: WithdrawUseCase by lazy { MyApplication().withdrawUseCase }
    private val depositUseCase: DepositUseCase by lazy { MyApplication().depositUseCase }
    private val transferUseCase: TransferUseCase by lazy { MyApplication().transferUseCase }


    // livedata
    val accountLiveData = MutableLiveData<Account>()
    val balanceLiveData = MutableLiveData<Int>()
    val messageLiveData = MutableLiveData<String>()

    // variable
    var nowAccount = Account.defaultAccount
    var nowMessage = ""

    // function
    fun login(sn: String, pwd: String) {
        viewModelScope.launch {
            loginUseCase.invoke(sn, pwd).onSuccess {
                Log.e(TAG, "login: $it")
                accountLiveData.postValue(it)
                nowAccount = it
            }.onFailure {
                Log.e(TAG, "login: ${it.message}")
                messageLiveData.postValue(it.message)
            }
        }
    }

    fun queryBalance(account: Account) {
        viewModelScope.launch {
            Log.e(TAG, "queryBalance: acc: $account")
            queryBalanceUseCase.invoke(account.serialNumber, account.password)
                .onSuccess {
                    Log.e(TAG, "queryBalance: $it")
                    nowMessage = "Now Balance: $it"
                    messageLiveData.postValue("Now Balance: $it")
                }
                .onFailure {
                    nowMessage = it.message ?: "throwable no message"
                    Log.e(TAG, "withdraw: nowMessage: $nowMessage")
                    messageLiveData.postValue(it.message)
                }
        }
    }

    fun withdraw(account: Account, money: Int) {
        viewModelScope.launch {
            Log.e(TAG, "withdraw: acc: $account")
            withdrawUseCase.invoke(account.serialNumber, account.password, money)
                .onSuccess {
                    balanceLiveData.postValue(it)
                    Log.e(TAG, "withdraw: money: $it")
                    login(account.serialNumber, account.password)
                    queryBalance(account)
                }
                .onFailure {
                    nowMessage = it.message ?: "throwable no message"
                    Log.e(TAG, "withdraw: nowMessage: $nowMessage")
                    messageLiveData.postValue(it.message)
                }
        }
    }

    fun deposit(account: Account, money: Int) {
        viewModelScope.launch {
            Log.e(TAG, "deposit: acc: $account")
            depositUseCase.invoke(account.serialNumber, account.password, money)
                .onSuccess {
                    Log.e(TAG, "deposit: money: $it")
                    login(account.serialNumber, account.password)
                    queryBalance(account)
                }
                .onFailure {
                    nowMessage = it.message ?: "throwable no message"
                    Log.e(TAG, "withdraw: nowMessage: $nowMessage")
                    messageLiveData.postValue(it.message)
                }

        }
    }

}