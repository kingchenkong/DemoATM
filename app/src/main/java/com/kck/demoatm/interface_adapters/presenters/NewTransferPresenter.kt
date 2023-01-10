package com.kck.demoatm.interface_adapters.presenters

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.kck.demoatm.application.ERROR_MSG_AMOUNT_IS_0
import com.kck.demoatm.application.ERROR_MSG_BALANCE_NOT_ENOUGH
import com.kck.demoatm.application.ERROR_MSG_SAME_ACC
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
//    val destBalance: Int
//        get() = destAccount.queryBalance()
//    val destBalanceText: String
//        get() = destBalance.toString()

    val inputAmountLiveData: MutableLiveData<String> = MutableLiveData()
    val inputAmountText: String
        get() = inputAmountLiveData.value ?: "0"
    val inputAmountInt: Int
        get() = getterInputAmountInt()

    val inputDestSNLiveData: MutableLiveData<String> = MutableLiveData()
    val destSNText: String
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
                    sourceAccountLiveData.postValue(it)
                }
                .onFailure {
                    Log.e(TAG, "getAccount: ${it.message}")
                    messageLiveData.postValue(it.message)
                }
        }
    }

    fun checkAmountOK(balance: Int, amount: Int): Boolean {
        if (amount == 0) {
            Log.w(TAG, "checkAmountOK: amount == 0")
            messageLiveData.postValue(ERROR_MSG_AMOUNT_IS_0)
            return false
        }

        val isBalanceEnough: Boolean =
            checkBalanceEnoughUseCase.invoke(balance, amount)
        Log.d(TAG, "checkAmountOK: balance: $balance")
        Log.d(TAG, "checkAmountOK: amount: $amount")
        Log.d(TAG, "checkAmountOK: isBalanceEnough: $isBalanceEnough")
        return if (isBalanceEnough) {
            messageLiveData.postValue("Amount is OK")
            true
        } else {
            messageErrorBalanceNotEnough()
            false
        }
    }

    fun checkDestDuplicate(sourceSN: String, destSN: String): Boolean {
        val isDuplicate: Boolean =
            checkDuplicateUseCase.invoke(sourceSN, destSN)
        Log.d(TAG, "checkDestDuplicate: $isDuplicate")
        if (isDuplicate) {
            messageDuplicateAccount()
        } else {
            messageLiveData.postValue("OK, No duplicate.")
        }
        return isDuplicate
    }

    private fun messageDuplicateAccount() {
        Log.w(TAG, "messageDuplicateAccount: ")
        messageLiveData.postValue(ERROR_MSG_SAME_ACC)
        errorStateLiveData.postValue(TransferPresenter.ErrorState.SAME_ACC)
    }

    private fun messageErrorBalanceNotEnough() {
        Log.w(TAG, "messageErrorBalanceNotEnough: ")
        messageLiveData.postValue(ERROR_MSG_BALANCE_NOT_ENOUGH)
        errorStateLiveData.postValue(TransferPresenter.ErrorState.BALANCE_NOT_ENOUGH)
    }

    fun transfer(
        sourceAccount: Account, destSN: String, amount: Int
    ) {
        val sourceSN = sourceAccount.serialNumber
        viewModelScope.launch {
            if (checkDestDuplicate(sourceSN, destSN)) {
                messageDuplicateAccount()
            } else if (!checkAmountOK(sourceAccount.queryBalance(), amount)) {
                messageErrorBalanceNotEnough()
            } else {
                transferUseCase.invoke(
                    sourceSN = sourceSN,
                    destSN = destSN,
                    money = amount
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

}