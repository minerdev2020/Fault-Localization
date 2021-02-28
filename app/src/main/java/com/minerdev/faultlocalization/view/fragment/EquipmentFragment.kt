package com.minerdev.faultlocalization.view.fragment

import android.content.Context.INPUT_METHOD_SERVICE
import android.content.DialogInterface
import android.content.Intent
import android.os.Bundle
import android.view.*
import android.view.inputmethod.InputMethodManager
import android.widget.SearchView
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.minerdev.faultlocalization.R
import com.minerdev.faultlocalization.Repository
import com.minerdev.faultlocalization.custom.EquipmentListAdapter
import com.minerdev.faultlocalization.custom.SelectDialog
import com.minerdev.faultlocalization.databinding.FragmentEquipBinding
import com.minerdev.faultlocalization.databinding.FragmentPersonBinding
import com.minerdev.faultlocalization.model.Equipment
import com.minerdev.faultlocalization.model.Person
import com.minerdev.faultlocalization.view.activity.DataHistoryActivity
import com.minerdev.faultlocalization.view.activity.EquipmentModifyActivity
import com.minerdev.faultlocalization.viewmodel.EquipmentViewModel
import com.minerdev.faultlocalization.viewmodel.MessageViewModel
import java.util.*

class EquipmentFragment : Fragment() {
    private val items1 = listOf("全部", "正常", "维修中", "停用")
    private val items2 = listOf("全部", "工程1", "工程2", "工程3", "工程4")

    private val binding by lazy { FragmentEquipBinding.inflate(layoutInflater) }
    private val viewModel : EquipmentViewModel by viewModels()
    private val adapter = EquipmentListAdapter()

    private var group1 = 0
    private var group2 = 0

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val manager = LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
        binding.recyclerView.layoutManager = manager
        adapter.listener = object : EquipmentListAdapter.OnItemClickListener {
            override fun onItemClick(
                viewHolder: EquipmentListAdapter.ViewHolder?,
                view: View?,
                position: Int
            ) {
                val intent = Intent(context, DataHistoryActivity::class.java)
                startActivity(intent)
            }

            override fun onItemLongClick(
                viewHolder: EquipmentListAdapter.ViewHolder?,
                view: View?,
                position: Int
            ) {
                val builder = context?.let { AlertDialog.Builder(it) }
                builder?.setTitle("友情提示")
                builder?.setMessage("您真的要删除吗？")
                builder?.setIcon(R.drawable.ic_round_warning_24)
                builder?.setPositiveButton(
                    "确认",
                    DialogInterface.OnClickListener { _, _ ->
//                        delete(adapter.getItem(position).uid)
                        return@OnClickListener
                    })
                builder?.setNegativeButton(
                    "取消",
                    DialogInterface.OnClickListener { _, _ -> return@OnClickListener })
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
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.toolbar_menu_add -> {
                val intent = Intent(context, EquipmentModifyActivity::class.java)
                startActivity(intent)
            }

            R.id.toolbar_menu_filter -> {
                val dialog = context?.let { SelectDialog(it) }
                dialog?.items1 = items1
                dialog?.items2 = items2
                dialog?.setCancelable(false)
                dialog?.listener = View.OnClickListener {
                    group1 = dialog?.spinner1ItemPosition ?: -1
                    group2 = dialog?.spinner2ItemPosition ?: -1
                    rearrangeList(null, group1, group2)
                }
                dialog?.show()
            }

            else -> {
            }
        }
        return super.onOptionsItemSelected(item)
    }

    private fun rearrangeList(keyword: String?, group1: Int, group2: Int) {

    }
}