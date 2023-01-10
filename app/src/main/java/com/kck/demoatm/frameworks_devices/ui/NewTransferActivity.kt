package com.kck.demoatm.frameworks_devices.ui

import android.os.Bundle
import android.util.Log
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.widget.doOnTextChanged
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.lifecycleScope
import com.kck.demoatm.R
import com.kck.demoatm.application.*
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
            Log.d(TAG, "initObserver: account: $it")
            binding.tvSourceSn.text = it.serialNumber
            binding.tvSourceBalance.text = "\$  ${it.queryBalance()}"
        }
        presenter.messageLiveData.observe(this) {
            Log.d(TAG, "initObserver: message: $it")
            binding.tvResult.text = it
        }
        presenter.inputAmountLiveData.observe(this) {
            Log.d(TAG, "initObserver: input amount: $it")
            Log.d(TAG, "initObserver: input amount int: ${presenter.inputAmountInt}")
        }
    }

    private fun initListener() {
        binding.editSourceAmount.doOnTextChanged { text, _, _, _ ->
            Log.d(TAG, "initListener: editSourceAmount")
            presenter.inputAmountLiveData.postValue(text.toString())
        }

        binding.editDestSn.doOnTextChanged { text, _, _, _ ->
            Log.d(TAG, "initListener: editDestSn")
            presenter.inputDestSNLiveData.postValue(text.toString())
        }

        binding.btnMock1.setOnClickListener {
            Log.d(TAG, "initListener: btnMock1")
            binding.editDestSn.setText(MOCK_1_ACC_SN)
        }
        binding.btnMock2.setOnClickListener {
            Log.d(TAG, "initListener: btnMock2")
            binding.editDestSn.setText(MOCK_2_ACC_SN)
        }
        binding.btnMock3.setOnClickListener {
            Log.d(TAG, "initListener: btnMock3")
            binding.editDestSn.setText(MOCK_3_ACC_SN)
        }
        binding.btnMock4.setOnClickListener {
            Log.d(TAG, "initListener: btnMock4")
            binding.editDestSn.setText(MOCK_4_ACC_SN)
        }

        binding.btnAmountCheck.setOnClickListener {
            Log.d(TAG, "initListener: btnAmountCheck")
            this.hideKeyboard(this)
            presenter.checkAmountOK(
                presenter.sourceAccount.queryBalance(),
                presenter.inputAmountInt
            )
        }
        binding.btnDestCheck.setOnClickListener {
            Log.d(TAG, "initListener: btnDestCheck")
            this.hideKeyboard(this)
            presenter.checkDestDuplicate(
                presenter.sourceAccount.serialNumber,
                presenter.destSNText
            )
        }

        binding.btnTransfer.setOnClickListener {
            Log.d(TAG, "initListener: btnTransfer")
            this.hideKeyboard(this)
            presenter.transfer(
                presenter.sourceAccount,
                presenter.destSNText,
                presenter.inputAmountInt
            )
        }
    }

    private fun processIntent() {
        intent.getBundleExtra("bundle")?.let { bundle ->
            val sn: String = bundle.getString("sn") ?: ""
            Log.d(TAG, "processIntent: sn: $sn")
            presenter.getAccount(sn)
        }
    }

}