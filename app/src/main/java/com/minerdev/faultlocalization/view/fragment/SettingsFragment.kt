package com.minerdev.faultlocalization.view.fragment

import android.R
import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.view.*
import android.widget.*
import android.widget.AdapterView.OnItemClickListener
import androidx.fragment.app.Fragment
import com.minerdev.faultlocalization.databinding.FragmentSettingsBinding
import com.minerdev.faultlocalization.view.activity.LoginActivity

class SettingsFragment : Fragment() {
    private val binding by lazy { FragmentSettingsBinding.inflate(layoutInflater) }
    private val LIST_MENU = arrayOf<String?>("修改个人信息", "退出账号")

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val arrayAdapter = ArrayAdapter(context!!, R.layout.simple_list_item_1, LIST_MENU)
        binding.listView.adapter = arrayAdapter
        binding.listView.onItemClickListener = OnItemClickListener { adapterView, view, i, l ->
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
        val searchView = activity?.findViewById(R.id.searchView)
        searchView.visibility = View.INVISIBLE
        var item = menu.findItem(R.id.toolbar_menu_add)
        item.isVisible = false
        item = menu.findItem(R.id.toolbar_menu_filter)
        item.isVisible = false
        super.onCreateOptionsMenu(menu, inflater)
    }

    protected fun setNetworkSetting() {
        return
    }

    private fun logout() {
        val sharedPreferences = activity?.getSharedPreferences("login", Activity.MODE_PRIVATE)
        val editor = sharedPreferences?.edit()
        editor?.clear()
        editor?.apply()
        val intent = Intent(context, LoginActivity::class.java)
        startActivity(intent)
        activity?.finish()
    }
}