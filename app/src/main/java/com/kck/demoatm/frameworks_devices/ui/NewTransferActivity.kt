package com.kck.demoatm.frameworks_devices.ui

import android.os.Bundle
import android.util.Log
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.widget.doOnTextChanged
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.lifecycleScope
import com.kck.demoatm.R
import com.kck.demoatm.application.hideKeyboard
import com.kck.demoatm.databinding.ActivityNewTransferBinding
import com.kck.demoatm.interface_adapters.presenters.NewTransferPresenter

class NewTransferActivity : AppCompatActivity() {
    private val TAG: String = NewTransferActivity::class.java.simpleName

    private val presenter: NewTransferPresenter by viewModels()

    private lateinit var binding: ActivityNewTransferBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_new_transfer)
        binding.lifecycleOwner = this

        lifecycleScope.launchWhenStarted {
            initObserver()
            initListener()

            processIntent()
        }
    }

    private fun initObserver() {
        presenter.sourceAccountLiveData.observe(this) {
            Log.e(TAG, "initObserver: account: $it")
            binding.tvSourceSn.text = it.serialNumber
            binding.tvSourceBalance.text = "\$  ${it.queryBalance()}"
        }
        presenter.messageLiveData.observe(this) {
            Log.e(TAG, "initObserver: message: $it")
//            binding.tvResult.text = it
        }
        presenter.inputAmountLiveData.observe(this) {
            Log.e(TAG, "initObserver: input amount: $it")
            Log.e(TAG, "initObserver: input amount int: ${presenter.inputAmountInt}")
        }
    }

    private fun initListener() {
        binding.editSourceAmount.doOnTextChanged { text, start, before, count ->
            presenter.inputAmountLiveData.postValue(text.toString())
        }

        binding.btnAmountCheck.setOnClickListener {
            this.hideKeyboard(this)
            presenter.checkAmountOK()
        }
    }

    private fun processIntent() {
        intent.getBundleExtra("bundle")?.let { bundle ->
            val sn: String = bundle.getString("sn") ?: ""
            Log.e(TAG, "processIntent: sn: $sn")
            presenter.getAccount(sn)
        }
    }

}