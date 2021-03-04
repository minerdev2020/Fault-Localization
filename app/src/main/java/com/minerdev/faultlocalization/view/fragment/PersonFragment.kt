package com.minerdev.faultlocalization.view.fragment

import android.content.Context.INPUT_METHOD_SERVICE
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
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.minerdev.faultlocalization.R
import com.minerdev.faultlocalization.adapter.PersonListAdapter
import com.minerdev.faultlocalization.databinding.FragmentPersonBinding
import com.minerdev.faultlocalization.factory.PersonViewModelFactory
import com.minerdev.faultlocalization.view.activity.LoginLogActivity
import com.minerdev.faultlocalization.view.activity.PersonModifyActivity
import com.minerdev.faultlocalization.viewmodel.PersonViewModel
import kotlinx.serialization.InternalSerializationApi
import java.util.*

class PersonFragment : Fragment() {
    private val items1 = listOf("全部", "上线", "下线")
    private val items2 = listOf("全部", "管理", "维修")

    private val binding by lazy { FragmentPersonBinding.inflate(layoutInflater) }
    private val adapter by lazy { PersonListAdapter(PersonListAdapter.DiffCallback()) }
    private val viewModel: PersonViewModel by viewModels { PersonViewModelFactory() }

    private var group1 = 0
    private var group2 = 0

    private lateinit var searchView: SearchView

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val manager = LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
        binding.recyclerView.layoutManager = manager
        binding.recyclerView.addItemDecoration(
            DividerItemDecoration(
                context,
                DividerItemDecoration.VERTICAL
            )
        )
        binding.recyclerView.adapter = adapter

        adapter.listener = object : PersonListAdapter.OnItemClickListener {
            override fun onItemClick(
                viewHolder: PersonListAdapter.ViewHolder,
                view: View,
                position: Int
            ) {
                if (searchView.hasFocus()) {
                    searchView.onActionViewCollapsed()
                }

                showPopupMenu(view, position)
            }

            override fun onItemLongClick(
                viewHolder: PersonListAdapter.ViewHolder,
                view: View,
                position: Int
            ) {
                tryCall(adapter[position].phone)
            }
        }

        viewModel.allItems.observe(viewLifecycleOwner, adapter::submitList)

        return binding.root
    }

    override fun onResume() {
        super.onResume()
        viewModel.loadItems()
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        super.onCreateOptionsMenu(menu, inflater)

        searchView = activity?.findViewById(R.id.searchView) ?: return
        searchView.visibility = View.VISIBLE
        searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String): Boolean {
                rearrangeList(query, group1, group2)
                val manager =
                    activity?.getSystemService(INPUT_METHOD_SERVICE) as InputMethodManager
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
                if (searchView.hasFocus()) {
                    searchView.onActionViewCollapsed()
                }

                val dialog = SelectDialogFragment()
                dialog.items1 = items1
                dialog.items2 = items2
                dialog.listener = View.OnClickListener {
                    group1 = dialog.spinner1ItemPosition
                    group2 = dialog.spinner2ItemPosition
                    rearrangeList("", group1, group2)
                }
                activity?.supportFragmentManager?.let { dialog.show(it, "SampleDialog") }
            }

            else -> {
            }
        }
        return super.onOptionsItemSelected(item)
    }

    private fun tryCall(phone: String) {
        val builder = context?.let { AlertDialog.Builder(it) }
        builder?.setTitle("友情提示")
        builder?.setMessage("您要转到通话画面吗？")
        builder?.setIcon(R.drawable.ic_round_notification_important_24)

        builder?.setPositiveButton("确认") { _, _ ->
            val intent = Intent(Intent.ACTION_DIAL, Uri.parse("tel:$phone"))
            startActivity(intent)
        }

        builder?.setNegativeButton("取消") { _, _ ->
            DialogInterface.OnClickListener { _, _ -> return@OnClickListener }
        }

        val alertDialog = builder?.create()
        alertDialog?.show()
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

    private fun rearrangeList(keyword: String, group1: Int, group2: Int) {

    }
}