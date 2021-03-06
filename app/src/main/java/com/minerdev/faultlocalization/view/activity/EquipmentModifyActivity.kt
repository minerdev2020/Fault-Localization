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
import com.minerdev.faultlocalization.factory.EquipmentViewModelFactory
import com.minerdev.faultlocalization.model.Equipment
import com.minerdev.faultlocalization.model.EquipmentState
import com.minerdev.faultlocalization.model.EquipmentType
import com.minerdev.faultlocalization.viewmodel.EquipmentViewModel

class EquipmentModifyActivity : AppCompatActivity() {
    private val viewModel: EquipmentViewModel by viewModels { EquipmentViewModelFactory() }
    private val adapter by lazy { SensorModifyListAdapter(SensorModifyListAdapter.DiffCallback()) }

    private lateinit var binding: ActivityEquipModifyBinding
    private lateinit var equipment: Equipment

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_equip_modify)
        binding.viewModel = viewModel
        binding.lifecycleOwner = this

        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        val mode = intent.getStringExtra("mode") ?: ""
        if (mode == "add") {
            supportActionBar?.title = "添加设备信息"
            binding.btnModify.text = "添加"

        } else if (mode == "modify") {
            supportActionBar?.title = "设备信息"
            binding.btnModify.text = "修改"
        }

        val id = intent.getIntExtra("id", 0)
        if (id > 0) {
            viewModel.loadItem(id)

        } else {
            viewModel.item.postValue(
                Equipment(
                    EquipmentState = EquipmentState(),
                    EquipmentType = EquipmentType(),
                    Sensor = ArrayList()
                )
            )
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
            builder.setPositiveButton(
                "确认",
                DialogInterface.OnClickListener { _, _ ->
                    adapter.removeItem(position)
                    return@OnClickListener
                })
            builder.setNegativeButton(
                "取消",
                DialogInterface.OnClickListener { _, _ -> return@OnClickListener })
            val alertDialog = builder.create()
            alertDialog.show()
        }

        viewModel.item.observe(this, {
            equipment = it
            adapter.submitList(equipment.Sensor)
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
            viewModel.modifyItems(equipment)
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