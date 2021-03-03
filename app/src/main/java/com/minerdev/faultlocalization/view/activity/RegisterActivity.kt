package com.minerdev.faultlocalization.view.activity

import android.os.Bundle
import android.text.InputFilter
import android.text.InputFilter.LengthFilter
import android.util.Log
import android.view.MenuItem
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.minerdev.faultlocalization.databinding.ActivityRegisterBinding
import com.minerdev.faultlocalization.retrofit.AuthRetrofitManager
import com.minerdev.faultlocalization.utils.Constants
import org.json.JSONObject
import java.util.regex.Pattern

class RegisterActivity : AppCompatActivity() {
    private val binding by lazy { ActivityRegisterBinding.inflate(layoutInflater) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        supportActionBar?.title = "注册"
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        setupButtons()
        setupEditTexts()
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            android.R.id.home -> finish()
            else -> {
            }
        }
        return super.onOptionsItemSelected(item)
    }

    private fun tryRegister(
        id: String,
        pw: String,
        name: String,
        phone: String,
        typeId: Int
    ) {
        if (id.isNotEmpty() && pw.isNotEmpty() && name.isNotEmpty() && phone.isNotEmpty()) {
            AuthRetrofitManager.instance.register(id, pw, name, phone, typeId,
                { response: String ->
                    run {
                        val data = JSONObject(response)
                        Log.d(Constants.TAG, "tryRegister response : " + data.getString("message"))
                        when (data.getInt("code")) {
                            201 -> {
                                finish()
                            }
                            409 -> {
                                Toast.makeText(this@RegisterActivity, "该用户已存在！", Toast.LENGTH_SHORT)
                                    .show()
                            }
                            else -> {
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
            Toast.makeText(this, "所输入的用户信息不全！", Toast.LENGTH_SHORT).show()
        }
    }

    private fun setupButtons() {
        binding.btnRegister.setOnClickListener {
            val id = binding.etId.text.toString()
            val pw = binding.etPw.text.toString()
            val name = binding.etName.text.toString()
            val phone = binding.etPhone.text.toString()
            val typeId = if (binding.radioButtonManager.isChecked) 1 else 2
            tryRegister(id, pw, name, phone, typeId)
        }

        binding.btnBack.setOnClickListener {
            finish()
        }
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