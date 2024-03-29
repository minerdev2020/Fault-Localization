package com.minerdev.faultlocalization.view.fragment

import android.content.Context.INPUT_METHOD_SERVICE
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.*
import android.view.inputmethod.InputMethodManager
import android.widget.SearchView
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.widget.PopupMenu
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.minerdev.faultlocalization.R
import com.minerdev.faultlocalization.adapter.PersonListAdapter
import com.minerdev.faultlocalization.databinding.FragmentPersonBinding
import com.minerdev.faultlocalization.utils.Constants
import com.minerdev.faultlocalization.utils.Constants.TYPE_ID
import com.minerdev.faultlocalization.view.activity.LoginLogActivity
import com.minerdev.faultlocalization.view.activity.PersonModifyActivity
import com.minerdev.faultlocalization.viewmodel.PersonViewModel
import com.minerdev.faultlocalization.viewmodel.factory.PersonViewModelFactory
import io.socket.client.IO
import io.socket.client.Socket
import java.net.URISyntaxException
import java.util.*

class PersonFragment : Fragment() {
    private val binding by lazy { FragmentPersonBinding.inflate(layoutInflater) }
    private val adapter by lazy { PersonListAdapter(PersonListAdapter.DiffCallback()) }
    private val viewModel: PersonViewModel by viewModels { PersonViewModelFactory() }

    private var socket: Socket? = null
    private var keyword = ""
    private var group1 = 0
    private var group2 = 0

    private lateinit var searchView: SearchView

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        try {
            socket = IO.socket(Constants.BASE_URL + "/persons")

        } catch (e: URISyntaxException) {
            Log.e(Constants.TAG, e.reason)
        }

        socket?.let { s ->
            s.connect()
            s.on(Socket.EVENT_CONNECT) {
                Log.d(Constants.TAG, "Connected!")
            }
            s.on("create") {
                viewModel.loadItems(keyword, group1, group2)
            }
            s.on("update") {
                viewModel.loadItems(keyword, group1, group2)
            }
            s.on("delete") {
                viewModel.loadItems(keyword, group1, group2)
            }
        }

        setHasOptionsMenu(true)

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel.allItems.observe(viewLifecycleOwner, adapter::submitList)

        val manager = LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
        binding.recyclerView.layoutManager = manager
        binding.recyclerView.adapter = adapter

        adapter.clickListener = object : PersonListAdapter.OnItemClickListener {
            override fun onItemClick(
                viewHolder: PersonListAdapter.ViewHolder,
                view: View,
                position: Int
            ) {
                if (searchView.hasFocus()) {
                    searchView.onActionViewCollapsed()
                }

                if (TYPE_ID == "1") {
                    showPopupMenu(view, position)

                } else if (TYPE_ID == "2") {
                    tryCall(adapter[position].phone)
                }
            }

            override fun onItemLongClick(
                viewHolder: PersonListAdapter.ViewHolder,
                view: View,
                position: Int
            ) {
                tryCall(adapter[position].phone)
            }
        }
    }

    override fun onResume() {
        super.onResume()
        Log.d(Constants.TAG, "onResume")
        viewModel.loadItems(keyword, group1, group2)
    }

    override fun onPause() {
        super.onPause()
        Log.d(Constants.TAG, "onPause")
        if (keyword.isEmpty() || group1 == 0 || group2 == 0) {
            viewModel.loadItems(keyword, group1, group2)
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        socket?.disconnect()
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        super.onCreateOptionsMenu(menu, inflater)

        searchView = requireActivity().findViewById(R.id.searchView)
        searchView.visibility = View.VISIBLE
        searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String): Boolean {
                keyword = query
                val manager =
                    requireActivity().getSystemService(INPUT_METHOD_SERVICE) as InputMethodManager
                manager.hideSoftInputFromWindow(
                    requireActivity().currentFocus?.windowToken,
                    InputMethodManager.HIDE_NOT_ALWAYS
                )
                viewModel.loadItems(keyword, group1, group2)
                return true
            }

            override fun onQueryTextChange(newText: String): Boolean {
                return true
            }
        })

        searchView.setOnCloseListener {
            searchView.onActionViewCollapsed()
            keyword = ""
            viewModel.loadItems(keyword, group1, group2)
            true
        }

        var item = menu.findItem(R.id.toolbar_menu_add)
        item.isVisible = false
        item = menu.findItem(R.id.toolbar_menu_list)
        item.isVisible = false
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.toolbar_menu_filter -> {
                if (searchView.hasFocus()) {
                    searchView.onActionViewCollapsed()
                }

                val dialog = FilterDialogFragment()
                viewModel.loadItemsStatesAndTypes()
                viewModel.itemStates.observe(viewLifecycleOwner, {
                    val names = ArrayList<String>().apply { add("全部") }
                    for (i in it) {
                        names.add(i.name)
                    }
                    dialog.items1.postValue(names)
                })
                viewModel.itemTypes.observe(viewLifecycleOwner, {
                    val names = ArrayList<String>().apply { add("全部") }
                    for (i in it) {
                        names.add(i.name)
                    }
                    dialog.items2.postValue(names)
                })
                dialog.listener = View.OnClickListener {
                    group1 = dialog.spinner1ItemPosition
                    group2 = dialog.spinner2ItemPosition
                    viewModel.loadItems(keyword, group1, group2)
                }
                dialog.show(requireActivity().supportFragmentManager, "SampleDialog")
            }

            else -> {
            }
        }
        return super.onOptionsItemSelected(item)
    }

    private fun tryCall(phone: String) {
        val builder = AlertDialog.Builder(requireContext())
        builder.setTitle("友情提示")
        builder.setMessage("您要转到通话画面吗？")
        builder.setIcon(R.drawable.ic_round_notification_important_24)

        builder.setPositiveButton("确认") { _, _ ->
            val intent = Intent(Intent.ACTION_DIAL, Uri.parse("tel:$phone"))
            startActivity(intent)
        }

        builder.setNegativeButton("取消") { _, _ ->
            return@setNegativeButton
        }

        val alertDialog = builder.create()
        alertDialog.show()
    }

    private fun showPopupMenu(view: View, position: Int) {
        val popupMenu = PopupMenu(requireContext(), view, Gravity.END)
        requireActivity().menuInflater.inflate(R.menu.menu_person, popupMenu.menu)

        popupMenu.setOnMenuItemClickListener { menuItem ->
            val intent: Intent
            when (menuItem.itemId) {
                R.id.popup_person_log -> {
                    intent = Intent(context, LoginLogActivity::class.java)
                    intent.putExtra("id", adapter[position].id)
                    startActivity(intent)
                }

                R.id.popup_person_call -> tryCall(adapter[position].phone)

                R.id.popup_person_modify -> {
                    intent = Intent(context, PersonModifyActivity::class.java)
                    intent.putExtra("id", adapter[position].id)
                    startActivity(intent)
                }

                R.id.popup_person_delete -> {
                    val builder = AlertDialog.Builder(requireContext())
                    builder.setTitle("友情提示")
                    builder.setIcon(R.drawable.ic_round_warning_24)
                    builder.setMessage("您真的要删除吗？")
                    builder.setPositiveButton("确认") { _, _ ->
                        viewModel.deleteItem(adapter[position].id)
                    }
                    builder.setNegativeButton("取消") { _, _ ->
                        return@setNegativeButton
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
}