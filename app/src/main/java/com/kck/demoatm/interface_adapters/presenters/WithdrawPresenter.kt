package com.kck.demoatm.interface_adapters.presenters

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.kck.demoatm.application.ERROR_MSG_BALANCE_NOT_ENOUGH
import com.kck.demoatm.application.MyApplication
import com.kck.demoatm.entities.Account
import com.kck.demoatm.use_cases.CheckBalanceEnoughUseCase
import com.kck.demoatm.use_cases.GetAccountUseCase
import com.kck.demoatm.use_cases.WithdrawUseCase
import kotlinx.coroutines.launch

class WithdrawPresenter : ViewModel() {
    private val TAG: String = WithdrawPresenter::class.java.simpleName

    private val getAccountUseCase: GetAccountUseCase by lazy { MyApplication().getAccountUseCase }
    private val checkBalanceEnoughUseCase: CheckBalanceEnoughUseCase by lazy { MyApplication().checkBalanceEnoughUseCase }
    private val withdrawUseCase: WithdrawUseCase by lazy { MyApplication().withdrawUseCase }

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
    val textMessage: String
        get() = messageLiveData.value ?: ""

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

    fun amountEqualZero(amount: Int): Boolean = amount == 0


    fun checkBalanceEnough(account: Account, money: Int): Boolean {
        val enough = checkBalanceEnoughUseCase.invoke(account.queryBalance(), money)
        Log.e(TAG, "checkBalanceEnough: $enough")
        return enough
    }

    fun withdraw(account: Account, money: Int) {
        viewModelScope.launch {
            Log.e(TAG, "withdraw: acc: $account")

            val amountEqualZero: Boolean = amountEqualZero(money)
            if (amountEqualZero) {
                Log.e(TAG, "withdraw: amount == 0")
                messageLiveData.postValue("Fail. amount == 0")
                return@launch
            }

            val enough: Boolean = checkBalanceEnough(account, money)
            if (!enough) {
                Log.e(TAG, "withdraw: textMessage: $ERROR_MSG_BALANCE_NOT_ENOUGH")
                messageLiveData.postValue(ERROR_MSG_BALANCE_NOT_ENOUGH)
                return@launch
            }
            withdrawUseCase.invoke(account.serialNumber, money)
                .onSuccess {
                    Log.e(TAG, "withdraw: money: $money")
                    getAccount(it.serialNumber)
                    messageLiveData.postValue("Success. Withdraw \$ $money")
                }
                .onFailure {
                    Log.e(TAG, "withdraw: textMessage: $textMessage")
                    messageLiveData.postValue(it.message)
                }
        }
    }

}