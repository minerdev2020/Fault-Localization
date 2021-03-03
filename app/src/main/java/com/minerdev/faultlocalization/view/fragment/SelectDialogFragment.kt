package com.minerdev.faultlocalization.view.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import androidx.fragment.app.DialogFragment
import com.minerdev.faultlocalization.databinding.SelectDialogBinding

class SelectDialogFragment : DialogFragment() {
    val spinner1ItemPosition: Int
        get() = binding.spn1.selectedItemPosition

    val spinner2ItemPosition: Int
        get() = binding.spn2.selectedItemPosition

    var listener: View.OnClickListener? = null
    var items1: List<String>? = null
    var items2: List<String>? = null

    private val binding by lazy { SelectDialogBinding.inflate(layoutInflater) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        isCancelable = false
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val adapter1 = context?.let {
            ArrayAdapter<String>(
                it,
                android.R.layout.simple_spinner_dropdown_item
            )
        }

        items1?.let { adapter1?.addAll(it) }
        binding.spn1.adapter = adapter1

        val adapter2 = context?.let {
            ArrayAdapter<String>(
                it,
                android.R.layout.simple_spinner_dropdown_item
            )
        }

        items2?.let { adapter2?.addAll(it) }
        binding.spn2.adapter = adapter2

        binding.btnOk.setOnClickListener {
            listener?.onClick(it)
            dismiss()
        }

        binding.btnCancel.setOnClickListener { dismiss() }
    }
}