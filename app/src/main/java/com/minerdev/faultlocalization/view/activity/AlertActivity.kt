package com.minerdev.faultlocalization.view.activity

import android.app.Activity
import android.content.Intent
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.util.Log
import com.minerdev.faultlocalization.databinding.ActivityAlertBinding
import com.minerdev.faultlocalization.model.Alert
import com.minerdev.faultlocalization.utils.Constants
import com.minerdev.faultlocalization.utils.Time
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.json.Json

class AlertActivity : Activity() {
    private val binding by lazy { ActivityAlertBinding.inflate(layoutInflater) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        val intent = intent
        Log.d(Constants.TAG, "data: " + intent.getStringExtra("data"))

        dialogActivityResize(0.9f, null)
        window.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))

        val data = intent.getStringExtra("data") ?: ""
        if (data.isNotEmpty()) {
            val alert = Json.decodeFromString<Alert>(data)
            binding.tvId.text = alert.id.toString()
            binding.tvCreatedAt.text = Time.getShortDate(alert.createdAt)
            binding.tvUpdatedAt.text = Time.getShortDate(alert.updatedAt)
            binding.tvState.text = alert.state.name
            binding.tvType.text = alert.type.name
            binding.tvTarget.text = alert.breakdown_info.name
            binding.tvBreakdownCause.text = alert.breakdown_cause
        }

        binding.btnBack.setOnClickListener {
            finish()
        }
    }

    override fun onNewIntent(intent: Intent?) {
        super.onNewIntent(intent)
    }

    private fun dialogActivityResize(width: Float?, height: Float?) {
        val displayMetrics = applicationContext.resources.displayMetrics

        window.attributes.width =
            if (width != null)
                (displayMetrics.widthPixels * width).toInt()
            else
                window.attributes.width

        window.attributes.height =
            if (height != null)
                (displayMetrics.heightPixels * height).toInt()
            else
                window.attributes.height
    }
}