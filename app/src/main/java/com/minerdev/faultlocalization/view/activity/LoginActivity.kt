package com.minerdev.faultlocalization.view.activity

import android.app.AlertDialog
import android.content.Intent
import android.net.ConnectivityManager
import android.os.Bundle
import android.text.InputFilter
import android.text.InputFilter.LengthFilter
import android.util.Log
import android.view.KeyEvent
import android.view.View
import android.view.inputmethod.EditorInfo
import android.view.inputmethod.InputMethodManager
import android.widget.TextView.OnEditorActionListener
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import com.google.android.material.snackbar.Snackbar
import com.minerdev.faultlocalization.R
import com.minerdev.faultlocalization.databinding.ActivityLoginBinding
import com.minerdev.faultlocalization.retrofit.AuthRetrofitManager
import com.minerdev.faultlocalization.utils.Constants.FINISH_INTERVAL_TIME
import com.minerdev.faultlocalization.utils.Constants.TAG
import org.json.JSONObject
import java.util.regex.Pattern
import kotlin.system.exitProcess

class LoginActivity : AppCompatActivity() {
    private var backPressedTime: Long = 0
    private val binding by lazy { ActivityLoginBinding.inflate(layoutInflater) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        setupButtons()

        setupEditTexts()

        if (!checkInternetConnection()) {
            val builder = AlertDialog.Builder(this)
            builder.setTitle("友情提示")
            builder.setMessage("未检测到网络，请检查网络连接状态。")
            builder.setIcon(R.drawable.ic_round_error_24)
            builder.setPositiveButton("确认") { _, _ -> finish() }
            val alertDialog = builder.create()
            alertDialog.show()

        } else if (checkLoginStatus()) {
            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
            finish()
        }
    }

    override fun onBackPressed() {
        val tempTime = System.currentTimeMillis()
        val intervalTime = tempTime - backPressedTime

        if (intervalTime in 0..FINISH_INTERVAL_TIME) {
            ActivityCompat.finishAffinity(this)
            System.runFinalization()
            exitProcess(0)

        } else {
            backPressedTime = tempTime
            Toast.makeText(this, "再输入一遍会退出程序。", Toast.LENGTH_SHORT).show()
        }
    }

    private fun tryLogin(view: View, id: String, pw: String) {
        if (id.isNotEmpty() && pw.isNotEmpty()) {
            AuthRetrofitManager.instance.login(id, pw,
                { response: String ->
                    run {
                        val data = JSONObject(response)
                        Log.d(TAG, "tryLogin response : " + data.getString("message"))
                        when (data.getInt("result")) {
                            101 -> {
                                val sharedPreferences = getSharedPreferences("login", MODE_PRIVATE)
                                val editor = sharedPreferences.edit()
                                editor.putString("id", id)
                                editor.apply()

                                val intent = Intent(this@LoginActivity, MainActivity::class.java)
                                startActivity(intent)
                                finish()
                            }
                            102 -> {
                                Snackbar.make(view, "账号或密码有误！", Snackbar.LENGTH_LONG).show()
                                binding.etPw.setText("")
                            }
                            103 -> {
                                Snackbar.make(view, "账号不存在！", Snackbar.LENGTH_LONG).show()
                                binding.etPw.setText("")
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
            Snackbar.make(view, "账号或密码有误！", Snackbar.LENGTH_LONG).show()
        }
    }

    private fun checkLoginStatus(): Boolean {
        val sharedPreferences = getSharedPreferences("login", MODE_PRIVATE)
        return sharedPreferences != null && sharedPreferences.contains("id")
    }

    private fun setupButtons() {
        binding.btnLogin.setOnClickListener { view ->
            val id = binding.etId.text.toString()
            tryLogin(view, id, binding.etPw.text.toString())
        }
        binding.btnRegister.setOnClickListener {
            val intent = Intent(applicationContext, RegisterActivity::class.java)
            startActivity(intent)
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

        binding.etPw.setOnEditorActionListener(OnEditorActionListener { _, i, keyEvent ->
            if (keyEvent != null && keyEvent.keyCode == KeyEvent.KEYCODE_ENTER || i == EditorInfo.IME_ACTION_DONE) {
                val manager = getSystemService(INPUT_METHOD_SERVICE) as InputMethodManager
                manager.hideSoftInputFromWindow(
                    currentFocus!!.windowToken,
                    InputMethodManager.HIDE_NOT_ALWAYS
                )

                binding.btnLogin.performClick()
                return@OnEditorActionListener true
            }
            false
        })
    }

    private fun checkInternetConnection(): Boolean {
        val connectivityManager = getSystemService(CONNECTIVITY_SERVICE) as ConnectivityManager
        val activeNetwork = connectivityManager.activeNetwork
        return activeNetwork != null
    }
}