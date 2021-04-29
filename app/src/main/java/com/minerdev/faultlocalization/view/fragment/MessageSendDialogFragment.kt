package com.minerdev.faultlocalization.view.fragment

import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.*
import android.widget.ArrayAdapter
import androidx.appcompat.app.AlertDialog
import androidx.lifecycle.MutableLiveData
import com.minerdev.faultlocalization.R
import com.minerdev.faultlocalization.base.BaseDialogFragment
import com.minerdev.faultlocalization.databinding.FragmentMessageSendDialogBinding

class MessageSendDialogFragment(private val state: String) : BaseDialogFragment() {
    val estimatedTime: Float
        get() = binding.etEstimatedTime.text.toString().toFloat()

    val contents: String
        get() = binding.textInputEtContents.text.toString()

    val spinnerItemPosition: Int
        get() = binding.spnType.selectedItemPosition

    val types = MutableLiveData<ArrayList<String>>()

    private val adapter by lazy {
        ArrayAdapter<String>(
            requireContext(),
            android.R.layout.simple_spinner_dropdown_item
        )
    }

    var listener: View.OnClickListener? = null

    private val binding by lazy { FragmentMessageSendDialogBinding.inflate(layoutInflater) }

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

        binding.spnType.adapter = adapter

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

            val type = binding.spnType.selectedItem
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

    override fun onResume() {
        super.onResume()
        context?.dialogFragmentResize(this, 0.9f, null)
    }
}