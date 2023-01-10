package com.kck.demoatm.interface_adapters.presenters

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.kck.demoatm.application.ERROR_MSG_BALANCE
import com.kck.demoatm.application.MyApplication
import com.kck.demoatm.entities.Account
import com.kck.demoatm.use_cases.*
import kotlinx.coroutines.launch

class NewTransferPresenter : ViewModel() {
    private val TAG: String = NewTransferPresenter::class.java.simpleName

    enum class ErrorState {
        NORMAL, BALANCE_NOT_ENOUGH, SAME_ACC
    }

    private val getAccountUseCase: GetAccountUseCase by lazy { MyApplication().getAccountUseCase }
    private val withdrawUseCase: WithdrawUseCase by lazy { MyApplication().withdrawUseCase }
    private val depositUseCase: DepositUseCase by lazy { MyApplication().depositUseCase }
    private val transferUseCase: TransferUseCase by lazy { MyApplication().transferUseCase }
    private val checkBalanceEnoughUseCase: CheckBalanceEnoughUseCase by lazy { MyApplication().checkBalanceEnoughUseCase }
    private val checkDuplicateUseCase: CheckDuplicateUseCase by lazy { MyApplication().checkDuplicateUseCase }

    val errorStateLiveData: MutableLiveData<TransferPresenter.ErrorState> = MutableLiveData()

    val sourceAccountLiveData: MutableLiveData<Account> = MutableLiveData()
    val sourceAccount: Account
        get() = sourceAccountLiveData.value ?: Account.defaultAccount
    val sourceBalance: Int
        get() = sourceAccount.queryBalance()
    val sourceBalanceText: String
        get() = sourceBalance.toString()

    val destAccountLiveData: MutableLiveData<Account> = MutableLiveData()
    val destAccount: Account
        get() = destAccountLiveData.value ?: Account.defaultAccount
    val destBalance: Int
        get() = destAccount.queryBalance()
    val destBalanceText: String
        get() = destBalance.toString()

    val messageLiveData: MutableLiveData<String> = MutableLiveData()
    val messageText: String
        get() = messageLiveData.value ?: ""

    val inputAmountLiveData: MutableLiveData<String> = MutableLiveData()
    val inputAmountText: String
        get() = inputAmountLiveData.value ?: "0"
    val inputAmountInt: Int
        get() = getterInputAmountInt()

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
                    sourceAccountLiveData.postValue(it)
                }
                .onFailure {
                    Log.e(TAG, "getAccount: ${it.message}")
                    messageLiveData.postValue(it.message)
                }
        }
    }

    fun checkAmountOK(): Boolean {
        val isBalanceEnough: Boolean =
            checkBalanceEnoughUseCase.invoke(sourceBalance, inputAmountInt)
        Log.e(TAG, "checkAmountOK: sourceBalance: $sourceBalance")
        Log.e(TAG, "checkAmountOK: inputAmountInt: $inputAmountInt")
        Log.e(TAG, "checkAmountOK: isBalanceEnough: $isBalanceEnough")
        return if (isBalanceEnough) {
            messageLiveData.postValue("Amount is OK")
            errorStateLiveData.postValue(TransferPresenter.ErrorState.NORMAL)
            true
        } else {
            messageLiveData.postValue(ERROR_MSG_BALANCE)
            errorStateLiveData.postValue(TransferPresenter.ErrorState.BALANCE_NOT_ENOUGH)
            false
        }
    }


}