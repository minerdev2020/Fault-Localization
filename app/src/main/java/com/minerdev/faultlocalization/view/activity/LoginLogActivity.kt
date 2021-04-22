package com.minerdev.faultlocalization.view.activity

import android.os.Bundle
import android.view.MenuItem
import androidx.activity.viewModels
import com.minerdev.faultlocalization.base.BaseActivity
import com.minerdev.faultlocalization.databinding.ActivityLoginLogBinding
import com.minerdev.faultlocalization.utils.Constants.ID
import com.minerdev.faultlocalization.viewmodel.LoginLogViewModel

class LoginLogActivity : BaseActivity() {
    private val binding by lazy { ActivityLoginLogBinding.inflate(layoutInflater) }
    private val viewModel: LoginLogViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        viewModel.logs.observe(this, {
            binding.tvLoginLog.text = it
        })
        viewModel.loadLoginLogs(ID.toInt())
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            android.R.id.home -> finish()
            else -> {
            }
        }
        return super.onOptionsItemSelected(item)
    }
}