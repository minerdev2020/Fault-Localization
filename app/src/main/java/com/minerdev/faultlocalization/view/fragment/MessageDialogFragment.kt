package com.minerdev.faultlocalization.view.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.DialogFragment
import androidx.lifecycle.MutableLiveData
import com.minerdev.faultlocalization.R
import com.minerdev.faultlocalization.databinding.FragmentMessageDialogBinding

class MessageDialogFragment(private val state: String) : DialogFragment() {
    val spinnerItem: String
        get() = binding.spn1.selectedItem.toString()

    val spinnerItemPosition: Int
        get() = binding.spn1.selectedItemPosition

    val types = MutableLiveData<ArrayList<String>>()

    private val adapter by lazy {
        ArrayAdapter<String>(
            requireContext(),
            android.R.layout.simple_spinner_dropdown_item
        )
    }

    var listener: View.OnClickListener? = null

    private val binding by lazy { FragmentMessageDialogBinding.inflate(layoutInflater) }

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

        binding.spn1.adapter = adapter

        types.observe(viewLifecycleOwner, {
            adapter.clear()
            adapter.addAll(it)
        })

        binding.btnOk.setOnClickListener {
            if (spinnerItemPosition == 0) {
                return@setOnClickListener
            }

            val builder = AlertDialog.Builder(requireContext())
            builder.setTitle("友情提示")
            builder.setMessage("无效的动作！")
            builder.setIcon(R.drawable.ic_round_warning_24)
            builder.setPositiveButton("返回") { _, _ ->
                return@setPositiveButton
            }
            val alertDialog = builder.create()

            val type = spinnerItem
            when (state) {
                "运行中" -> {
                    if (type != "启动申请") {
                        listener?.onClick(it)
                        dismiss()

                    } else {
                        alertDialog.show()
                    }
                }
                "维修中" -> {
                    if (type != "维修申请") {
                        listener?.onClick(it)
                        dismiss()

                    } else {
                        alertDialog.show()
                    }
                }
                "停用中" -> {
                    if (type != "停用申请") {
                        listener?.onClick(it)
                        dismiss()

                    } else {
                        alertDialog.show()
                    }
                }
                else -> {
                    alertDialog.show()
                }
            }
        }

        binding.btnCancel.setOnClickListener { dismiss() }
    }
}