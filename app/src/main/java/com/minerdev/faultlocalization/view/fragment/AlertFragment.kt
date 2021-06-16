package com.minerdev.faultlocalization.view.fragment

import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.*
import android.view.inputmethod.InputMethodManager
import android.widget.SearchView
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.minerdev.faultlocalization.R
import com.minerdev.faultlocalization.adapter.AlertListAdapter
import com.minerdev.faultlocalization.databinding.FragmentAlertBinding
import com.minerdev.faultlocalization.utils.Constants
import com.minerdev.faultlocalization.viewmodel.AlertViewModel
import com.minerdev.faultlocalization.viewmodel.factory.AlertViewModelFactory
import io.socket.client.IO
import io.socket.client.Socket
import java.net.URISyntaxException
import java.util.*

class AlertFragment : Fragment() {
    private val binding by lazy { FragmentAlertBinding.inflate(layoutInflater) }
    private val adapter by lazy { AlertListAdapter(AlertListAdapter.DiffCallback()) }
    private val viewModel: AlertViewModel by viewModels { AlertViewModelFactory() }

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
            socket = IO.socket(Constants.BASE_URL + "/alerts")

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

        adapter.clickListener = object : AlertListAdapter.OnItemClickListener {
            override fun onItemClick(
                viewHolder: AlertListAdapter.ViewHolder,
                view: View,
                position: Int
            ) {
                val dialog = AlertDialogFragment(adapter[position])
                dialog.show(requireActivity().supportFragmentManager, "AlertDialogFragment")
            }

            override fun onButtonClick(
                viewHolder: AlertListAdapter.ViewHolder,
                view: View,
                position: Int
            ) {
                if (Constants.TYPE_ID == "2") {
                    if (adapter[position].state.name == "进行中") {
                        val dialog = AlertModifyDialogFragment(adapter[position].state.name)
                        viewModel.loadItemsStatesAndTypes()
                        viewModel.itemStates.observe(viewLifecycleOwner, {
                            val names = ArrayList<String>().apply { add("选择") }
                            for (i in it) {
                                names.add(i.name)
                            }
                            dialog.states.postValue(names)
                        })

                        dialog.listener = View.OnClickListener {
                            val stateId = dialog.spinnerItemPosition
                            viewModel.modifyItemState(adapter[position].id, stateId.toByte())
                        }

                        dialog.show(
                            requireActivity().supportFragmentManager,
                            "AlertModifyDialogFragment"
                        )
                    }
                }
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
                    requireActivity().getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
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
}