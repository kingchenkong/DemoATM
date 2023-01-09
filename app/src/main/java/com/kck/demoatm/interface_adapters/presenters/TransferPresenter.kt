package com.kck.demoatm.interface_adapters.presenters

import android.util.Log
import androidx.databinding.ObservableField
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.kck.demoatm.application.ERROR_MSG_BALANCE
import com.kck.demoatm.application.ERROR_MSG_SAME_ACC
import com.kck.demoatm.application.MyApplication
import com.kck.demoatm.entities.Account
import com.kck.demoatm.use_cases.*
import kotlinx.coroutines.launch

class TransferPresenter : ViewModel() {
    private val TAG: String = TransferPresenter::class.java.simpleName

    //    private val loginUseCase: LoginUseCase by lazy { MyApplication().loginUseCase }
    private val getAccountUseCase: GetAccountUseCase by lazy { MyApplication().getAccountUseCase }
    private val withdrawUseCase: WithdrawUseCase by lazy { MyApplication().withdrawUseCase }
    private val depositUseCase: DepositUseCase by lazy { MyApplication().depositUseCase }
    private val transferUseCase: TransferUseCase by lazy { MyApplication().transferUseCase }
    private val checkBalanceEnoughUseCase: CheckBalanceEnoughUseCase by lazy { MyApplication().checkBalanceEnoughUseCase }
    private val checkDuplicateUseCase: CheckDuplicateUseCase by lazy { MyApplication().checkDuplicateUseCase }

    enum class ErrorState {
        NORMAL, BALANCE_NOT_ENOUGH, SAME_ACC
    }

    enum class IsMOCKDataState {
        NORMAL, MOCK_1, MOCK_2, MOCK_3, MOCK_4
    }

    val errorStateLiveData: MutableLiveData<ErrorState> = MutableLiveData()
    val isMockDataStateLiveData: MutableLiveData<IsMOCKDataState> = MutableLiveData()

    val accountLiveData: MutableLiveData<Account> = MutableLiveData()
    val messageLiveData: MutableLiveData<String> = MutableLiveData()

    val inputAmountLiveData: MutableLiveData<String> = MutableLiveData()
    val inputAmountInt
        get() = inputAmountLiveData.value?.toInt()

    val inputSerialNumberLiveData: MutableLiveData<String> = MutableLiveData()
    val inputPasswordLiveData: MutableLiveData<String> = MutableLiveData()

    var sourceAccount: ObservableField<Account> = ObservableField(Account.defaultAccount)
//    var inputAmount: ObservableField<String> = ObservableField("")
//    var inputSerialNum: ObservableField<String> = ObservableField("")
//    var inputPassword: ObservableField<String> = ObservableField("")

    fun getAccount(serialNumber: String) {
        viewModelScope.launch {
            getAccountUseCase.invoke(serialNumber)
                .onSuccess {
                    Log.e(TAG, "getAccount: $it")
                    accountLiveData.postValue(it)
                    sourceAccount.set(it)
                }
                .onFailure {
                    Log.e(TAG, "getAccount: ${it.message}")
                    messageLiveData.postValue(it.message)
                }
        }
    }

    fun isDuplicateAccount(sourceSN: String, destSN: String): Boolean {
        val isDuplicate = checkDuplicateUseCase.invoke(sourceSN, destSN)
        if (isDuplicate) {
            messageLiveData.postValue(ERROR_MSG_SAME_ACC)
            errorStateLiveData.postValue(ErrorState.SAME_ACC)
        }
        return isDuplicate
    }

    fun checkAmountOK(account: Account): Boolean {
        inputAmountInt?.let {
            val isBalanceEnough: Boolean = checkBalanceEnoughUseCase.invoke(account, it)

            return if (isBalanceEnough) {
                messageLiveData.postValue(ERROR_MSG_BALANCE)
                errorStateLiveData.postValue(ErrorState.BALANCE_NOT_ENOUGH)
                false
            } else {
                messageLiveData.postValue("Amount is OK")
                errorStateLiveData.postValue(ErrorState.NORMAL)
                true
            }
        }
        return false
    }

    fun transferCheck() {
        viewModelScope.launch {
            // 1. is duplicate account?
            val isDuplicate = isDuplicateAccount(
                accountLiveData.value!!.serialNumber,
                inputSerialNumberLiveData.value!!
            )
            Log.e(TAG, "transferCheck: is Duplicate account")

            // 2. is balance enough?
            val amountOK = checkAmountOK(accountLiveData.value!!)
            Log.e(TAG, "transferCheck: amountOK: $amountOK ")

            if (isDuplicate) {
                return@launch
            } else if (!amountOK) {
                return@launch
            } else {
                Log.e(TAG, "transferCheck: go transfer:")
                // 3. go transfer
                errorStateLiveData.postValue(ErrorState.NORMAL)
                goTransfer()
            }
        }
    }

    private fun goTransfer() {
        Log.e(TAG, "goTransfer: ")
        viewModelScope.launch {
            accountLiveData.value?.let { sourceAccount ->
                Log.e(TAG, "goTransfer: sourceAcc: $sourceAccount")
                inputSerialNumberLiveData.value?.let { destSn ->
                    Log.e(TAG, "goTransfer: destSN: $destSn")
                    inputPasswordLiveData.value?.let { destPwd ->
                        Log.e(TAG, "goTransfer: destPwd: $destPwd")
                        inputAmountInt?.let { amount ->
                            Log.e(TAG, "goTransfer: inputAmountInt: $inputAmountInt")
                            transferUseCase.invoke(sourceAccount.serialNumber, destSn, amount)
                                .onSuccess {
                                    Log.e(TAG, "goTransfer: success: money: $it")
                                    messageLiveData.postValue("Success transfer \$ $it")
                                    getAccount(sourceAccount.serialNumber)
                                }
                                .onFailure {
                                    Log.e(TAG, "goTransfer: fail: msg: ${it.message}")
                                    messageLiveData.postValue(it.message)
                                }
                        }
                    }
                }
            }
        }
    }


}