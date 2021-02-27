package com.minerdev.faultlocalization.view.activity

import android.content.ContentValues
import android.os.Bundle
import android.text.InputFilter
import android.text.InputFilter.LengthFilter
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.snackbar.Snackbar
import com.minerdev.faultlocalization.databinding.ActivityRegisterBinding
import java.util.regex.Pattern

class RegisterActivity : AppCompatActivity() {
    private val binding by lazy { ActivityRegisterBinding.inflate(layoutInflater) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        setupButtons()

        setupEditTexts()
    }

    private fun tryRegister(view: View, id: String, pw: String, name: String, phone: String) {
        if (id.isNotEmpty() && pw.isNotEmpty() && name.isNotEmpty() && phone.isNotEmpty()) {
            val values = ContentValues()
            values.put("id", id)
            values.put("pw", pw)
            values.put("name", name)
            values.put("phone", phone)
            val networkTask = NetworkTask(this,
                "http://192.168.35.141:80/register.php", values, "注册中...",
                object : OnDataReceiveListener() {
                    fun parseData(receivedString: String) {
                        if (receivedString == "register_correct") {
                            finish()
                        } else {
                            Snackbar.make(view, "账号已存在！", Snackbar.LENGTH_LONG).show()
                        }
                    }
                })
            networkTask.execute()
        } else {
            Snackbar.make(view, "有误！", Snackbar.LENGTH_LONG).show()
        }
    }

    private fun setupButtons() {
        binding.btnRegister.setOnClickListener { view ->
            val id = binding.etId.text.toString()
            val pw = binding.etPw.text.toString()
            val name = binding.etName.text.toString()
            val phone = binding.etPhone.text.toString()
            tryRegister(view, id, pw, name, phone)
        }

        binding.btnBack.setOnClickListener { finish() }
    }

    private fun setupEditTexts() {
        binding.etId.filters = arrayOf(InputFilter { charSequence, i, i1, spanned, i2, i3 ->
            val pattern = Pattern.compile("^[a-zA-Z0-9]*$")
            if (charSequence == "" || pattern.matcher(charSequence).matches()) {
                charSequence
            } else ""
        }, LengthFilter(8))

        binding.etPw.filters = arrayOf(InputFilter { charSequence, i, i1, spanned, i2, i3 ->
            val pattern = Pattern.compile("^[a-zA-Z0-9]*$")
            if (charSequence == "" || pattern.matcher(charSequence).matches()) {
                charSequence
            } else ""
        }, LengthFilter(8))
    }
}