package com.minerdev.faultlocalization.view.fragment

import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.minerdev.faultlocalization.base.BaseDialogFragment
import com.minerdev.faultlocalization.databinding.FragmentMessageDialogBinding
import com.minerdev.faultlocalization.model.Message

class MessageDialogFragment(private val message: Message) : BaseDialogFragment() {
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

        binding.tvId.text = message.id.toString()
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
}