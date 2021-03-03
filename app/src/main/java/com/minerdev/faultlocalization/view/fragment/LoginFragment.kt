package com.minerdev.faultlocalization.view.fragment

import android.content.Context.INPUT_METHOD_SERVICE
import android.content.Context.MODE_PRIVATE
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
import androidx.fragment.app.Fragment
import androidx.navigation.NavController
import androidx.navigation.Navigation
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.setupWithNavController
import com.minerdev.faultlocalization.R
import com.minerdev.faultlocalization.databinding.FragmentLoginBinding
import com.minerdev.faultlocalization.retrofit.AuthRetrofitManager
import com.minerdev.faultlocalization.utils.Constants.TAG
import com.minerdev.faultlocalization.utils.Constants.TOKEN
import com.minerdev.faultlocalization.view.activity.MainActivity
import org.json.JSONObject
import java.util.regex.Pattern

class LoginFragment : Fragment() {
    private val binding by lazy { FragmentLoginBinding.inflate(layoutInflater) }
    lateinit var navController: NavController

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val actionBar = activity?.actionBar
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        navController = Navigation.findNavController(view)
        val appBarConfiguration = AppBarConfiguration(navController.graph)
        binding.toolbar.setupWithNavController(navController, appBarConfiguration)

        setupButtons()
        setupEditTexts()
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
                                    activity?.getSharedPreferences("login", MODE_PRIVATE)
                                val editor = sharedPreferences?.edit()
                                editor?.putString("id", id)
                                editor?.putString("token", data.getString("token"))
                                editor?.apply()

                                TOKEN = data.getString("token")
                                Log.d(TAG, "tryLogin response : " + data.getString("token"))

                                val intent = Intent(activity, MainActivity::class.java)
                                startActivity(intent)
                                activity?.finish()
                            }
                            400 -> {
                                Toast.makeText(context, "账号或密码有误！", Toast.LENGTH_SHORT).show()
                                binding.textInputEtPw.setText("")
                            }
                            404 -> {
                                Toast.makeText(context, "账号不存在！", Toast.LENGTH_SHORT).show()
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
            Toast.makeText(context, "账号或密码有误！", Toast.LENGTH_SHORT).show()
        }
    }

    private fun setupButtons() {
        binding.btnLogin.setOnClickListener { view ->
            val id = binding.textInputEtId.text.toString()
            tryLogin(view, id, binding.textInputEtPw.text.toString())
        }

        binding.btnBack.setOnClickListener {
            navController.navigate(R.id.action_loginFragment_to_splashFragment)
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
                val manager = activity?.getSystemService(INPUT_METHOD_SERVICE) as InputMethodManager
                manager.hideSoftInputFromWindow(
                    activity?.currentFocus?.windowToken,
                    InputMethodManager.HIDE_NOT_ALWAYS
                )

                binding.btnLogin.performClick()
                return@OnEditorActionListener true
            }
            false
        })
    }
}