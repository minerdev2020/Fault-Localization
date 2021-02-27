package com.minerdev.faultlocalization.view.fragment

import android.content.ContentValues
import android.content.DialogInterface
import android.content.Intent
import android.os.Bundle
import android.view.*
import android.view.inputmethod.InputMethodManager
import android.widget.SearchView
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.gson.Gson
import com.minerdev.faultlocalization.R
import com.minerdev.faultlocalization.custom.EquipListAdapter
import com.minerdev.faultlocalization.custom.SelectDialog
import com.minerdev.faultlocalization.databinding.FragmentPersonBinding
import com.minerdev.faultlocalization.model.Equipment
import com.minerdev.faultlocalization.view.activity.DataHistoryActivity
import com.minerdev.faultlocalization.view.activity.EquipModifyActivity
import java.util.*

class EquipFragment : Fragment() {
    private val originalItems: ArrayList<Equipment> = ArrayList<Equipment>()
    private val items: ArrayList<Equipment> = ArrayList<Equipment>()
    private val items1 = arrayOf("全部", "正常", "维修中", "停用")
    private val items2 = arrayOf("全部", "工程1", "工程2", "工程3", "工程4")

    private val binding by lazy { FragmentPersonBinding.inflate(layoutInflater) }
    private val adapter = EquipListAdapter()

    private var group1 = 0
    private var group2 = 0
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val manager = LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
        binding.recyclerView.layoutManager = manager
        adapter.setOnItemClickListener(object : OnEquipItemClickListener() {
            fun onItemClick(viewHolder: EquipListAdapter.ViewHolder?, view: View?, position: Int) {
                val intent = Intent(context, DataHistoryActivity::class.java)
                startActivity(intent)
            }

            fun onItemLongClick(
                viewHolder: EquipListAdapter.ViewHolder?,
                view: View?,
                position: Int
            ) {
                val builder = AlertDialog.Builder(context)
                builder.setTitle("友情提示")
                builder.setMessage("您真的要删除吗？")
                builder.setIcon(R.drawable.ic_round_warning_24)
                builder.setPositiveButton(
                    "确认",
                    DialogInterface.OnClickListener { _, _ ->
                        delete(adapter.getItem(position).uid)
                        return@OnClickListener
                    })
                builder.setNegativeButton(
                    "取消",
                    DialogInterface.OnClickListener { _, _ -> return@OnClickListener })
                val alertDialog = builder.create()
                alertDialog.show()
            }
        })
        binding.recyclerView.adapter = adapter
        originalItems.clear()

        return binding.root
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        val searchView: SearchView = activity?.findViewById(R.id.searchView)
        searchView.visibility = View.VISIBLE
        searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String): Boolean {
                rearrangeList(query, group1, group2)
                val manager =
                    activity?.getSystemService(activity.INPUT_METHOD_SERVICE) as InputMethodManager
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
        super.onCreateOptionsMenu(menu, inflater)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.toolbar_menu_add -> {
                val intent = Intent(getContext(), EquipModifyActivity::class.java)
                startActivity(intent)
            }
            R.id.toolbar_menu_filter -> {
                val dialog = SelectDialog(context)
                dialog.setItems1(items1)
                dialog.setItems2(items2)
                dialog.setCancelable(false)
                dialog.setOnOKButtonClickListener(View.OnClickListener {
                    group1 = dialog.spinner1ItemPosition
                    group2 = dialog.spinner2ItemPosition
                    rearrangeList(null, group1, group2)
                })
                dialog.show()
            }
            else -> {
            }
        }
        return super.onOptionsItemSelected(item)
    }

    protected fun setNetworkSetting() {
        val values = ContentValues()
        values.put("data_type", "equipment")
        networkSetting = NetworkSetting("http://192.168.35.141:80/get_data.php", values,
            "获取数据中...", true, 500, object : OnDataReceiveListener() {
                fun parseData(receivedString: String) {
                    if (receivedString == "no_need") {
                        return
                    }
                    originalItems.clear()
                    val gson = Gson()
                    val data: Array<Equipment.SerializedData>? =
                        gson.fromJson<Array<Equipment.SerializedData>>(
                            receivedString,
                            Array<Equipment.SerializedData>::class.java
                        )
                    if (data != null) {
                        for (item in data) {
                            originalItems.add(Equipment(item))
                        }
                        adapter.setItems(originalItems)
                        adapter.notifyDataSetChanged()
                    }
                }
            })
    }

    private fun rearrangeList(keyword: String?, group1: Int, group2: Int) {
        if (keyword == null && group1 == 0 && group2 == 0) {
            adapter.setItems(originalItems)
            adapter.notifyDataSetChanged()
            return
        }
        items.clear()
        for (i in originalItems) {
            if ((keyword == null || i.name.contains(keyword))
                && (group1 == 0 || i.state.name === items1[group1])
                && (group2 == 0 || i.type === items2[group2])
            ) {
                items.add(i)
            }
        }
        if (originalItems.size > 0) {
            adapter.setItems(originalItems)
            adapter.notifyDataSetChanged()
        }
    }

    private fun delete(position: Int) {
        val values = ContentValues()
        values.put("data_type", "equipment")
        values.put("uid", originalItems[position].uid)
        val networkTask = NetworkTask(context,
            "http://192.168.35.141:80/delete.php", values, "删除中...",
            object : OnDataReceiveListener() {
                fun parseData(receivedString: String) {
                    if (receivedString == "delete_success") {
                        Toast.makeText(context, "delete success.", Toast.LENGTH_SHORT).show()
                    } else {
                        Toast.makeText(context, "delete failed.", Toast.LENGTH_SHORT).show()
                    }
                }
            })
        networkTask.execute()
    }
}