package com.minerdev.faultlocalization.view.activity

import android.os.Bundle
import android.text.InputFilter
import android.text.InputFilter.LengthFilter
import android.util.Log
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.snackbar.Snackbar
import com.minerdev.faultlocalization.databinding.ActivityRegisterBinding
import com.minerdev.faultlocalization.model.Person
import com.minerdev.faultlocalization.retrofit.AuthRetrofitManager
import com.minerdev.faultlocalization.utils.Constants
import org.json.JSONObject
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
            AuthRetrofitManager.instance.register(id, pw, name, phone,
                { response: String ->
                    run {
                        val data = JSONObject(response)
                        Log.d(Constants.TAG, "tryRegister response : " + data.getString("message"))
                        when (data.getInt("result")) {
                            201 -> {
                                finish()
                            }
                            else -> {
                                Snackbar.make(view, "账号已存在！", Snackbar.LENGTH_LONG).show()
                            }
                        }
                    }
                },
                { error: Throwable ->
                    run {
                        Log.d(Constants.TAG, "tryRegister error : " + error.localizedMessage)
                    }
                }
            )
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
        binding.etId.filters = arrayOf(InputFilter { charSequence, _, _, _, _, _ ->
            val pattern = Pattern.compile("^[a-zA-Z0-9]*$")
            if (charSequence == "" || pattern.matcher(charSequence).matches()) {
                charSequence
            } else ""
        }, LengthFilter(8))

        binding.etPw.filters = arrayOf(InputFilter { charSequence, _, _, _, _, _ ->
            val pattern = Pattern.compile("^[a-zA-Z0-9]*$")
            if (charSequence == "" || pattern.matcher(charSequence).matches()) {
                charSequence
            } else ""
        }, LengthFilter(8))
    }
}