package com.kck.demoatm.frameworks_devices.ui

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.lifecycleScope
import com.kck.demoatm.R
import com.kck.demoatm.databinding.ActivityChooseFunctionsBinding
import com.kck.demoatm.interface_adapters.presenters.ChooseFunctionPresenter

class ChooseFunctionsActivity : AppCompatActivity() {
    private val TAG: String = ChooseFunctionsActivity::class.java.simpleName

    private val presenter: ChooseFunctionPresenter by viewModels()
    private lateinit var binding: ActivityChooseFunctionsBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_choose_functions)
        binding.lifecycleOwner = this
        binding.presenter = presenter

        lifecycleScope.launchWhenStarted {
            initObserver()
            initListener()

            processIntent()
        }
    }

    override fun onResume() {
        super.onResume()

        lifecycleScope.launchWhenResumed {
            presenter.accountLiveData.value?.let {
                presenter.login(it.serialNumber, it.password)
            }
        }
    }

    private fun processIntent() {
        intent.getBundleExtra("bundle")?.let { bundle ->
            val sn: String = bundle.getString("sn") ?: ""
            val pwd = bundle.getString("pwd") ?: ""
            Log.d(TAG, "processIntent: sn: $sn, pwd: $pwd")
            presenter.login(sn, pwd)
        }
    }

    private fun initObserver() {
        presenter.textSNLiveData.observe(this) {
            Log.d(TAG, "initObserver: textSNLiveData: $it")
        }
        presenter.textPWDLiveData.observe(this) {
            Log.d(TAG, "initObserver: textPWDLiveData: $it")
        }
        presenter.textBalanceLiveData.observe(this) {
            Log.d(TAG, "initObserver: textBalanceLiveData: $it")
        }
        presenter.accountLiveData.observe(this) {
            Log.d(TAG, "initObserver: account: $it")
            binding.tvAccount.text = it.toString()

            presenter.textSNLiveData.postValue("${presenter.defTextSN}${it.serialNumber}")
            presenter.textPWDLiveData.postValue("${presenter.defTextPWD}${it.password}")
            presenter.textBalanceLiveData.postValue("${presenter.defTextBalance}${it.queryBalance()}")
        }
        presenter.messageLiveData.observe(this) {
            Log.d(TAG, "initObserver: message: $it")
        }
    }

    private fun initListener() {
        binding.btnWithdraw.setOnClickListener {
            Log.d(TAG, "initListener: btnWithdraw")
            intentToWithdraw(presenter.nowAccount.serialNumber)
        }

        binding.btnDeposit.setOnClickListener {
            Log.d(TAG, "initListener: btnDeposit")
            intentToDeposit(presenter.nowAccount.serialNumber)
        }

        binding.btnTransferPage.setOnClickListener {
            Log.d(TAG, "initListener: btnTransferPage")
            intentToTransfer(presenter.nowAccount.serialNumber)
        }
    }

    private fun intentToWithdraw(
        serialNumber: String
    ) {
        Log.d(TAG, "intentToWithdraw: $serialNumber")
        val bundle = Bundle()
        bundle.putString("sn", serialNumber)
        val intent = Intent(this@ChooseFunctionsActivity, WithdrawActivity::class.java)
        intent.putExtra("bundle", bundle)
        startActivity(intent)
    }

    private fun intentToDeposit(
        serialNumber: String
    ) {
        Log.d(TAG, "intentToDeposit: $serialNumber")
        val bundle = Bundle()
        bundle.putString("sn", serialNumber)
        val intent = Intent(this@ChooseFunctionsActivity, DepositActivity::class.java)
        intent.putExtra("bundle", bundle)
        startActivity(intent)
    }

    private fun intentToTransfer(
        serialNumber: String,
    ) {
        Log.d(TAG, "intentToTransfer: $serialNumber")
        val bundle = Bundle()
        bundle.putString("sn", serialNumber)
        val intent = Intent(this@ChooseFunctionsActivity, TransferActivity::class.java)
        intent.putExtra("bundle", bundle)
        startActivity(intent)
    }
}