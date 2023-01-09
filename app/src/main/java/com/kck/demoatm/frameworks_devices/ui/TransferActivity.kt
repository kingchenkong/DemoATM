package com.kck.demoatm.frameworks_devices.ui

import android.os.Bundle
import android.util.Log
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.lifecycleScope
import com.kck.demoatm.R
import com.kck.demoatm.databinding.ActivityTransferBinding
import com.kck.demoatm.interface_adapters.presenters.AccountViewModel

class TransferActivity : AppCompatActivity() {
    private val TAG: String = TransferActivity::class.java.simpleName

    private lateinit var binding: ActivityTransferBinding

    private val accountViewModel: AccountViewModel by viewModels()

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
        accountViewModel.accountLiveData.observe(this) {
            Log.e(TAG, "initObserver: account: $it")
            binding.tvAccount.text = it.toString()
        }

    }

    private fun initListener() {

    }

    private fun processIntent() {
        intent.getBundleExtra("bundle")?.let { bundle ->
            val sn: String = bundle.getString("sn") ?: ""
            val pwd = bundle.getString("pwd") ?: ""
            Log.e(TAG, "processIntent: sn: $sn, pwd: $pwd")

            accountViewModel.login(sn, pwd)
        }
    }


}