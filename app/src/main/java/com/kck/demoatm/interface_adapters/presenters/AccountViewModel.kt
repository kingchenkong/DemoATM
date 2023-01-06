package com.kck.demoatm.interface_adapters.presenters

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.kck.demoatm.entities.Account
import com.kck.demoatm.use_cases.GetAccountUseCase

class AccountViewModel : ViewModel() {
    private val TAG: String = AccountViewModel::class.java.simpleName


    private val getAccountUseCase: GetAccountUseCase by lazy { GetAccountUseCase() }

    // livedata
    val accountLiveData = MutableLiveData<Account>()

    fun getDefaultAccount() {

    }

}