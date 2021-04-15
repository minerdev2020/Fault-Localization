package com.minerdev.faultlocalization.view.activity

import android.os.Bundle
import android.view.MenuItem
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.minerdev.faultlocalization.adapter.LoginLogListAdapter
import com.minerdev.faultlocalization.databinding.ActivityLoginLogBinding
import com.minerdev.faultlocalization.utils.Constants.ID
import com.minerdev.faultlocalization.viewmodel.LoginLogViewModel

class LoginLogActivity : AppCompatActivity() {
    private val binding by lazy { ActivityLoginLogBinding.inflate(layoutInflater) }
    private val adapter by lazy { LoginLogListAdapter(LoginLogListAdapter.DiffCallback()) }
    private val viewModel: LoginLogViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        viewModel.logs.observe(this, adapter::submitList)

        val manager = LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)
        binding.recyclerView.layoutManager = manager
        binding.recyclerView.adapter = adapter
        viewModel.loadLogs(ID.toInt())
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