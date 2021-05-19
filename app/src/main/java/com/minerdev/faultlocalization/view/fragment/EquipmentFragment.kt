package com.minerdev.faultlocalization.view.fragment

import android.content.Context.INPUT_METHOD_SERVICE
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.*
import android.view.inputmethod.InputMethodManager
import android.widget.SearchView
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.minerdev.faultlocalization.R
import com.minerdev.faultlocalization.adapter.EquipmentListAdapter
import com.minerdev.faultlocalization.databinding.FragmentEquipmentBinding
import com.minerdev.faultlocalization.utils.Constants
import com.minerdev.faultlocalization.utils.Constants.TYPE_ID
import com.minerdev.faultlocalization.view.activity.DataHistoryActivity
import com.minerdev.faultlocalization.view.activity.EquipmentModifyActivity
import com.minerdev.faultlocalization.view.activity.MyTaskActivity
import com.minerdev.faultlocalization.viewmodel.EquipmentViewModel
import com.minerdev.faultlocalization.viewmodel.factory.EquipmentViewModelFactory
import io.socket.client.IO
import io.socket.client.Socket
import java.net.URISyntaxException
import java.util.*

class EquipmentFragment : Fragment() {
    private val binding by lazy { FragmentEquipmentBinding.inflate(layoutInflater) }
    private val adapter by lazy {
        EquipmentListAdapter(
            requireContext(),
            EquipmentListAdapter.DiffCallback()
        )
    }
    private val viewModel: EquipmentViewModel by viewModels { EquipmentViewModelFactory() }

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
            socket = IO.socket(Constants.BASE_URL + "/equipments")

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

        adapter.clickListener = object : EquipmentListAdapter.OnItemClickListener {
            override fun onDetailButtonClick(
                viewHolder: EquipmentListAdapter.ViewHolder?,
                view: View?,
                position: Int
            ) {
                onDetailButtonClick(position)
            }

            override fun onModifyButtonClick(
                viewHolder: EquipmentListAdapter.ViewHolder?,
                view: View?,
                position: Int
            ) {
                onModifyButtonClick(position)
            }

            override fun onItemLongClick(
                viewHolder: EquipmentListAdapter.ViewHolder?,
                view: View?,
                position: Int
            ) {
                onItemLongClick(position)
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

        if (TYPE_ID == "1") {
            val item = menu.findItem(R.id.toolbar_menu_list)
            item.isVisible = false

        } else {
            val item = menu.findItem(R.id.toolbar_menu_add)
            item.isVisible = false
        }
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.toolbar_menu_add -> {
                if (searchView.hasFocus()) {
                    searchView.onActionViewCollapsed()
                }

                val intent = Intent(context, EquipmentModifyActivity::class.java)
                intent.putExtra("mode", "add")
                startActivity(intent)
            }

            R.id.toolbar_menu_list -> {
                if (searchView.hasFocus()) {
                    searchView.onActionViewCollapsed()
                }

                val intent = Intent(context, MyTaskActivity::class.java)
                startActivity(intent)
            }

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
                }
                dialog.show(requireActivity().supportFragmentManager, "FilterDialogFragment")
            }

            else -> {
            }
        }
        return super.onOptionsItemSelected(item)
    }

    private fun onDetailButtonClick(position: Int) {
        val intent = Intent(context, DataHistoryActivity::class.java)
        intent.putExtra("id", adapter[position].id)
        startActivity(intent)
    }

    private fun onModifyButtonClick(position: Int) {
        if (TYPE_ID == "1") {
            val intent = Intent(context, EquipmentModifyActivity::class.java)
            intent.putExtra("id", adapter[position].id)
            intent.putExtra("mode", "modify")
            startActivity(intent)

        } else if (TYPE_ID == "2") {
            val dialog = RequestSendDialogFragment(adapter[position].state.name)
            viewModel.loadMessageStatesAndTypes()
            viewModel.messageTypes.observe(viewLifecycleOwner, {
                val names = ArrayList<String>().apply { add("选择") }
                for (i in it) {
                    names.add(i.name)
                }
                dialog.types.postValue(names)
            })

            dialog.listener = View.OnClickListener {
                val estimatedTime = dialog.estimatedTime
                val contents = dialog.contents
                val typeId = dialog.spinnerItemPosition
                viewModel.addMessage(adapter[position].id, typeId, estimatedTime, contents)
            }

            dialog.show(
                requireActivity().supportFragmentManager,
                "RequestSendDialogFragment"
            )
        }
    }

    private fun onItemLongClick(position: Int) {
        if (TYPE_ID == "1") {
            val builder = AlertDialog.Builder(requireContext())
            builder.setTitle("友情提示")
            builder.setMessage("您真的要删除吗？")
            builder.setIcon(R.drawable.ic_round_warning_24)
            builder.setPositiveButton("确认") { _, _ ->
                viewModel.deleteItem(adapter[position].id)
            }
            builder.setNegativeButton("取消") { _, _ ->
                return@setNegativeButton
            }
            val alertDialog = builder.create()
            alertDialog.show()
        }
    }
}