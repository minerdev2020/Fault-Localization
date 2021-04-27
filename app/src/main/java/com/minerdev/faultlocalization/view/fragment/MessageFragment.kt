package com.minerdev.faultlocalization.view.fragment

import android.content.Context.INPUT_METHOD_SERVICE
import android.os.Bundle
import android.util.Log
import android.view.*
import android.view.inputmethod.InputMethodManager
import android.widget.SearchView
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.minerdev.faultlocalization.R
import com.minerdev.faultlocalization.adapter.MessageListAdapter
import com.minerdev.faultlocalization.base.BasePageFragment
import com.minerdev.faultlocalization.databinding.FragmentMessageBinding
import com.minerdev.faultlocalization.utils.Constants
import com.minerdev.faultlocalization.utils.Constants.TYPE_ID
import com.minerdev.faultlocalization.viewmodel.MessageViewModel
import com.minerdev.faultlocalization.viewmodel.factory.MessageViewModelFactory
import io.socket.client.IO
import io.socket.client.Socket
import java.net.URISyntaxException
import java.util.*

class MessageFragment : BasePageFragment() {
    private val binding by lazy { FragmentMessageBinding.inflate(layoutInflater) }
    private val adapter by lazy { MessageListAdapter(MessageListAdapter.DiffCallback()) }
    private val viewModel: MessageViewModel by viewModels { MessageViewModelFactory() }

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
            socket = IO.socket(Constants.BASE_URL + "/messages")

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

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel.allItems.observe(viewLifecycleOwner, adapter::submitList)

        val manager = LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
        binding.recyclerView.layoutManager = manager
        binding.recyclerView.adapter = adapter

        adapter.clickListener = object : MessageListAdapter.OnItemClickListener {
            override fun onItemClick(
                viewHolder: MessageListAdapter.ViewHolder,
                view: View,
                position: Int
            ) {
                val dialog = MessageDialogFragment(adapter[position])
                dialog.show(requireActivity().supportFragmentManager, "MessageDialogFragment")
            }

            override fun onButtonClick(
                viewHolder: MessageListAdapter.ViewHolder,
                view: View,
                position: Int
            ) {
                if (TYPE_ID == "1") {
                    if (adapter[position].state.name == "等待中") {
                        val builder = AlertDialog.Builder(requireContext())
                        builder.setTitle("友情提示")
                        builder.setMessage("是否接受该请求？")
                        builder.setIcon(R.drawable.ic_round_notification_important_24)

                        builder.setPositiveButton("接受") { _, _ ->
                            viewModel.acceptRequest(adapter[position])
                        }

                        builder.setNegativeButton("拒绝") { _, _ ->
                            viewModel.refuseRequest(adapter[position])
                        }

                        builder.setNeutralButton("取消") { _, _ ->
                            return@setNeutralButton
                        }

                        val alertDialog = builder.create()
                        alertDialog.show()
                    }
                }
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        socket?.disconnect()
    }

    override fun initializePage() {
        keyword = ""
        group1 = 0
        group2 = 0

        viewModel.loadItems(keyword, group1, group2)
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
                }
                dialog.show(requireActivity().supportFragmentManager, "SampleDialog")
            }

            else -> {
            }
        }

        return super.onOptionsItemSelected(item)
    }
}