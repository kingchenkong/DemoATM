package com.kck.demoatm.frameworks_devices.ui

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.lifecycleScope
import com.kck.demoatm.R
import com.kck.demoatm.databinding.ActivityOperateBinding
import com.kck.demoatm.interface_adapters.presenters.LoginPresenter

class OperateActivity : AppCompatActivity() {
    private val TAG: String = OperateActivity::class.java.simpleName

    private lateinit var binding: ActivityOperateBinding

    private val loginPresenter: LoginPresenter by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_operate)
        binding.lifecycleOwner = this

        lifecycleScope.launchWhenStarted {
            initObserver()
            initListener()

            processIntent()
        }
    }

    private fun processIntent() {
        intent.getBundleExtra("bundle")?.let { bundle ->
            val sn: String = bundle.getString("sn") ?: ""
            val pwd = bundle.getString("pwd") ?: ""
            Log.e(TAG, "processIntent: sn: $sn, pwd: $pwd")

            loginPresenter.login(sn, pwd)
        }
    }

    private fun initObserver() {
        loginPresenter.textSNLiveData.observe(this) {
            Log.e(TAG, "initObserver: textSNLiveData: $it")
            binding.tvSn.text = it
        }
        loginPresenter.textPWDLiveData.observe(this) {
            Log.e(TAG, "initObserver: textPWDLiveData: $it")
            binding.tvPwd.text = it
        }
        loginPresenter.textBalanceLiveData.observe(this) {
            Log.e(TAG, "initObserver: textBalanceLiveData: $it")
            binding.tvBalance.text = it
        }
        loginPresenter.accountLiveData.observe(this) {
            Log.e(TAG, "initObserver: account: $it")
            binding.tvAccount.text = it.toString()

            loginPresenter.textSNLiveData.postValue("${loginPresenter.defTextSN}${it.serialNumber}")
            loginPresenter.textPWDLiveData.postValue("${loginPresenter.defTextPWD}${it.password}")
            loginPresenter.textBalanceLiveData.postValue("${loginPresenter.defTextBalance}${it.queryBalance()}")
        }
        loginPresenter.messageLiveData.observe(this) {
            Log.e(TAG, "initObserver: message: $it")
//            binding.tvMessage.text = it.toString()
        }
    }

    private fun initListener() {
        binding.btnWithdraw.setOnClickListener {
//            loginPresenter.withdraw(
//                loginPresenter.nowAccount,
//                binding.editMoney.text.toString().toInt()
//            )
        }

        binding.btnDeposit.setOnClickListener {
//            loginPresenter.deposit(
//                loginPresenter.nowAccount,
//                binding.editMoney.text.toString().toInt()
//            )
        }

        binding.btnQuery.setOnClickListener {
//            loginPresenter.queryBalance(loginPresenter.nowAccount)
        }

        binding.btnTransferPage.setOnClickListener {
            loginPresenter.nowAccount.let {
                intentToTransfer(it.serialNumber, it.password)
            }
        }

    }

    private fun intentToTransfer(
        serialNumber: String,
        password: String,
    ) {
        val bundle = Bundle()
        bundle.putString("sn", serialNumber)
        bundle.putString("pwd", password)
        val intent = Intent(this@OperateActivity, TransferActivity::class.java)
        intent.putExtra("bundle", bundle)
        startActivity(intent)
    }


}