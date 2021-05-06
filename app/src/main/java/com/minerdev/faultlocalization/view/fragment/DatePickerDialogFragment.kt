package com.minerdev.faultlocalization.view.fragment

import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.minerdev.faultlocalization.base.BaseDialogFragment
import com.minerdev.faultlocalization.databinding.FragmentDatePickerDialogBinding

class DatePickerDialogFragment : BaseDialogFragment() {
    val date: String
        get() =
            if (!isNow)
                binding.datePicker.year.toString() + "." +
                        (binding.datePicker.month + 1).toString() + "." +
                        binding.datePicker.dayOfMonth.toString() + " " +
                        binding.timePicker.hour.toString() + ":" +
                        binding.timePicker.minute.toString() + ":00"
            else
                "全部"

    var listener: View.OnClickListener? = null
    private var isNow = false

    private val binding by lazy { FragmentDatePickerDialogBinding.inflate(layoutInflater) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        isCancelable = false
    }

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

        binding.swNow.setOnCheckedChangeListener { _, p1 -> isNow = p1 }

        binding.btnOk.setOnClickListener {
            listener?.onClick(it)
            dismiss()
        }

        binding.btnCancel.setOnClickListener { dismiss() }
    }

    override fun onResume() {
        super.onResume()
        context?.dialogFragmentResize(this, 0.7f, null)
    }
}