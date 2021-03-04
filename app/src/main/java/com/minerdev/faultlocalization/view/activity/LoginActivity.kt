package com.minerdev.faultlocalization.view.activity

import android.content.Intent
import android.os.Bundle
import android.text.InputFilter
import android.text.InputFilter.LengthFilter
import android.util.Log
import android.view.*
import android.view.inputmethod.EditorInfo
import android.view.inputmethod.InputMethodManager
import android.widget.TextView.OnEditorActionListener
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.minerdev.faultlocalization.databinding.ActivityLoginBinding
import com.minerdev.faultlocalization.retrofit.AuthRetrofitManager
import com.minerdev.faultlocalization.utils.Constants.TAG
import com.minerdev.faultlocalization.utils.Constants.TOKEN
import org.json.JSONObject
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

    private fun tryLogin(view: View, id: String, pw: String) {
        if (id.isNotEmpty() && pw.isNotEmpty()) {
            AuthRetrofitManager.instance.login(id, pw,
                { response: String ->
                    run {
                        val data = JSONObject(response)
                        Log.d(TAG, "tryLogin response : " + data.getString("message"))
                        when (data.getInt("code")) {
                            200 -> {
                                val sharedPreferences =
                                    getSharedPreferences("login", MODE_PRIVATE)
                                val editor = sharedPreferences.edit()
                                editor.putString("id", id)
                                editor.putString("token", data.getString("token"))
                                editor.apply()

                                TOKEN = data.getString("token")
                                Log.d(TAG, "tryLogin response : " + data.getString("token"))

                                val intent = Intent(this@LoginActivity, MainActivity::class.java)
                                startActivity(intent)
                                finish()
                            }
                            400 -> {
                                Toast.makeText(this@LoginActivity, "账号或密码有误！", Toast.LENGTH_SHORT)
                                    .show()
                                binding.textInputEtPw.setText("")
                            }
                            401 -> {
                                Toast.makeText(this@LoginActivity, "该账号已登录！", Toast.LENGTH_SHORT)
                                    .show()
                                binding.textInputEtPw.setText("")
                            }
                            404 -> {
                                Toast.makeText(this@LoginActivity, "账号不存在！", Toast.LENGTH_SHORT)
                                    .show()
                                binding.textInputEtPw.setText("")
                            }
                            else -> {

                            }
                        }
                    }
                },
                { error: Throwable ->
                    run {
                        Log.d(TAG, "tryLogin error : " + error.localizedMessage)
                    }
                }
            )
        } else {
            Toast.makeText(this, "账号或密码有误！", Toast.LENGTH_SHORT).show()
        }
    }

    private fun setupButtons() {
        binding.btnLogin.setOnClickListener { view ->
            val id = binding.textInputEtId.text.toString()
            tryLogin(view, id, binding.textInputEtPw.text.toString())
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