package com.kck.demoatm.interface_adapters.presenters

import android.util.Log
import androidx.databinding.ObservableField
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.kck.demoatm.application.*
import com.kck.demoatm.entities.Account
import com.kck.demoatm.use_cases.DepositUseCase
import com.kck.demoatm.use_cases.LoginUseCase
import com.kck.demoatm.use_cases.TransferUseCase
import com.kck.demoatm.use_cases.WithdrawUseCase
import kotlinx.coroutines.launch

class TransferPresenter : ViewModel() {
    private val TAG: String = TransferPresenter::class.java.simpleName

    private val loginUseCase: LoginUseCase by lazy { MyApplication().loginUseCase }
    private val withdrawUseCase: WithdrawUseCase by lazy { MyApplication().withdrawUseCase }
    private val depositUseCase: DepositUseCase by lazy { MyApplication().depositUseCase }
    private val transferUseCase: TransferUseCase by lazy { MyApplication().transferUseCase }

    enum class ErrorState {
        NORMAL, BALANCE_NOT_ENOUGH, SAME_ACC
    }

    enum class IsMOCKDataState {
        NORMAL, MOCK_1, MOCK_2, MOCK_3, MOCK_4
    }

    val errorStateLiveData = MutableLiveData<ErrorState>()
    val isMockDataStateLiveData = MutableLiveData<IsMOCKDataState>()

    val accountLiveData = MutableLiveData<Account>()
    val messageLiveData = MutableLiveData<String>()

    val inputAmountLiveData = MutableLiveData<String>()
    val inputAmountInt
        get() = inputAmountLiveData.value?.toInt()

    val inputSerialNumberLiveData = MutableLiveData<String>()
    val inputPasswordLiveData = MutableLiveData<String>()

    var sourceAccount: ObservableField<Account> = ObservableField(Account.defaultAccount)
//    var inputAmount: ObservableField<String> = ObservableField("")
//    var inputSerialNum: ObservableField<String> = ObservableField("")
//    var inputPassword: ObservableField<String> = ObservableField("")

    fun login(sn: String, pwd: String) {
        viewModelScope.launch {
            loginUseCase.invoke(sn, pwd).onSuccess {
                Log.e(TAG, "login: $it")
                accountLiveData.postValue(it)
                sourceAccount.set(it)
                checkMockData(it)
            }.onFailure {
                Log.e(TAG, "login: ${it.message}")
                messageLiveData.postValue(it.message)
            }
        }
    }

    fun checkMockData(account: Account) {
        val state = when (account.serialNumber) {
            MOCK_1_ACC_SN -> IsMOCKDataState.MOCK_1
            MOCK_2_ACC_SN -> IsMOCKDataState.MOCK_2
            MOCK_3_ACC_SN -> IsMOCKDataState.MOCK_3
            MOCK_4_ACC_SN -> IsMOCKDataState.MOCK_4
            else -> IsMOCKDataState.NORMAL
        }
        isMockDataStateLiveData.postValue(state)
        Log.e(TAG, "checkMockData: state: $state")
    }

    fun isDuplicateAccount(sourceSN: String, destSN: String): Boolean {
        val isDuplicate = sourceSN == destSN
        if (isDuplicate) {
            messageLiveData.postValue(ERROR_SAME_ACC)
            errorStateLiveData.postValue(ErrorState.SAME_ACC)
        }
        return isDuplicate
    }

    fun checkAmountOK(account: Account): Boolean {
        inputAmountInt?.let {
            return if (it > account.queryBalance()) {
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
                            transferUseCase.invoke(
                                sourceAccount.serialNumber, sourceAccount.password,
                                destSn, destPwd,
                                amount
                            )
                                .onSuccess {
                                    Log.e(TAG, "goTransfer: success: money: $it")
                                    messageLiveData.postValue("Success transfer \$ $it")
                                    login(sourceAccount.serialNumber, sourceAccount.password)
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