package com.minerdev.faultlocalization.view.fragment

import android.content.ContentValues
import android.content.DialogInterface
import android.os.Bundle
import android.view.*
import android.view.inputmethod.InputMethodManager
import android.widget.SearchView
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.gson.Gson
import com.minerdev.faultlocalization.R
import com.minerdev.faultlocalization.custom.MessageListAdapter
import com.minerdev.faultlocalization.custom.SelectDialog
import com.minerdev.faultlocalization.databinding.FragmentMessageBinding
import com.minerdev.faultlocalization.model.Message
import java.util.*

class MessageFragment : Fragment() {
    private val originalItems: ArrayList<Message> = ArrayList<Message>()
    private val items: ArrayList<Message> = ArrayList<Message>()
    private val items1 = arrayOf("全部", "未完成", "进行中", "已完成")
    private val items2 = arrayOf("全部", "注册", "维修申请", "维修完成")

    private val binding by lazy { FragmentMessageBinding.inflate(layoutInflater) }
    private val adapter = MessageListAdapter()

    private var group1 = 0
    private var group2 = 0

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val manager = LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
        binding.recyclerView.layoutManager = manager
        adapter.setOnItemClickListener(object : OnMessageItemClickListener() {
            fun onItemClick(
                viewHolder: MessageListAdapter.ViewHolder?,
                view: View?,
                position: Int
            ) {
                val builder = AlertDialog.Builder(context)
                builder.setTitle("友情提示")
                builder.setMessage("是否允许该请求？")
                builder.setIcon(R.drawable.ic_round_notification_important_24)
                builder.setPositiveButton(
                    "允许",
                    DialogInterface.OnClickListener { _, _ -> return@OnClickListener })
                builder.setNegativeButton(
                    "拒绝",
                    DialogInterface.OnClickListener { _, _ -> return@OnClickListener })
                builder.setNeutralButton(
                    "取消",
                    DialogInterface.OnClickListener { _, _ -> return@OnClickListener })
                val alertDialog = builder.create()
                alertDialog.show()
            }

            fun onItemLongClick(
                viewHolder: MessageListAdapter.ViewHolder?,
                view: View?,
                position: Int
            ) {
                return
            }
        })

        binding.recyclerView.adapter = adapter
        originalItems.clear()

        return binding.root
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        super.onCreateOptionsMenu(menu, inflater)

        val searchView: SearchView = activity!!.findViewById(R.id.searchView)
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
        val item = menu.findItem(R.id.toolbar_menu_add)
        item.isVisible = false
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
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
        values.put("data_type", "message")
        networkSetting = NetworkSetting("http://192.168.35.141:80/get_data.php", values,
            "获取数据中...", true, 500, object : OnDataReceiveListener() {
                fun parseData(receivedString: String) {
                    if (receivedString == "no_need") {
                        return
                    }
                    originalItems.clear()
                    val gson = Gson()
                    val data: Array<Message.SerializedData>? =
                        gson.fromJson<Array<Message.SerializedData>>(
                            receivedString,
                            Array<Message.SerializedData>::class.java
                        )
                    if (data != null) {
                        for (item in data) {
                            originalItems.add(Message(item))
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
            if ((keyword == null || i.fromWho.contains(keyword))
                && (group1 == 0 || i.state.name === items1[group1])
                && (group2 == 0 || i.type.name === items2[group2])
            ) {
                items.add(i)
            }
        }

        adapter.setItems(items)
        adapter.notifyDataSetChanged()
    }
}