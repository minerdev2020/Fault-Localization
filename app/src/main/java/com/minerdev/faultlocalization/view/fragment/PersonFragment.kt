package com.minerdev.faultlocalization.view.fragment

import android.content.ContentValues
import android.content.DialogInterface
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.*
import android.view.inputmethod.InputMethodManager
import android.widget.SearchView
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.widget.PopupMenu
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.gson.Gson
import com.minerdev.faultlocalization.R
import com.minerdev.faultlocalization.custom.PersonListAdapter
import com.minerdev.faultlocalization.custom.SelectDialog
import com.minerdev.faultlocalization.databinding.FragmentPersonBinding
import com.minerdev.faultlocalization.model.Person
import com.minerdev.faultlocalization.view.activity.LoginLogActivity
import com.minerdev.faultlocalization.view.activity.PersonModifyActivity
import java.util.*

class PersonFragment : Fragment() {
    private val originalItems: ArrayList<Person> = ArrayList<Person>()
    private val items: ArrayList<Person> = ArrayList<Person>()
    private val items1 = arrayOf("全部", "上线", "下线")
    private val items2 = arrayOf("全部", "管理", "维修")

    private val binding by lazy { FragmentPersonBinding.inflate(layoutInflater) }
    private val adapter = PersonListAdapter()

    private var group1 = 0
    private var group2 = 0
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val manager = LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
        binding.recyclerView.layoutManager = manager
        adapter.setOnItemClickListener(object : OnPersonItemClickListener() {
            fun onItemClick(viewHolder: PersonListAdapter.ViewHolder?, view: View, position: Int) {
                setPopupMenu(view, position)
            }

            fun onItemLongClick(
                viewHolder: PersonListAdapter.ViewHolder?,
                view: View?,
                position: Int
            ) {
                tryCall(adapter.getItem(position).getPhone())
            }
        })

        binding.recyclerView.adapter = adapter
        originalItems.clear()

        return binding.root
    }

    protected fun setNetworkSetting() {
        val values = ContentValues()
        values.put("data_type", "person")
        networkSetting = NetworkSetting("http://192.168.35.141:80/get_data.php", values,
            "获取数据中...", true, 500, object : OnDataReceiveListener() {
                fun parseData(receivedString: String) {
                    if (receivedString == "no_need") {
                        return
                    }
                    originalItems.clear()
                    val gson = Gson()
                    val data: Array<Person.SerializedData>? =
                        gson.fromJson<Array<Person.SerializedData>>(
                            receivedString,
                            Array<Person.SerializedData>::class.java
                        )
                    if (data != null) {
                        for (item in data) {
                            originalItems.add(Person(item))
                        }
                        adapter.setItems(originalItems)
                        adapter.notifyDataSetChanged()
                    }
                }
            })
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
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.toolbar_menu_add -> {
                val intent = Intent(context, PersonModifyActivity::class.java)
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

    private fun tryCall(phone: String) {
        val builder = AlertDialog.Builder(context)
        builder.setTitle("友情提示")
        builder.setMessage("您要转到通话画面吗？")
        builder.setIcon(R.drawable.ic_round_notification_important_24)
        builder.setPositiveButton("确认") { _, _ ->
            val intent = Intent(Intent.ACTION_DIAL, Uri.parse("tel:$phone"))
            startActivity(intent)
        }
        builder.setNegativeButton("取消") { _, _ ->
            DialogInterface.OnClickListener { _, _ -> return@OnClickListener }
        }
        val alertDialog = builder.create()
        alertDialog.show()
    }

    private fun setPopupMenu(view: View, position: Int) {
        val popupMenu = PopupMenu(context, view)
        activity?.menuInflater?.inflate(R.menu.menu_person, popupMenu.menu)
        popupMenu.setOnMenuItemClickListener { menuItem ->
            val intent: Intent
            when (menuItem.itemId) {
                R.id.popup_person_log -> {
                    intent = Intent(context, LoginLogActivity::class.java)
                    startActivity(intent)
                }
                R.id.popup_person_call -> tryCall(adapter.getItem(position).getPhone())
                R.id.popup_person_modify -> {
                    intent = Intent(context, PersonModifyActivity::class.java)
                    startActivity(intent)
                }
                R.id.popup_person_delete -> {
                    val builder = AlertDialog.Builder(context)
                    builder.setTitle("友情提示")
                    builder.setMessage("您真的要删除吗？")
                    builder.setIcon(R.drawable.ic_round_warning_24)
                    builder.setPositiveButton("确认") { _, _ ->
                        delete(
                            adapter.getItem(
                                position
                            ).getUid()
                        )
                    }
                    builder.setNegativeButton("取消") { _, _ ->
                        DialogInterface.OnClickListener { _, _ -> return@OnClickListener }
                    }
                    val alertDialog = builder.create()
                    alertDialog.show()
                }
                else -> Toast.makeText(context, "popup menu error.", Toast.LENGTH_SHORT).show()
            }
            true
        }
        popupMenu.show()
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
                && (group2 == 0 || i.type.name === items2[group2])
            ) {
                items.add(i)
            }
        }
        adapter.setItems(items)
        adapter.notifyDataSetChanged()
    }

    private fun delete(uid: Int) {
        val values = ContentValues()
        values.put("data_type", "person")
        values.put("uid", uid)
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