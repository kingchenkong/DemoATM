package com.kck.demoatm.interface_adapters.presenters

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.kck.demoatm.application.MyApplication
import com.kck.demoatm.entities.Account
import com.kck.demoatm.use_cases.LoginUseCase
import kotlinx.coroutines.launch

class ChooseFunctionPresenter : ViewModel() {
    private val TAG: String = ChooseFunctionPresenter::class.java.simpleName

    private val loginUseCase: LoginUseCase by lazy { MyApplication().loginUseCase }

    val accountLiveData: MutableLiveData<Account> = MutableLiveData()
    val messageLiveData: MutableLiveData<String> = MutableLiveData()

    var nowAccount = Account.defaultAccount

    val defTextSN: String = "Serial Num:  "
    val textSNLiveData: MutableLiveData<String> = MutableLiveData()
    val defTextPWD: String = "Password:    "
    val textPWDLiveData: MutableLiveData<String> = MutableLiveData()
    val defTextBalance: String = "Balance:     "
    val textBalanceLiveData: MutableLiveData<String> = MutableLiveData()

    fun login(sn: String, pwd: String) {
        viewModelScope.launch {
            loginUseCase.invoke(sn, pwd).onSuccess {
                Log.e(TAG, "login: $it")
                accountLiveData.postValue(it)
                nowAccount = it
                messageLiveData.postValue("Login success.")
            }.onFailure {
                Log.e(TAG, "login: ${it.message}")
                messageLiveData.postValue(it.message)
            }
        }
    }
}