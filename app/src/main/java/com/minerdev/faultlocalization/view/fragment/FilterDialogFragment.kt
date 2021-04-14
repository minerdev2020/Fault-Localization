package com.minerdev.faultlocalization.view.fragment

import android.content.Context
import android.graphics.Color
import android.graphics.Point
import android.graphics.drawable.ColorDrawable
import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import android.widget.ArrayAdapter
import androidx.fragment.app.DialogFragment
import androidx.lifecycle.MutableLiveData
import com.minerdev.faultlocalization.databinding.FragmentFilterDialogBinding

class FilterDialogFragment : DialogFragment() {
    val spinner1ItemPosition: Int
        get() = binding.spn1.selectedItemPosition

    val spinner2ItemPosition: Int
        get() = binding.spn2.selectedItemPosition

    val items1 = MutableLiveData<ArrayList<String>>()
    val items2 = MutableLiveData<ArrayList<String>>()

    private val adapter1 by lazy {
        ArrayAdapter<String>(
            requireContext(),
            android.R.layout.simple_spinner_dropdown_item
        )
    }
    private val adapter2 by lazy {
        ArrayAdapter<String>(
            requireContext(),
            android.R.layout.simple_spinner_dropdown_item
        )
    }

    var listener: View.OnClickListener? = null

    private val binding by lazy { FragmentFilterDialogBinding.inflate(layoutInflater) }

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

        binding.spn1.adapter = adapter1
        binding.spn2.adapter = adapter2

        items1.observe(viewLifecycleOwner, {
            adapter1.clear()
            adapter1.addAll(it)
        })
        items2.observe(viewLifecycleOwner, {
            adapter2.clear()
            adapter2.addAll(it)
        })

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