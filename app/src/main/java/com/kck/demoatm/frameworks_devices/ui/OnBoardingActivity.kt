package com.kck.demoatm.frameworks_devices.ui

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.widget.doOnTextChanged
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.lifecycleScope
import com.kck.demoatm.R
import com.kck.demoatm.application.*
import com.kck.demoatm.databinding.ActivityOnBoardingBinding
import com.kck.demoatm.interface_adapters.presenters.LoginPresenter

class OnBoardingActivity : AppCompatActivity() {
    private val TAG: String = OnBoardingActivity::class.java.simpleName

    private val myApp by lazy { MyApplication() }

    private lateinit var binding: ActivityOnBoardingBinding

    private val loginPresenter: LoginPresenter by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_on_boarding)
        binding.lifecycleOwner = this

        lifecycleScope.launchWhenStarted {
            initObserver()
            initListener()
        }
    }

    private fun initObserver() {
        loginPresenter.inputSerialNumberLiveData.observe(this) {
            Log.d(TAG, "initObserver: inputSerialNumberLiveData: $it")
        }

        loginPresenter.inputPasswordLiveData.observe(this) {
            Log.d(TAG, "initObserver: inputPasswordLiveData: $it")
        }

        loginPresenter.accountLiveData.observe(this) {
            Log.d(TAG, "initObserver: account: $it")
            lifecycleScope.launchWhenStarted {
                intentToOperateActivity(it.serialNumber, it.password)
            }
        }
        loginPresenter.messageLiveData.observe(this) {
            Log.d(TAG, "initObserver: message: $it")
            lifecycleScope.launchWhenStarted {
                binding.tvResult.text = it
            }
        }
    }

    private fun initListener() {
        binding.editSerial.doOnTextChanged { text, _, _, _ ->
            Log.d(TAG, "initListener: editSerial")
            loginPresenter.inputSerialNumberLiveData.postValue(text.toString())
        }
        binding.editPwd.doOnTextChanged { text, _, _, _ ->
            Log.d(TAG, "initListener: editPwd")
            loginPresenter.inputPasswordLiveData.postValue(text.toString())
        }
        binding.btnLogin.setOnClickListener {
            Log.d(TAG, "initListener: btnLogin")
            loginPresenter.login(
                binding.editSerial.text.toString(),
                binding.editPwd.text.toString()
            )
            this.hideKeyboard(this)
        }
        binding.btnMock1.setOnClickListener {
            Log.d(TAG, "initListener: btnMock1")
            binding.editSerial.setText(MOCK_1_ACC_SN)
            binding.editPwd.setText(MOCK_1_ACC_PWD)
        }
        binding.btnMock2.setOnClickListener {
            Log.d(TAG, "initListener: btnMock2")
            binding.editSerial.setText(MOCK_2_ACC_SN)
            binding.editPwd.setText(MOCK_2_ACC_PWD)
        }
        binding.btnMock3.setOnClickListener {
            Log.d(TAG, "initListener: btnMock3")
            binding.editSerial.setText(MOCK_3_ACC_SN)
            binding.editPwd.setText(MOCK_3_ACC_PWD)
        }
        binding.btnMock4.setOnClickListener {
            Log.d(TAG, "initListener: btnMock4")
            binding.editSerial.setText(MOCK_4_ACC_SN)
            binding.editPwd.setText(MOCK_4_ACC_PWD)
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