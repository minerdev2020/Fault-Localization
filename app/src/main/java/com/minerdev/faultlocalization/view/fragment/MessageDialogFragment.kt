package com.minerdev.faultlocalization.view.fragment

import android.content.Context
import android.graphics.Color
import android.graphics.Point
import android.graphics.drawable.ColorDrawable
import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import androidx.fragment.app.DialogFragment
import com.minerdev.faultlocalization.databinding.FragmentMessageDialogBinding
import com.minerdev.faultlocalization.model.Message

class MessageDialogFragment(private val message: Message) : DialogFragment() {
    private val binding by lazy { FragmentMessageDialogBinding.inflate(layoutInflater) }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        dialog?.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.tvState.text = message.state.name
        binding.tvFrom.text = message.from.name
        binding.tvType.text = message.type.name

        val estimatedTime = "${message.estimated_time} 小时"
        binding.tvEstimatedTime.text = estimatedTime

        binding.tvReplyer.text = message.replyer?.name ?: "未被回答"
        binding.tvTarget.text = message.equipment_info.name

        binding.textInputEtContents.setText(message.contents)

        binding.btnBack.setOnClickListener {
            dismiss()
        }
    }

    override fun onResume() {
        super.onResume()
        context?.dialogFragmentResize(this, 0.9f, null)
    }

    // 확장 함수
    private fun Context.dialogFragmentResize(
        dialogFragment: DialogFragment,
        width: Float?,
        height: Float?
    ) {
        val windowManager = getSystemService(Context.WINDOW_SERVICE) as WindowManager

        if (Build.VERSION.SDK_INT < 30) {
            val display = windowManager.defaultDisplay
            val size = Point()

            display.getSize(size)

            val window = dialogFragment.dialog?.window
            val params = dialogFragment.dialog?.window?.attributes

            val x = width?.let { (size.x * it).toInt() } ?: params?.width ?: 0
            val y = height?.let { (size.y * it).toInt() } ?: params?.height ?: 0
            window?.setLayout(x, y)

        } else {
            val rect = windowManager.currentWindowMetrics.bounds
            val window = dialogFragment.dialog?.window
            val params = dialogFragment.dialog?.window?.attributes

            val x = width?.let { (rect.width() * it).toInt() } ?: params?.width ?: 0
            val y = height?.let { (rect.width() * it).toInt() } ?: params?.height ?: 0

            window?.setLayout(x, y)
        }
    }
}