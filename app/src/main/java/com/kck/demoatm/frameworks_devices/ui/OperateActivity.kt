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
import com.kck.demoatm.interface_adapters.presenters.AccountViewModel
import org.koin.android.ext.android.bind

class OperateActivity : AppCompatActivity() {
    private val TAG: String = OperateActivity::class.java.simpleName

    private lateinit var binding: ActivityOperateBinding

    private val accountViewModel: AccountViewModel by viewModels()

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

            accountViewModel.login(sn, pwd)
        }
    }

    private fun initObserver() {
        accountViewModel.accountLiveData.observe(this) {
            Log.e(TAG, "initObserver: account: $it")
            binding.tvAccount.text = it.toString()
        }
        accountViewModel.messageLiveData.observe(this) {
            Log.e(TAG, "initObserver: message: $it")
            binding.tvMessage.text = it.toString()
        }
    }

    private fun initListener() {
        binding.btnWithdraw.setOnClickListener {
            accountViewModel.withdraw(
                accountViewModel.nowAccount,
                binding.editMoney.text.toString().toInt()
            )
        }

        binding.btnDeposit.setOnClickListener {
            accountViewModel.deposit(
                accountViewModel.nowAccount,
                binding.editMoney.text.toString().toInt()
            )
        }

        binding.btnQuery.setOnClickListener {
            accountViewModel.queryBalance(accountViewModel.nowAccount)
        }

        binding.btnTransferPage.setOnClickListener {
            accountViewModel.nowAccount.let {
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