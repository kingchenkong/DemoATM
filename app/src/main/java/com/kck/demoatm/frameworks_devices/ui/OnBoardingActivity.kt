package com.kck.demoatm.frameworks_devices.ui

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.lifecycleScope
import com.kck.demoatm.R
import com.kck.demoatm.application.*
import com.kck.demoatm.databinding.ActivityOnBoardingBinding
import com.kck.demoatm.interface_adapters.presenters.LoginPresenter

class OnBoardingActivity : AppCompatActivity() {
    private val TAG: String = OnBoardingActivity::class.java.simpleName
    private val presenter: LoginPresenter by viewModels()

    private lateinit var binding: ActivityOnBoardingBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = DataBindingUtil.setContentView(this, R.layout.activity_on_boarding)
        binding.lifecycleOwner = this
        binding.presenter = presenter

        lifecycleScope.launchWhenStarted {
            initObserver()
            initListener()
        }
    }

    override fun onResume() {
        super.onResume()

        lifecycleScope.launchWhenResumed {
            presenter.inputSerialNumberLiveData.postValue("")
            presenter.inputPasswordLiveData.postValue("")
            presenter.messageLiveData.postValue("")
            binding.editSerial.requestFocus()
            this@OnBoardingActivity.displayKeyboard(this@OnBoardingActivity)
        }
    }

    private fun initObserver() {
        presenter.inputSerialNumberLiveData.observe(this) {
            Log.d(TAG, "initObserver: inputSerialNumberLiveData: $it")
        }

        presenter.inputPasswordLiveData.observe(this) {
            Log.d(TAG, "initObserver: inputPasswordLiveData: $it")
        }

        presenter.accountLiveData.observe(this) {
            Log.d(TAG, "initObserver: account: $it")
            lifecycleScope.launchWhenStarted {
                intentToOperateActivity(it.serialNumber, it.password)
            }
        }
        presenter.messageLiveData.observe(this) {
            Log.d(TAG, "initObserver: message: $it")
        }
    }

    private fun initListener() {
        binding.btnLogin.setOnClickListener {
            Log.d(TAG, "initListener: btnLogin")
            presenter.inputSerialNumberLiveData.value?.let { sn ->
                presenter.inputPasswordLiveData.value?.let { pwd ->
                    presenter.login(sn, pwd)
                }
            }
            this.hideKeyboard(this)
        }
        binding.btnMock1.setOnClickListener {
            Log.d(TAG, "initListener: btnMock1")
            presenter.inputSerialNumberLiveData.postValue(MOCK_1_ACC_SN)
            presenter.inputPasswordLiveData.postValue(MOCK_1_ACC_PWD)
        }
        binding.btnMock2.setOnClickListener {
            Log.d(TAG, "initListener: btnMock2")
            presenter.inputSerialNumberLiveData.postValue(MOCK_2_ACC_SN)
            presenter.inputPasswordLiveData.postValue(MOCK_2_ACC_PWD)
        }
        binding.btnMock3.setOnClickListener {
            Log.d(TAG, "initListener: btnMock3")
            presenter.inputSerialNumberLiveData.postValue(MOCK_3_ACC_SN)
            presenter.inputPasswordLiveData.postValue(MOCK_3_ACC_PWD)
        }
        binding.btnMock4.setOnClickListener {
            Log.d(TAG, "initListener: btnMock4")
            presenter.inputSerialNumberLiveData.postValue(MOCK_4_ACC_SN)
            presenter.inputPasswordLiveData.postValue(MOCK_4_ACC_PWD)
        }
    }

    private fun intentToOperateActivity(
        serialNumber: String,
        password: String,
    ) {
        Log.d(TAG, "intentToOperateActivity: sn: $serialNumber, pwd: $password")
        val bundle = Bundle()
        bundle.putString("sn", serialNumber)
        bundle.putString("pwd", password)
        val intent = Intent(this@OnBoardingActivity, ChooseFunctionsActivity::class.java)
        intent.putExtra("bundle", bundle)
        startActivity(intent)
    }
}