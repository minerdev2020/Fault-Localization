package com.minerdev.faultlocalization.view.fragment

import android.content.Context.INPUT_METHOD_SERVICE
import android.content.DialogInterface
import android.os.Bundle
import android.view.*
import android.view.inputmethod.InputMethodManager
import android.widget.SearchView
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.minerdev.faultlocalization.R
import com.minerdev.faultlocalization.custom.MessageListAdapter
import com.minerdev.faultlocalization.databinding.FragmentMessageBinding
import com.minerdev.faultlocalization.model.Message
import com.minerdev.faultlocalization.viewmodel.ItemViewModel
import java.util.*

class MessageFragment : Fragment() {
    private val items1 = listOf("全部", "未完成", "进行中", "已完成")
    private val items2 = listOf("全部", "注册", "维修申请", "维修完成")

    private val binding by lazy { FragmentMessageBinding.inflate(layoutInflater) }
    private val viewModel: ItemViewModel<Message> by viewModels()
    private val adapter = MessageListAdapter()

    private var group1 = 0
    private var group2 = 0

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val manager = LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
        binding.recyclerView.layoutManager = manager
        adapter.listener = object : MessageListAdapter.OnItemClickListener {
            override fun onItemClick(
                viewHolder: MessageListAdapter.ViewHolder?,
                view: View?,
                position: Int
            ) {
                val builder = context?.let { AlertDialog.Builder(it) }
                builder?.setTitle("友情提示")
                builder?.setMessage("是否允许该请求？")
                builder?.setIcon(R.drawable.ic_round_notification_important_24)

                builder?.setPositiveButton(
                    "允许",
                    DialogInterface.OnClickListener { _, _ ->
                        return@OnClickListener
                    }
                )

                builder?.setNegativeButton(
                    "拒绝",
                    DialogInterface.OnClickListener { _, _ ->
                        return@OnClickListener
                    }
                )

                builder?.setNeutralButton(
                    "取消",
                    DialogInterface.OnClickListener { _, _ ->
                        return@OnClickListener
                    }
                )

                val alertDialog = builder?.create()
                alertDialog?.show()
            }
        }

        binding.recyclerView.adapter = adapter

        return binding.root
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        super.onCreateOptionsMenu(menu, inflater)

        val searchView: SearchView = activity?.findViewById(R.id.searchView) ?: return
        searchView.visibility = View.VISIBLE
        searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String): Boolean {
                rearrangeList(query, group1, group2)

                val manager = activity?.getSystemService(INPUT_METHOD_SERVICE) as InputMethodManager
                manager.hideSoftInputFromWindow(
                    activity?.currentFocus?.windowToken,
                    InputMethodManager.HIDE_NOT_ALWAYS
                )
                return true
            }

            override fun onQueryTextChange(newText: String): Boolean {
                return true
            }
        })

        searchView.setOnCloseListener {
            searchView.onActionViewCollapsed()
            true
        }

        val item = menu.findItem(R.id.toolbar_menu_add)
        item.isVisible = false
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.toolbar_menu_filter -> {
                val dialog = SelectDialog()
                dialog.items1 = items1
                dialog.items2 = items2
                dialog.listener = View.OnClickListener {
                    group1 = dialog.spinner1ItemPosition
                    group2 = dialog.spinner2ItemPosition
                    rearrangeList(null, group1, group2)
                }
                activity?.supportFragmentManager?.let { dialog.show(it, "SampleDialog") }
            }

            else -> {
            }
        }

        return super.onOptionsItemSelected(item)
    }

    private fun rearrangeList(keyword: String?, group1: Int, group2: Int) {

    }
}