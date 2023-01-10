package com.kck.demoatm.interface_adapters.presenters

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.kck.demoatm.application.MyApplication
import com.kck.demoatm.entities.Account
import com.kck.demoatm.use_cases.LoginUseCase
import kotlinx.coroutines.launch

class LoginPresenter : ViewModel() {
    private val TAG: String = LoginPresenter::class.java.simpleName

    private val loginUseCase: LoginUseCase by lazy { MyApplication().loginUseCase }

    val inputSerialNumberLiveData: MutableLiveData<String> = MutableLiveData()
    val inputPasswordLiveData: MutableLiveData<String> = MutableLiveData()
    val accountLiveData: MutableLiveData<Account> = MutableLiveData()
    val messageLiveData: MutableLiveData<String> = MutableLiveData()
    val textSNLiveData: MutableLiveData<String> = MutableLiveData()
    val textPWDLiveData: MutableLiveData<String> = MutableLiveData()
    val textBalanceLiveData: MutableLiveData<String> = MutableLiveData()

    fun login(sn: String, pwd: String) {
        viewModelScope.launch {
            loginUseCase.invoke(sn, pwd).onSuccess {
                Log.d(TAG, "login: success: $it")
                accountLiveData.postValue(it)
                messageLiveData.postValue("Login success.")
            }.onFailure {
                Log.e(TAG, "login: fail: ${it.message}")
                messageLiveData.postValue(it.message)
            }
        }
    }

}