package com.minerdev.faultlocalization.view.fragment

import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.minerdev.faultlocalization.base.BaseDialogFragment
import com.minerdev.faultlocalization.databinding.FragmentAlertDialogBinding
import com.minerdev.faultlocalization.model.Alert

class AlertDialogFragment(private val alert: Alert) : BaseDialogFragment() {
    private val binding by lazy { FragmentAlertDialogBinding.inflate(layoutInflater) }

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

        binding.tvId.text = alert.id.toString()
        binding.tvState.text = alert.state.name
        binding.tvType.text = alert.type.name
        binding.tvTarget.text = alert.breakdown_info.name
        binding.tvBreakdownCause.text = alert.breakdown_cause

        binding.btnBack.setOnClickListener {
            dismiss()
        }
    }

    override fun onResume() {
        super.onResume()
        context?.dialogFragmentResize(this, 0.9f, null)
    }
}