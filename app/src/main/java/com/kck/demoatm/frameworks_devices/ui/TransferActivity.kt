package com.kck.demoatm.frameworks_devices.ui

import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.inputmethod.InputMethodManager
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.widget.doOnTextChanged
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.lifecycleScope
import com.kck.demoatm.R
import com.kck.demoatm.application.*
import com.kck.demoatm.databinding.ActivityTransferBinding
import com.kck.demoatm.interface_adapters.presenters.TransferPresenter

class TransferActivity : AppCompatActivity() {
    private val TAG: String = TransferActivity::class.java.simpleName

    private lateinit var binding: ActivityTransferBinding

    //    private val accountViewModel: AccountViewModel by viewModels()
    private val transferPresenter: TransferPresenter by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_transfer)
        binding.lifecycleOwner = this

        lifecycleScope.launchWhenStarted {
            initObserver()
            initListener()

            processIntent()
        }
    }

    private fun initObserver() {
        transferPresenter.accountLiveData.observe(this) {
            Log.e(TAG, "initObserver: account: $it")
            binding.tvSourceSn.text = it.serialNumber
            binding.tvSourceBalance.text = "\$  ${it.queryBalance()}"
        }
        transferPresenter.messageLiveData.observe(this) {
            Log.e(TAG, "initObserver: message: $it")
            binding.tvResult.text = it
        }
        transferPresenter.inputAmountLiveData.observe(this) {
            Log.e(TAG, "initObserver: input amount: $it")
            Log.e(TAG, "initObserver: input amount int: ${transferPresenter.inputAmountInt}")
        }
        transferPresenter.inputSerialNumberLiveData.observe(this) {
            Log.e(TAG, "initObserver: input sn: $it")
        }
        transferPresenter.inputPasswordLiveData.observe(this) {
            Log.e(TAG, "initObserver: input pwd: $it")
        }
        transferPresenter.errorStateLiveData.observe(this) {
            Log.e(TAG, "initObserver: error: $it")
            when (it) {
                TransferPresenter.ErrorState.BALANCE_NOT_ENOUGH -> {
                    binding.editSourceAmount.setText("0")
                }
                else -> {}
            }
        }
    }

    private fun initListener() {
        binding.editSourceAmount.doOnTextChanged { text, start, before, count ->
            transferPresenter.inputAmountLiveData.postValue(text.toString())
        }
        binding.editDestSn.doOnTextChanged { text, start, before, count ->
            transferPresenter.inputSerialNumberLiveData.postValue(text.toString())
        }
        binding.editDestPwd.doOnTextChanged { text, start, before, count ->
            transferPresenter.inputPasswordLiveData.postValue(text.toString())
        }

        binding.btnMock1.setOnClickListener {
            binding.editDestSn.setText(MOCK_1_ACC_SN)
            binding.editDestPwd.setText(MOCK_1_ACC_PWD)
        }
        binding.btnMock2.setOnClickListener {
            binding.editDestSn.setText(MOCK_2_ACC_SN)
            binding.editDestPwd.setText(MOCK_2_ACC_PWD)
        }
        binding.btnMock3.setOnClickListener {
            binding.editDestSn.setText(MOCK_3_ACC_SN)
            binding.editDestPwd.setText(MOCK_3_ACC_PWD)
        }
        binding.btnMock4.setOnClickListener {
            binding.editDestSn.setText(MOCK_4_ACC_SN)
            binding.editDestPwd.setText(MOCK_4_ACC_PWD)
        }
        binding.btnAmountCheck.setOnClickListener {
            hideKeyboard()
            transferPresenter.accountLiveData.value?.let { account ->
                transferPresenter.checkAmountOK(account)
            }
        }
        binding.btnDestCheck.setOnClickListener {
            hideKeyboard()
            Log.e(TAG, "initListener: btnDestCheck: ")
            lifecycleScope.launchWhenStarted {
                transferPresenter.transferCheck()

            }
        }
    }

    private fun processIntent() {
        intent.getBundleExtra("bundle")?.let { bundle ->
            val sn: String = bundle.getString("sn") ?: ""
            Log.e(TAG, "processIntent: sn: $sn")

            transferPresenter.getAccount(sn)
        }
    }

    private fun hideKeyboard() {
        val view = this.currentFocus
        val imm = getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        imm.hideSoftInputFromWindow(view?.windowToken, 0)
    }
}