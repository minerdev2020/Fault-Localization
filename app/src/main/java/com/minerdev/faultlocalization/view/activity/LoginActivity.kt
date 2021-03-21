package com.minerdev.faultlocalization.view.activity

import android.content.Intent
import android.os.Bundle
import android.text.InputFilter
import android.text.InputFilter.LengthFilter
import android.view.*
import android.view.inputmethod.EditorInfo
import android.view.inputmethod.InputMethodManager
import android.widget.TextView.OnEditorActionListener
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.minerdev.faultlocalization.databinding.ActivityLoginBinding
import com.minerdev.faultlocalization.utils.AppHelper
import java.util.regex.Pattern

class LoginActivity : AppCompatActivity() {
    private val binding by lazy { ActivityLoginBinding.inflate(layoutInflater) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        supportActionBar?.title = "登录"
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

    private fun tryLogin(userId: String, userPw: String) {
        if (userId.isNotEmpty() && userPw.isNotEmpty()) {
            AppHelper.login(
                userId, userPw,
                {
                    val intent = Intent(this@LoginActivity, MainActivity::class.java)
                    startActivity(intent)
                    finish()
                },
                {
                    binding.textInputEtPw.setText("")
                },
            )

        } else {
            Toast.makeText(this, "账号或密码有误！", Toast.LENGTH_SHORT).show()
        }
    }

    private fun setupButtons() {
        binding.btnLogin.setOnClickListener {
            tryLogin(
                binding.textInputEtId.text.toString(),
                binding.textInputEtPw.text.toString()
            )
        }

        binding.btnBack.setOnClickListener {
            finish()
        }
    }

    private fun setupEditTexts() {
        binding.textInputEtId.filters = arrayOf(InputFilter { charSequence, _, _, _, _, _ ->
            val pattern = Pattern.compile("^[a-zA-Z0-9]*$")
            if (charSequence == "" || pattern.matcher(charSequence).matches()) {
                charSequence
            } else ""
        }, LengthFilter(8))

        binding.textInputEtPw.filters = arrayOf(InputFilter { charSequence, _, _, _, _, _ ->
            val pattern = Pattern.compile("^[a-zA-Z0-9]*$")
            if (charSequence == "" || pattern.matcher(charSequence).matches()) {
                charSequence
            } else ""
        }, LengthFilter(8))

        binding.textInputEtPw.setOnEditorActionListener(OnEditorActionListener { _, i, keyEvent ->
            if (keyEvent != null && keyEvent.keyCode == KeyEvent.KEYCODE_ENTER || i == EditorInfo.IME_ACTION_DONE) {
                val manager = getSystemService(INPUT_METHOD_SERVICE) as InputMethodManager
                manager.hideSoftInputFromWindow(
                    currentFocus?.windowToken,
                    InputMethodManager.HIDE_NOT_ALWAYS
                )

                binding.btnLogin.performClick()
                return@OnEditorActionListener true
            }
            false
        })
    }
}