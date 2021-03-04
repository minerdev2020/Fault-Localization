package com.minerdev.faultlocalization.view.activity

import android.content.DialogInterface
import android.os.Bundle
import android.view.MenuItem
import androidx.activity.viewModels
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import com.minerdev.faultlocalization.R
import com.minerdev.faultlocalization.databinding.ActivityEquipModifyBinding
import com.minerdev.faultlocalization.factory.EquipmentViewModelFactory
import com.minerdev.faultlocalization.model.Equipment
import com.minerdev.faultlocalization.model.EquipmentState
import com.minerdev.faultlocalization.model.EquipmentType
import com.minerdev.faultlocalization.viewmodel.EquipmentViewModel
import kotlinx.serialization.InternalSerializationApi

class EquipmentModifyActivity : AppCompatActivity() {
    private val viewModel: EquipmentViewModel by viewModels { EquipmentViewModelFactory() }
    private lateinit var binding: ActivityEquipModifyBinding
    private lateinit var equipment: Equipment

    @InternalSerializationApi
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_equip_modify)
        binding.viewModel = viewModel
        binding.lifecycleOwner = this

        supportActionBar?.title = "人员信息修改"
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        val id = intent.getIntExtra("id", 0)
        if (id > 0) {
            viewModel.loadItem(id)

        } else {
            viewModel.item.postValue(
                Equipment(
                    EquipmentState = EquipmentState(),
                    EquipmentType = EquipmentType()
                )
            )
        }

        viewModel.item.observe(this, { equipment = it })

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