package com.kck.demoatm.frameworks_devices.ui

import android.os.Bundle
import android.util.Log
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.lifecycleScope
import com.kck.demoatm.R
import com.kck.demoatm.application.hideKeyboard
import com.kck.demoatm.databinding.ActivityWithdrawBinding
import com.kck.demoatm.interface_adapters.presenters.WithdrawPresenter

class WithdrawActivity : AppCompatActivity() {
    private val TAG: String = WithdrawActivity::class.java.simpleName
    private val presenter: WithdrawPresenter by viewModels()

    private lateinit var binding: ActivityWithdrawBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_withdraw)
        binding.lifecycleOwner = this
        binding.presenter = presenter

        lifecycleScope.launchWhenStarted {
            initObserver()
            initListener()

            processIntent()
        }
    }

    private fun initObserver() {
        presenter.accountLiveData.observe(this) {
            Log.d(TAG, "initObserver: accountLiveData: $it")
            presenter.serialNumberLiveData.postValue(it.serialNumber)
            presenter.balanceLiveData.postValue(it.queryBalance().toString())
        }
        presenter.messageLiveData.observe(this) {
            Log.d(TAG, "initObserver: messageLiveData: $it")
        }
        presenter.inputAmountLiveData.observe(this) {
            Log.d(TAG, "initObserver: inputAmountLiveData: $it")
        }
    }

    private fun initListener() {
        binding.btnDeposit.setOnClickListener {
            Log.d(TAG, "initListener: btnDeposit:")
            this@WithdrawActivity.hideKeyboard(this@WithdrawActivity)
            presenter.withdraw()
        }
    }

    private fun processIntent() {
        intent.getBundleExtra("bundle")?.let { bundle ->
            val sn: String = bundle.getString("sn") ?: ""
            val pwd = bundle.getString("pwd") ?: ""
            Log.d(TAG, "processIntent: sn: $sn, pwd: $pwd")
            presenter.getAccount(sn)
        }
    }
}