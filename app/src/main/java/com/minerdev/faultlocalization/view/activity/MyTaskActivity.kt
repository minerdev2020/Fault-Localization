package com.minerdev.faultlocalization.view.activity

import android.os.Bundle
import android.view.MenuItem
import android.view.View
import androidx.activity.viewModels
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.minerdev.faultlocalization.R
import com.minerdev.faultlocalization.adapter.TaskListAdapter
import com.minerdev.faultlocalization.databinding.ActivityMyTaskBinding
import com.minerdev.faultlocalization.utils.Constants.ID
import com.minerdev.faultlocalization.viewmodel.TaskViewModel
import com.minerdev.faultlocalization.viewmodel.factory.TaskViewModelFactory
import java.util.*

class MyTaskActivity : AppCompatActivity() {
    private val binding by lazy { ActivityMyTaskBinding.inflate(layoutInflater) }
    private val adapter by lazy { TaskListAdapter(TaskListAdapter.DiffCallback()) }
    private val viewModel: TaskViewModel by viewModels { TaskViewModelFactory(this) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        supportActionBar?.title = "我的任务"
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        setupRecyclerView()

        viewModel.allItems.observe(this, adapter::submitList)
        viewModel.loadItems(ID)
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

        adapter.clickListener = object : TaskListAdapter.OnItemClickListener {
            override fun onItemClick(
                viewHolder: TaskListAdapter.ViewHolder,
                view: View,
                position: Int
            ) {
                if (adapter[position].state.name == "已完成") {
                    val builder = AlertDialog.Builder(this@MyTaskActivity)
                    builder.setTitle("友情提示")
                    builder.setMessage("您真的要删除该任务记录吗？")
                    builder.setIcon(R.drawable.ic_round_warning_24)
                    builder.setPositiveButton("确认") { _, _ ->
                        viewModel.deleteItem(adapter[position].id) {
                            viewModel.loadItems(ID)
                        }
                    }
                    builder.setNegativeButton("取消") { _, _ ->
                        return@setNegativeButton
                    }
                    val alertDialog = builder.create()
                    alertDialog.show()

                } else {
                    val builder = AlertDialog.Builder(this@MyTaskActivity)
                    builder.setTitle("友情提示")
                    builder.setMessage("您真的要将任务状态改为已完成吗？")
                    builder.setIcon(R.drawable.ic_round_warning_24)
                    builder.setPositiveButton("确认") { _, _ ->
                        adapter[position].state_id = 2
                        viewModel.modifyItem(adapter[position]) {
                            viewModel.loadItems(ID)
                        }
                    }
                    builder.setNegativeButton("取消") { _, _ ->
                        return@setNegativeButton
                    }
                    val alertDialog = builder.create()
                    alertDialog.show()
                }
            }
        }
    }
}