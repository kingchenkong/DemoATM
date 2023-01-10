package com.kck.demoatm.frameworks_devices.ui

import android.content.Context
import android.os.Bundle
import android.view.inputmethod.InputMethodManager
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.lifecycleScope
import com.kck.demoatm.R
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

    }

    private fun initListener() {

    }

    private fun processIntent() {

    }

}