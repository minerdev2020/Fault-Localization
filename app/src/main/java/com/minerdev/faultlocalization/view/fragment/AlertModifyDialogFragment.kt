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
import com.minerdev.faultlocalization.databinding.FragmentAlertModifyDialogBinding

class AlertModifyDialogFragment(private val state: String) : BaseDialogFragment() {
    val spinnerItemPosition: Int
        get() = binding.spnType.selectedItemPosition

    val states = MutableLiveData<ArrayList<String>>()

    private val adapter by lazy {
        ArrayAdapter<String>(
            requireContext(),
            android.R.layout.simple_spinner_dropdown_item
        )
    }

    var listener: View.OnClickListener? = null

    private val binding by lazy { FragmentAlertModifyDialogBinding.inflate(layoutInflater) }

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

        states.observe(viewLifecycleOwner, {
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

            val newState = binding.spnType.selectedItem
            when (state) {
                "进行中" -> {
                    if (newState == "已完成") {
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