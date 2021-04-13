package com.minerdev.faultlocalization.view.fragment

import android.content.Context
import android.graphics.Color
import android.graphics.Point
import android.graphics.drawable.ColorDrawable
import android.os.Build
import android.os.Bundle
import android.view.*
import android.widget.ArrayAdapter
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.DialogFragment
import androidx.lifecycle.MutableLiveData
import com.minerdev.faultlocalization.R
import com.minerdev.faultlocalization.databinding.FragmentSendMessageDialogBinding

class SendMessageDialogFragment(private val state: String) : DialogFragment() {
    val contents: String
        get() = binding.textInputEtContents.text.toString()

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

    private val binding by lazy { FragmentSendMessageDialogBinding.inflate(layoutInflater) }

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

            val type = binding.spn1.selectedItem
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