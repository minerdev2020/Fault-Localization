package com.minerdev.faultlocalization.custom

import android.app.Dialog
import android.content.Context
import android.os.Bundle
import android.view.View
import android.widget.ArrayAdapter
import com.minerdev.faultlocalization.databinding.SelectDialogBinding

class SelectDialog(context: Context) : Dialog(context) {
    val spinner1ItemPosition: Int
        get() = binding.spn1.selectedItemPosition

    val spinner2ItemPosition: Int
        get() = binding.spn2.selectedItemPosition

    var listener: View.OnClickListener? = null
    var items1: List<String>? = null
    var items2: List<String>? = null

    private val binding by lazy { SelectDialogBinding.inflate(layoutInflater) }

    override fun onCreate(savedInstanceState: Bundle) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        val adapter1 = ArrayAdapter<String>(context, android.R.layout.simple_spinner_dropdown_item)
        items1?.let { adapter1.addAll(it) }
        binding.spn1.adapter = adapter1

        val adapter2 = ArrayAdapter<String>(context, android.R.layout.simple_spinner_dropdown_item)
        items2?.let { adapter2.addAll(it) }
        binding.spn2.adapter = adapter2

        binding.btnOk.setOnClickListener { view ->
            listener?.onClick(view)
            dismiss()
        }

        binding.btnCancel.setOnClickListener { dismiss() }
    }
}