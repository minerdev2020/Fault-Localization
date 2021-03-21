package com.minerdev.faultlocalization.view.activity

import android.os.Bundle
import android.view.MenuItem
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.minerdev.faultlocalization.adapter.SensorDataListAdapter
import com.minerdev.faultlocalization.databinding.ActivityDataHistoryBinding
import com.minerdev.faultlocalization.viewmodel.EquipmentViewModel
import com.minerdev.faultlocalization.viewmodel.factory.EquipmentViewModelFactory

class DataHistoryActivity : AppCompatActivity() {
    private val binding by lazy { ActivityDataHistoryBinding.inflate(layoutInflater) }
    private val adapter by lazy { SensorDataListAdapter(SensorDataListAdapter.DiffCallback()) }
    private val viewModel: EquipmentViewModel by viewModels { EquipmentViewModelFactory(this) }

    private var id = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        supportActionBar?.title = "详细信息"
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        id = intent.getIntExtra("id", 0)
        viewModel.item.observe(this, {
            adapter.submitList(it.sensor_info)
        })
        viewModel.loadItem(id)

        setupRecyclerView()
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            android.R.id.home -> finish()
            else -> {
            }
        }
        return super.onOptionsItemSelected(item)
    }

    private fun setupRecyclerView() {
        val manager = LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)
        binding.recyclerView.layoutManager = manager
        binding.recyclerView.addItemDecoration(
            DividerItemDecoration(
                this,
                DividerItemDecoration.VERTICAL
            )
        )
        binding.recyclerView.adapter = adapter
    }
}