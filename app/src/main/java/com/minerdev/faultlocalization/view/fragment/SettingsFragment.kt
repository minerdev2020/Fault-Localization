package com.minerdev.faultlocalization.view.fragment

import android.content.Context.MODE_PRIVATE
import android.os.Bundle
import android.util.Log
import android.view.*
import android.widget.AdapterView.OnItemClickListener
import android.widget.ArrayAdapter
import android.widget.SearchView
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.minerdev.faultlocalization.R
import com.minerdev.faultlocalization.databinding.FragmentSettingsBinding
import com.minerdev.faultlocalization.retrofit.AuthRetrofitManager
import com.minerdev.faultlocalization.utils.Constants.TAG
import org.json.JSONObject

class SettingsFragment : Fragment() {
    private val listMenu = listOf("修改个人信息", "退出账号")
    private val binding by lazy { FragmentSettingsBinding.inflate(layoutInflater) }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val arrayAdapter =
            context?.let { ArrayAdapter(it, android.R.layout.simple_list_item_1, listMenu) }
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
        val searchView: SearchView = activity?.findViewById(R.id.searchView) ?: return
        searchView.visibility = View.INVISIBLE
        var item = menu.findItem(R.id.toolbar_menu_add)
        item.isVisible = false
        item = menu.findItem(R.id.toolbar_menu_filter)
        item.isVisible = false
        super.onCreateOptionsMenu(menu, inflater)
    }

    private fun logout() {
        val sharedPreferences = activity?.getSharedPreferences("login", MODE_PRIVATE)
        val id = sharedPreferences?.getString("id", "") ?: ""
        Log.d(TAG, "logout : $id")

        AuthRetrofitManager.instance.logout(id,
            { _: Int, response: String ->
                val data = JSONObject(response)
                Log.d(TAG, "logout response : " + data.getString("message"))
                val editor = sharedPreferences?.edit()
                editor?.clear()
                editor?.apply()
                activity?.finish()
            },
            { code: Int, response: String ->
                val data = JSONObject(response)
                Log.d(TAG, "logout response : " + data.getString("message"))
                when (code) {
                    401 -> {
                        Toast.makeText(context, "该账号已注销！", Toast.LENGTH_SHORT)
                            .show()
                        val editor = sharedPreferences?.edit()
                        editor?.clear()
                        editor?.apply()
                        activity?.finish()
                    }
                    404 -> {
                        Toast.makeText(context, "该账号不存在！", Toast.LENGTH_SHORT)
                            .show()
                        val editor = sharedPreferences?.edit()
                        editor?.clear()
                        editor?.apply()
                        activity?.finish()
                    }
                    else -> {
                    }
                }
            },
            { error: Throwable ->
                Log.d(TAG, "logout error : " + error.localizedMessage)
            }
        )
    }
}