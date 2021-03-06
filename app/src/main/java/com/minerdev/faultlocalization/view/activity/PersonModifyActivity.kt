package com.minerdev.faultlocalization.view.activity

import android.content.DialogInterface
import android.os.Bundle
import android.view.MenuItem
import androidx.activity.viewModels
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import com.minerdev.faultlocalization.R
import com.minerdev.faultlocalization.databinding.ActivityPersonModifyBinding
import com.minerdev.faultlocalization.factory.PersonViewModelFactory
import com.minerdev.faultlocalization.model.Person
import com.minerdev.faultlocalization.viewmodel.PersonViewModel
import kotlinx.serialization.InternalSerializationApi

class PersonModifyActivity : AppCompatActivity() {
    private val viewModel: PersonViewModel by viewModels { PersonViewModelFactory() }
    private lateinit var binding: ActivityPersonModifyBinding
    private lateinit var person: Person

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_person_modify)
        binding.viewModel = viewModel
        binding.lifecycleOwner = this

        supportActionBar?.title = "人员信息修改"
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        val id = intent.getIntExtra("id", 0)
        viewModel.loadItem(id)
        viewModel.item.observe(this, { person = it })

        binding.btnModify.setOnClickListener {
            person.type_id = if (binding.radioButtonManager.isChecked) 1 else 2
            viewModel.modifyItems(person)
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