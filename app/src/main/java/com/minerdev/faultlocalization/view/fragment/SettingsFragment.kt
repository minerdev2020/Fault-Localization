package com.minerdev.faultlocalization.view.fragment

import android.content.Context.MODE_PRIVATE
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.*
import android.widget.AdapterView.OnItemClickListener
import android.widget.ArrayAdapter
import android.widget.SearchView
import androidx.fragment.app.Fragment
import com.minerdev.faultlocalization.R
import com.minerdev.faultlocalization.databinding.FragmentSettingsBinding
import com.minerdev.faultlocalization.retrofit.AuthRetrofitManager
import com.minerdev.faultlocalization.utils.Constants.TAG
import com.minerdev.faultlocalization.view.activity.TitleActivity
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
            { response: String ->
                run {
                    val data = JSONObject(response)
                    Log.d(TAG, "logout response : " + data.getString("message"))
                    when (data.getInt("code")) {
                        200 -> {
                            val editor = sharedPreferences?.edit()
                            editor?.clear()
                            editor?.apply()

                            val intent = Intent(context, TitleActivity::class.java)
                            startActivity(intent)
                            activity?.finish()
                        }
                        else -> {
                        }
                    }
                }
            },
            { error: Throwable ->
                run {
                    Log.d(TAG, "logout error : " + error.localizedMessage)
                }
            }
        )
    }
}