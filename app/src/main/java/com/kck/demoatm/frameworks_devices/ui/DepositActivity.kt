package com.kck.demoatm.frameworks_devices.ui

import android.content.Context
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.inputmethod.InputMethodManager
import androidx.activity.viewModels
import androidx.core.widget.doOnTextChanged
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.lifecycleScope
import com.kck.demoatm.R
import com.kck.demoatm.application.hideKeyboard
import com.kck.demoatm.databinding.ActivityDepositBinding
import com.kck.demoatm.interface_adapters.presenters.DepositPresenter

class DepositActivity : AppCompatActivity() {
    private val TAG: String = DepositActivity::class.java.simpleName
    private val presenter: DepositPresenter by viewModels()

    private lateinit var binding: ActivityDepositBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_deposit)
        binding.lifecycleOwner = this

        lifecycleScope.launchWhenStarted {
            initObserver()
            initListener()

            processIntent()
        }
    }

    private fun initObserver() {
        presenter.accountLiveData.observe(this) {
            Log.d(TAG, "initObserver: accountLiveData: $it")
            binding.tvSn.text = it.serialNumber
            binding.tvBalance.text = it.queryBalance().toString()
        }
    }

    private fun initListener() {
        binding.editAmount.doOnTextChanged { text, _, _, _ ->
            Log.d(TAG, "initListener: editAmount: $text")
            presenter.inputAmountLiveData.postValue(text.toString())
        }
        binding.btnDeposit.setOnClickListener {
            Log.d(TAG, "initListener: btnDeposit")
            this.hideKeyboard(this)
            presenter.deposit(presenter.nowAccount, presenter.inputAmountInt)
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