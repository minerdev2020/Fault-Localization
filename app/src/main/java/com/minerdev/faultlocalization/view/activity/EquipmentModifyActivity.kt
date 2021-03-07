package com.minerdev.faultlocalization.view.activity

import android.content.DialogInterface
import android.os.Bundle
import android.view.MenuItem
import android.view.View
import androidx.activity.viewModels
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.minerdev.faultlocalization.R
import com.minerdev.faultlocalization.adapter.SensorModifyListAdapter
import com.minerdev.faultlocalization.databinding.ActivityEquipModifyBinding
import com.minerdev.faultlocalization.model.Equipment
import com.minerdev.faultlocalization.model.EquipmentState
import com.minerdev.faultlocalization.model.EquipmentType
import com.minerdev.faultlocalization.utils.Constants.CREATE
import com.minerdev.faultlocalization.utils.Constants.DELETE
import com.minerdev.faultlocalization.utils.Constants.UPDATE
import com.minerdev.faultlocalization.viewmodel.EquipmentModifyViewModel
import com.minerdev.faultlocalization.viewmodel.factory.EquipmentModifyViewModelFactory
import com.minerdev.faultlocalization.viewmodel.factory.EquipmentViewModelFactory

class EquipmentModifyActivity : AppCompatActivity() {
    private val viewModel: EquipmentModifyViewModel by viewModels { EquipmentModifyViewModelFactory(this) }
    private val adapter by lazy { SensorModifyListAdapter(SensorModifyListAdapter.DiffCallback()) }

    private lateinit var binding: ActivityEquipModifyBinding
    private lateinit var equipment: Equipment
    private lateinit var mode: String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_equip_modify)
        binding.viewModel = viewModel
        binding.lifecycleOwner = this

        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        mode = intent.getStringExtra("mode") ?: ""
        if (mode == "add") {
            supportActionBar?.title = "添加设备信息"
            binding.btnModify.text = "添加"

            viewModel.item.postValue(
                Equipment(
                    EquipmentState = EquipmentState(),
                    EquipmentType = EquipmentType(),
                    Sensors = ArrayList()
                )
            )

        } else if (mode == "modify") {
            supportActionBar?.title = "设备信息"
            binding.btnModify.text = "修改"
            val id = intent.getIntExtra("id", 0)
            viewModel.loadItem(id)
        }

        val manager = LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)
        binding.recyclerView.layoutManager = manager
        binding.recyclerView.addItemDecoration(
            DividerItemDecoration(
                this,
                DividerItemDecoration.VERTICAL
            )
        )
        binding.recyclerView.adapter = adapter

        adapter.listener = { _: SensorModifyListAdapter.ViewHolder?,
                             _: View?,
                             position: Int ->
            val builder = AlertDialog.Builder(this)
            builder.setTitle("友情提示")
            builder.setMessage("您真的要删除吗？")
            builder.setIcon(R.drawable.ic_round_warning_24)
            builder.setPositiveButton("确认") { _, _ ->
                adapter.removeItem(position)
            }
            builder.setNegativeButton("取消") { _, _ ->
                return@setNegativeButton
            }
            val alertDialog = builder.create()
            alertDialog.show()
        }

        viewModel.item.observe(this, {
            equipment = it
            adapter.submitList(equipment.Sensors)
        })

        binding.materialBtnAdd.setOnClickListener {
            adapter.addEmptyItem()
        }

        binding.btnModify.setOnClickListener {
            when (binding.radioGroupState.checkedRadioButtonId) {
                R.id.radioButton_running -> equipment.state_id = 1
                R.id.radioButton_under_repair -> equipment.state_id = 2
                R.id.radioButton_stoped -> equipment.state_id = 3
                else -> {
                }
            }

            if (mode == "add") {
                viewModel.addItem(equipment)
                for (sensor in equipment.Sensors) {
                    viewModel.addSensor(sensor)
                }

            } else if (mode == "modify") {
                viewModel.modifyItem(equipment)
                for (sensor in equipment.Sensors) {
                    when (sensor.state) {
                        CREATE -> viewModel.addSensor(sensor)
                        UPDATE -> viewModel.modifySensor(sensor)
                        DELETE -> viewModel.deleteSensor(sensor.id)
                        else -> {
                        }
                    }
                }
            }

            super.finish()
        }

        binding.btnCancel.setOnClickListener { finish() }
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            android.R.id.home -> finish()
            else -> {
            }
        }
        return super.onOptionsItemSelected(item)
    }

    override fun finish() {
        val builder = AlertDialog.Builder(this)
        builder.setTitle("友情提示")
        builder.setIcon(R.drawable.ic_round_warning_24)
        builder.setMessage("您确认不保存所输入内容吗？")
        builder.setPositiveButton("确认") { _: DialogInterface?, _: Int -> super.finish() }
        builder.setNegativeButton("取消") { dialog: DialogInterface, _: Int -> dialog.dismiss() }
        val alertDialog = builder.create()
        alertDialog.show()
    }
}