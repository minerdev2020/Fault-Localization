package com.minerdev.faultlocalization.adapter

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.minerdev.faultlocalization.model.Equipment
import com.minerdev.faultlocalization.view.fragment.EquipmentInfoFragment
import com.minerdev.faultlocalization.view.fragment.SensorDataPageFragment

class SensorDataPageAdapter(fa: FragmentActivity) :
    FragmentStateAdapter(fa) {
    var equipment = Equipment()

    override fun getItemCount(): Int {
        return equipment.sensor_info.size + 1
    }

    override fun createFragment(position: Int): Fragment {
        return if (position == 0) {
            EquipmentInfoFragment(equipment)

        } else {
            SensorDataPageFragment(equipment.sensor_info[position - 1])
        }
    }
}