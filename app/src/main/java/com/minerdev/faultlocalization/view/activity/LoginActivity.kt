package com.minerdev.faultlocalization.view.activity

import android.app.AlertDialog
import android.content.ContentValues
import android.content.Intent
import android.os.Bundle
import android.text.InputFilter
import android.text.InputFilter.LengthFilter
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
import com.minerdev.greformanager.utils.Constants.FINISH_INTERVAL_TIME
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

        if (SocketClient.checkInternetState(this) === -1) {
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
        if (!id.isEmpty() && !pw.isEmpty()) {
            val values = ContentValues()
            values.put("id", id)
            values.put("pw", pw)
            val networkTask = NetworkTask(this,
                "http://192.168.35.141:80/login.php", values, "登录中...",
                object : OnDataReceiveListener() {
                    fun parseData(receivedString: String) {
                        when (receivedString) {
                            "login_correct" -> {
                                val sharedPreferences = getSharedPreferences("login", MODE_PRIVATE)
                                val editor = sharedPreferences.edit()
                                editor.putString("id", values["id"].toString())
                                editor.apply()
                                val intent = Intent(this@LoginActivity, MainActivity::class.java)
                                startActivity(intent)
                                finish()
                            }
                            "login_wrong" -> {
                                Snackbar.make(view, "账号或密码有误！", Snackbar.LENGTH_LONG).show()
                                binding.etPw.setText("")
                            }
                            else -> {
                                Snackbar.make(view, "账号不存在！", Snackbar.LENGTH_LONG).show()
                                binding.etPw.setText("")
                            }
                        }
                    }
                })
            networkTask.execute()
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
}