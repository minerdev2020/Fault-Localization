package com.minerdev.faultlocalization.adapter

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.minerdev.faultlocalization.model.Sensor
import com.minerdev.faultlocalization.view.fragment.SensorDataPageFragment

class SensorDataPageAdapter(fa: FragmentActivity) :
    FragmentStateAdapter(fa) {

    val sensorList = ArrayList<Sensor>()

    override fun getItemCount(): Int {
        return sensorList.size
    }

    override fun createFragment(position: Int): Fragment {
        return SensorDataPageFragment(sensorList[position])
    }
}