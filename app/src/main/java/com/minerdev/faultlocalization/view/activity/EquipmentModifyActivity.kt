package com.minerdev.faultlocalization.view.activity

import android.content.DialogInterface
import android.os.Bundle
import android.util.Log
import android.view.MenuItem
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
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
import com.minerdev.faultlocalization.utils.Constants
import com.minerdev.faultlocalization.utils.Constants.CREATE
import com.minerdev.faultlocalization.utils.Constants.UPDATE
import com.minerdev.faultlocalization.viewmodel.EquipmentModifyViewModel
import com.minerdev.faultlocalization.viewmodel.factory.EquipmentModifyViewModelFactory
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.json.Json

class EquipmentModifyActivity : AppCompatActivity() {
    private val viewModel: EquipmentModifyViewModel by viewModels {
        EquipmentModifyViewModelFactory(this)
    }
    private val adapter by lazy { SensorModifyListAdapter(SensorModifyListAdapter.DiffCallback()) }

    private lateinit var binding: ActivityEquipModifyBinding
    private lateinit var mode: String
    private var equipment = Equipment()

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
            viewModel.item.postValue(Equipment(type_id = 1))

        } else if (mode == "modify") {
            supportActionBar?.title = "设备信息"
            binding.btnModify.text = "修改"
            val id = intent.getIntExtra("id", 0)
            viewModel.loadItem(id)
        }

        setupRecyclerView()

        setupViewModel()

        setupButtons()

        binding.spinnerType.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(p0: AdapterView<*>?, p1: View?, p2: Int, p3: Long) {
                equipment.type_id = p2 + 1
                Log.d(Constants.TAG, "equipment type : ${p2 + 1}")
            }

            override fun onNothingSelected(p0: AdapterView<*>?) {
                equipment.type_id = 1
            }
        }
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

        adapter.clickListener = object : SensorModifyListAdapter.OnItemClickListener {
            override fun onItemClick(
                viewHolder: SensorModifyListAdapter.ViewHolder,
                view: View,
                position: Int
            ) {
                val builder = AlertDialog.Builder(this@EquipmentModifyActivity)
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
        }
    }

    private fun setupViewModel() {
        viewModel.item.observe(this, {
            equipment = it
            adapter.parentId = it.id
            adapter.submitList(equipment.sensor_info)
        })

        viewModel.itemTypes.observe(this, {
            val names = ArrayList<String>()
            for (item in it) {
                names.add(item.name)
            }
            val arrayAdapter =
                ArrayAdapter<String>(this, android.R.layout.simple_spinner_dropdown_item)
            arrayAdapter.addAll(names)
            binding.spinnerType.adapter = arrayAdapter
            binding.spinnerType.setSelection(equipment.type_id - 1)
        })

        viewModel.sensorTypes.observe(this, {
            val names = ArrayList<String>()
            for (item in it) {
                names.add(item.name)
            }
            adapter.types.addAll(names)
            adapter.notifyDataSetChanged()
        })

        viewModel.loadItemsStatesAndTypes()
        viewModel.loadSensorStatesAndTypes()
    }

    private fun setupButtons() {
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
                viewModel.addItem(equipment) {
                    val equipment = Json.decodeFromString<Equipment>(it)
                    for (sensor in adapter.currentList) {
                        sensor.parent_id = equipment.id
                        viewModel.addSensor(sensor)
                    }
                }

            } else if (mode == "modify") {
                viewModel.modifyItem(equipment)
                for (sensor in adapter.currentList) {
                    when (sensor.editState) {
                        CREATE -> viewModel.addSensor(sensor)
                        UPDATE -> viewModel.modifySensor(sensor)
                        else -> {
                        }
                    }
                }
                for (sensor in adapter.deleteList) {
                    viewModel.deleteSensor(sensor.id)
                }
            }

            super.finish()
        }

        binding.btnCancel.setOnClickListener { finish() }
    }
}