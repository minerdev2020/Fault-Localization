package com.minerdev.faultlocalization.view.activity

import android.os.Bundle
import android.view.MenuItem
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import com.minerdev.faultlocalization.adapter.SensorDataPageAdapter
import com.minerdev.faultlocalization.databinding.ActivityDataHistoryBinding
import com.minerdev.faultlocalization.viewmodel.EquipmentViewModel
import com.minerdev.faultlocalization.viewmodel.factory.EquipmentViewModelFactory

class DataHistoryActivity : AppCompatActivity() {
    private val adapter = SensorDataPageAdapter(this)
    private val binding by lazy { ActivityDataHistoryBinding.inflate(layoutInflater) }
    private val viewModel: EquipmentViewModel by viewModels { EquipmentViewModelFactory() }

    private var id = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        supportActionBar?.title = "历史数据"
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        binding.viewPager.adapter = adapter

        id = intent.getIntExtra("id", 0)
        viewModel.item.observe(this, {
            adapter.sensorList.addAll(it.sensor_info)
            adapter.notifyDataSetChanged()
        })
        viewModel.loadItem(id)
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