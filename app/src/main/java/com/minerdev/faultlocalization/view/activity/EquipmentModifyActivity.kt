package com.minerdev.faultlocalization.view.activity

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.minerdev.faultlocalization.databinding.ActivityEquipModifyBinding

class EquipmentModifyActivity : AppCompatActivity() {
    private val binding by lazy { ActivityEquipModifyBinding.inflate(layoutInflater) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
    }
}