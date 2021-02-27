package com.minerdev.faultlocalization.custom

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentStatePagerAdapter
import java.util.*

class SectionPageAdapter(fm: FragmentManager?) : FragmentStatePagerAdapter(
    fm!!
) {
    private val fragmentList: ArrayList<NetworkFragment> = ArrayList<NetworkFragment>()
    private val fragmentTitleList = ArrayList<String>()
    fun addFragment(fragment: NetworkFragment, title: String) {
        fragmentList.add(fragment)
        fragmentTitleList.add(title)
    }

    override fun getPageTitle(position: Int): CharSequence? {
        return fragmentTitleList[position]
    }

    override fun getItem(position: Int): Fragment {
        return fragmentList[position]
    }

    override fun getCount(): Int {
        return fragmentList.size
    }

    fun getNetworkSetting(position: Int): NetworkSetting {
        return fragmentList[position].getNetworkSetting()
    }
}