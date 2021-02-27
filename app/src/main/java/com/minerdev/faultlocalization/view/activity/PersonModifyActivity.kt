package com.minerdev.faultlocalization.view.activity

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.minerdev.faultlocalization.databinding.ActivityPersonModifyBinding

class PersonModifyActivity : AppCompatActivity() {
    private val binding by lazy { ActivityPersonModifyBinding.inflate(layoutInflater) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
    }
}