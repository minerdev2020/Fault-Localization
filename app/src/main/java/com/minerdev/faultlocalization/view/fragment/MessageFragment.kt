package com.minerdev.faultlocalization.view.fragment

import android.content.Context.INPUT_METHOD_SERVICE
import android.os.Bundle
import android.view.*
import android.view.inputmethod.InputMethodManager
import android.widget.SearchView
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.minerdev.faultlocalization.R
import com.minerdev.faultlocalization.adapter.MessageListAdapter
import com.minerdev.faultlocalization.databinding.FragmentMessageBinding
import com.minerdev.faultlocalization.utils.Constants.TYPE_ID
import com.minerdev.faultlocalization.viewmodel.MessageViewModel
import com.minerdev.faultlocalization.viewmodel.factory.MessageViewModelFactory
import java.util.*
import kotlin.concurrent.timer

class MessageFragment : Fragment() {
    private val binding by lazy { FragmentMessageBinding.inflate(layoutInflater) }
    private val adapter by lazy { MessageListAdapter(MessageListAdapter.DiffCallback()) }
    private val viewModel: MessageViewModel by viewModels { MessageViewModelFactory(requireContext()) }

    private var keyword = ""
    private var group1 = 0
    private var group2 = 0

    private lateinit var searchView: SearchView
    private lateinit var timer: Timer

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        viewModel.allItems.observe(viewLifecycleOwner, adapter::submitList)

        val manager = LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
        binding.recyclerView.layoutManager = manager
        binding.recyclerView.addItemDecoration(
            DividerItemDecoration(
                context,
                DividerItemDecoration.VERTICAL
            )
        )
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

        return binding.root
    }

    override fun onResume() {
        super.onResume()
        timer = timer(period = 1000) {
            viewModel.loadItems(keyword, group1, group2)
        }
    }

    override fun onPause() {
        super.onPause()
        timer.cancel()
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
                return true
            }

            override fun onQueryTextChange(newText: String): Boolean {
                return true
            }
        })

        searchView.setOnCloseListener {
            searchView.onActionViewCollapsed()
            keyword = ""
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