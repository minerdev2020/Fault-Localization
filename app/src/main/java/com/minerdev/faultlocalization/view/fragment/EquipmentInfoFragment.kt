package com.minerdev.faultlocalization.view.fragment

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.minerdev.faultlocalization.databinding.FragmentEquipmentInfoDataBinding
import com.minerdev.faultlocalization.model.Equipment
import com.minerdev.faultlocalization.utils.Constants.BASE_URL

class EquipmentInfoFragment(private val equipment: Equipment) : Fragment() {
    private val binding by lazy { FragmentEquipmentInfoDataBinding.inflate(layoutInflater) }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        return binding.root
    }

    @SuppressLint("SetJavaScriptEnabled")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.tvName.text = equipment.name
        binding.tvState.text = equipment.state.name

        binding.webView.settings.javaScriptEnabled = true
        binding.webView.loadUrl(BASE_URL)
    }
}