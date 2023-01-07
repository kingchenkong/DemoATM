package com.kck.demoatm.interface_adapters.presenters

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.kck.demoatm.application.MyApplication
import com.kck.demoatm.entities.Account
import com.kck.demoatm.use_cases.LoginUseCase
import kotlinx.coroutines.launch

class AccountViewModel : ViewModel() {
    private val TAG: String = AccountViewModel::class.java.simpleName

    private val loginUseCase: LoginUseCase by lazy { MyApplication().loginUseCase }

    // livedata
    val accountLiveData = MutableLiveData<Account>()

    fun login(sn: String, pwd: String) {
        viewModelScope.launch {
            loginUseCase.invoke(sn, pwd).onSuccess {
                Log.e(TAG, "login: $it")
            }.onFailure {
                Log.e(TAG, "login: ${it.message}")
            }
        }
    }


}