package com.kck.demoatm.interface_adapters.presenters

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.kck.demoatm.application.MyApplication
import com.kck.demoatm.entities.Account
import com.kck.demoatm.use_cases.*
import kotlinx.coroutines.launch

class TransferPresenter : ViewModel() {
    private val TAG: String = TransferPresenter::class.java.simpleName

    private val getAccountUseCase: GetAccountUseCase by lazy { MyApplication().getAccountUseCase }
    private val transferUseCase: TransferUseCase by lazy { MyApplication().transferUseCase }

    val sourceAccountLiveData: MutableLiveData<Account> = MutableLiveData()
    val sourceAccount: Account
        get() = sourceAccountLiveData.value ?: Account.defaultAccount
    val sourceBalanceText: String
        get() = sourceAccount.queryBalance().toString()

    val inputAmountLiveData: MutableLiveData<String> = MutableLiveData()
    private val inputAmountText: String
        get() = inputAmountLiveData.value ?: "0"
    private val inputAmountInt: Int
        get() = getterInputAmountInt()

    val inputDestSNLiveData: MutableLiveData<String> = MutableLiveData()
    private val destSNText: String
        get() = inputDestSNLiveData.value ?: ""

    val messageLiveData: MutableLiveData<String> = MutableLiveData()
    val messageText: String
        get() = messageLiveData.value ?: ""

    private fun getterInputAmountInt(): Int {
        return if (inputAmountText.isEmpty()) {
            0
        } else {
            inputAmountText.toInt()
        }
    }

    fun getAccount(serialNumber: String) {
        viewModelScope.launch {
            getAccountUseCase.invoke(serialNumber)
                .onSuccess {
                    Log.d(TAG, "getAccount: success")
                    sourceAccountLiveData.postValue(it)
                }
                .onFailure {
                    Log.e(TAG, "getAccount: ${it.message}")
                    messageLiveData.postValue(it.message)
                }
        }
    }

    fun validating() {
        viewModelScope.launch {
            // source account
            transferUseCase.validating(
                sourceAccount.serialNumber,
                sourceAccount.queryBalance(),
                destSNText,
                inputAmountInt
            )
                .onSuccess { Log.d(TAG, "validating: OK.")
                messageLiveData.postValue("validating: OK.")}
                .onFailure { messageLiveData.postValue(it.message) }
        }
    }

    fun transfer() {
        val sourceSN = sourceAccount.serialNumber
        val destSN = destSNText
        val amount = inputAmountInt
        Log.d(TAG, "transfer: sourceSN: $sourceSN, destSN: $destSN, amount: $amount")
        viewModelScope.launch {
            transferUseCase.invoke(
                sourceSN = sourceSN,
                destSN = destSN,
                amount = amount
            ).onSuccess {
                Log.d(TAG, "transfer: dest-Account: $it")
                messageLiveData.postValue("Transfer Success, \$ $amount")
                Log.d(TAG, "transfer: re-getAccount")
                getAccount(sourceSN)
            }.onFailure {
                Log.e(TAG, "transfer: Fail, $it")
                messageLiveData.postValue(it.message)
            }
        }
    }

}