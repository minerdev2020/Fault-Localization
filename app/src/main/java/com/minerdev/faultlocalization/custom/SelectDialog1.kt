package com.minerdev.faultlocalization.custom

import android.R
import android.app.Dialog
import android.content.Context
import android.os.Bundle
import android.view.View
import android.widget.*

class SelectDialog(context: Context) : Dialog(context) {
    private var listener: View.OnClickListener? = null
    private var items1: Array<String>
    private var items2: Array<String>
    private var spinner1: Spinner? = null
    private var spinner2: Spinner? = null
    override fun onCreate(savedInstanceState: Bundle) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.select_dialog)
        spinner1 = findViewById(R.id.select_dialog_spinner1)
        spinner2 = findViewById(R.id.select_dialog_spinner2)
        val adapter1 = ArrayAdapter(context, R.layout.simple_spinner_item, items1)
        adapter1.setDropDownViewResource(R.layout.simple_spinner_dropdown_item)
        spinner1.setAdapter(adapter1)
        val adapter2 = ArrayAdapter(context, R.layout.simple_spinner_item, items2)
        adapter2.setDropDownViewResource(R.layout.simple_spinner_dropdown_item)
        spinner2.setAdapter(adapter2)
        val button_ok = findViewById<Button>(R.id.select_dialog_button_ok)
        button_ok.setOnClickListener { view ->
            if (listener != null) {
                listener!!.onClick(view)
            }
            dismiss()
        }
        val button_cancel = findViewById<Button>(R.id.select_dialog_button_cancel)
        button_cancel.setOnClickListener { dismiss() }
    }

    fun setOnOKButtonClickListener(clickListener: View.OnClickListener?) {
        listener = clickListener
    }

    fun setItems1(items1: Array<String>) {
        this.items1 = items1
    }

    fun setItems2(items2: Array<String>) {
        this.items2 = items2
    }

    val spinner1ItemPosition: Int
        get() = spinner1!!.selectedItemPosition
    val spinner2ItemPosition: Int
        get() = spinner2!!.selectedItemPosition
}