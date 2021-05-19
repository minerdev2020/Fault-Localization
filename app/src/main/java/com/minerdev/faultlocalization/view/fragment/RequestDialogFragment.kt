package com.minerdev.faultlocalization.view.fragment

import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.minerdev.faultlocalization.base.BaseDialogFragment
import com.minerdev.faultlocalization.databinding.FragmentRequestDialogBinding
import com.minerdev.faultlocalization.model.Request
import com.minerdev.faultlocalization.utils.Time

class RequestDialogFragment(private val request: Request) : BaseDialogFragment() {
    private val binding by lazy { FragmentRequestDialogBinding.inflate(layoutInflater) }

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

        binding.tvId.text = request.id.toString()
        binding.tvCreatedAt.text = Time.getDate(request.createdAt)
        binding.tvUpdatedAt.text = Time.getDate(request.updatedAt)
        binding.tvState.text = request.state.name
        binding.tvFrom.text = request.from.name
        binding.tvType.text = request.type.name

        val estimatedTime = "${request.estimated_time} 小时"
        binding.tvEstimatedTime.text = estimatedTime

        binding.tvReplyer.text = request.replyer?.name ?: "未被回答"
        binding.tvTarget.text = request.equipment_info.name

        binding.textInputEtContents.setText(request.contents)

        binding.btnBack.setOnClickListener {
            dismiss()
        }
    }

    override fun onResume() {
        super.onResume()
        context?.dialogFragmentResize(this, 0.9f, null)
    }
}