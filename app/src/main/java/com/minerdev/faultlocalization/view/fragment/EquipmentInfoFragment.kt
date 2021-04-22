package com.minerdev.faultlocalization.view.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.minerdev.faultlocalization.databinding.FragmentEquipmentInfoBinding
import com.minerdev.faultlocalization.model.Equipment

class EquipmentInfoFragment(private val equipment: Equipment) : Fragment() {
    private val binding by lazy { FragmentEquipmentInfoBinding.inflate(layoutInflater) }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.tvName.text = equipment.name
        binding.tvState.text = equipment.state.name
        binding.tvBootingCount.text = equipment.booting_count.toString()
        binding.tvLastRepairDate.text = "无"
        binding.tvLastBreakdownCause.text = "无"
    }
}