package com.minerdev.faultlocalization.view.fragment

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.minerdev.faultlocalization.databinding.FragmentSensorDataBinding
import com.minerdev.faultlocalization.model.Sensor
import com.minerdev.faultlocalization.utils.Constants.BASE_URL

class SensorDataPageFragment(private val sensor: Sensor) : Fragment() {
    private val binding by lazy { FragmentSensorDataBinding.inflate(layoutInflater) }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        return binding.root
    }

    @SuppressLint("SetJavaScriptEnabled")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.tvName.text = sensor.name
        binding.tvState.text = sensor.state.name

        binding.webView.settings.javaScriptEnabled = true
        binding.webView.loadUrl(BASE_URL)
    }
}