package com.minerdev.faultlocalization.view.fragment

import android.content.Intent
import android.os.Bundle
import android.view.*
import android.widget.AdapterView.OnItemClickListener
import android.widget.ArrayAdapter
import android.widget.SearchView
import androidx.core.app.ActivityCompat
import androidx.fragment.app.Fragment
import com.minerdev.faultlocalization.R
import com.minerdev.faultlocalization.databinding.FragmentSettingsBinding
import com.minerdev.faultlocalization.service.NotificationService
import com.minerdev.faultlocalization.utils.AppHelper
import com.minerdev.faultlocalization.view.activity.SplashActivity

class SettingsFragment : Fragment() {
    private val listMenu = listOf("修改个人信息", "退出账号", "退出程序")
    private val binding by lazy { FragmentSettingsBinding.inflate(layoutInflater) }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        setHasOptionsMenu(true)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val arrayAdapter =
            ArrayAdapter(requireContext(), android.R.layout.simple_list_item_1, listMenu)
        binding.listView.adapter = arrayAdapter
        binding.listView.onItemClickListener = OnItemClickListener { _, _, position, _ ->
            when (position) {
                0 -> {
                }
                1 -> {
                    requireActivity().stopService(
                        Intent(
                            requireActivity().applicationContext,
                            NotificationService::class.java
                        )
                    )
                    AppHelper.logout(
                        {
                            val intent = Intent(context, SplashActivity::class.java)
                            startActivity(intent)
                            requireActivity().finish()
                        },
                        {
                            val intent = Intent(context, SplashActivity::class.java)
                            startActivity(intent)
                            requireActivity().finish()
                        })
                }
                2 -> {
                    requireActivity().stopService(
                        Intent(
                            requireActivity().applicationContext,
                            NotificationService::class.java
                        )
                    )
                    AppHelper.logout(
                        { ActivityCompat.finishAffinity(requireActivity()) },
                        { ActivityCompat.finishAffinity(requireActivity()) })
                }
                else -> {
                }
            }
        }
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
}