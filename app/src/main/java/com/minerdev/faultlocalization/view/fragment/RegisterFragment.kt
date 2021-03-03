package com.minerdev.faultlocalization.view.fragment

import android.os.Bundle
import android.text.InputFilter
import android.text.InputFilter.LengthFilter
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.navigation.NavController
import androidx.navigation.Navigation
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.setupWithNavController
import com.minerdev.faultlocalization.R
import com.minerdev.faultlocalization.databinding.FragmentRegisterBinding
import com.minerdev.faultlocalization.retrofit.AuthRetrofitManager
import com.minerdev.faultlocalization.utils.Constants
import org.json.JSONObject
import java.util.regex.Pattern

class RegisterFragment : Fragment() {
    private val binding by lazy { FragmentRegisterBinding.inflate(layoutInflater) }
    lateinit var navController: NavController

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
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
                                navController.navigate(R.id.action_registerFragment_to_titleFragment)
                            }
                            409 -> {
                                Toast.makeText(context, "该用户已存在！", Toast.LENGTH_SHORT).show()
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
            Toast.makeText(context, "所输入的用户信息不全！", Toast.LENGTH_SHORT).show()
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
            navController.navigate(R.id.action_registerFragment_to_titleFragment)
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