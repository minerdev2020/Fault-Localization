package com.minerdev.faultlocalization.view.activity

import android.os.Bundle
import android.view.MenuItem
import androidx.activity.viewModels
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import com.minerdev.faultlocalization.R
import com.minerdev.faultlocalization.databinding.ActivityPersonModifyBinding
import com.minerdev.faultlocalization.viewmodel.factory.PersonViewModelFactory
import com.minerdev.faultlocalization.model.Person
import com.minerdev.faultlocalization.repository.EquipmentRepository
import com.minerdev.faultlocalization.viewmodel.PersonViewModel

class PersonModifyActivity : AppCompatActivity() {
    private val viewModel: PersonViewModel by viewModels { PersonViewModelFactory(this) }
    private lateinit var binding: ActivityPersonModifyBinding
    private lateinit var person: Person

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_person_modify)
        binding.viewModel = viewModel
        binding.lifecycleOwner = this

        supportActionBar?.title = "修改人员信息"
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        val id = intent.getIntExtra("id", 0)
        viewModel.loadItem(id)
        viewModel.item.observe(this, { person = it })

        binding.btnModify.setOnClickListener {
            person.type_id = if (binding.radioButtonManager.isChecked) 1 else 2
            viewModel.modifyItem(person)
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
        builder.setPositiveButton("确认") { _, _ -> super.finish() }
        builder.setNegativeButton("取消") { dialog, _ -> dialog.dismiss() }
        val alertDialog = builder.create()
        alertDialog.show()
    }
}