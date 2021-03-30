package com.minerdev.faultlocalization.view.fragment

import android.os.Bundle
import android.view.*
import android.widget.AdapterView.OnItemClickListener
import android.widget.ArrayAdapter
import android.widget.SearchView
import androidx.fragment.app.Fragment
import com.minerdev.faultlocalization.R
import com.minerdev.faultlocalization.databinding.FragmentSettingsBinding
import com.minerdev.faultlocalization.utils.AppHelper

class SettingsFragment : Fragment() {
    private val listMenu = listOf("修改个人信息", "退出账号")
    private val binding by lazy { FragmentSettingsBinding.inflate(layoutInflater) }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val arrayAdapter =
            ArrayAdapter(requireContext(), android.R.layout.simple_list_item_1, listMenu)
        binding.listView.adapter = arrayAdapter
        binding.listView.onItemClickListener = OnItemClickListener { _, _, i, _ ->
            when (i) {
                0 -> {
                }
                1 -> logout()
                else -> {
                }
            }
        }

        return binding.root
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        val searchView: SearchView = requireActivity().findViewById(R.id.searchView)
        searchView.visibility = View.INVISIBLE
        var item = menu.findItem(R.id.toolbar_menu_add)
        item.isVisible = false
        item = menu.findItem(R.id.toolbar_menu_filter)
        item.isVisible = false
        item = menu.findItem(R.id.toolbar_menu_list)
        item.isVisible = false
        super.onCreateOptionsMenu(menu, inflater)
    }

    private fun logout() {
        AppHelper.logout({ requireActivity().finish() }, { requireActivity().finish() })
    }
}