package com.minerdev.faultlocalization.view.activity

import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.View
import androidx.activity.viewModels
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.minerdev.faultlocalization.R
import com.minerdev.faultlocalization.adapter.SensorDataListAdapter
import com.minerdev.faultlocalization.adapter.SensorModifyListAdapter
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
            adapter.submitList(it.Sensors)
        })
        viewModel.loadItem(id)

        setupRecyclerView()
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        val menuInflater = menuInflater
        menuInflater.inflate(R.menu.menu_data_history_toolbar, menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            android.R.id.home -> finish()
            R.id.toolbar_menu_delete -> {
                val builder = AlertDialog.Builder(this)
                builder.setTitle("友情提示")
                builder.setIcon(R.drawable.ic_round_warning_24)
                builder.setMessage("您真的要删除吗？")
                builder.setPositiveButton("确认") { _, _ ->
                    viewModel.deleteItem(id)
                    finish()
                }
                builder.setNegativeButton("取消") { _, _ ->
                    return@setNegativeButton
                }

                val alertDialog = builder.create()
                alertDialog.show()
            }
            R.id.toolbar_menu_modify -> {
                val intent = Intent(this, EquipmentModifyActivity::class.java)
                intent.putExtra("id", id)
                intent.putExtra("mode", "modify")
                startActivity(intent)
            }
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