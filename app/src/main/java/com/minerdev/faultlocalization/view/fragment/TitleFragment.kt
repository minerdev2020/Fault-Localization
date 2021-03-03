package com.minerdev.faultlocalization.view.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.NavController
import androidx.navigation.Navigation
import com.minerdev.faultlocalization.R
import com.minerdev.faultlocalization.databinding.FragmentTitleBinding

class TitleFragment : Fragment() {
    private val binding by lazy { FragmentTitleBinding.inflate(layoutInflater) }
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
        setupButtons()
    }

    private fun setupButtons() {
        binding.btnLogin.setOnClickListener {
            navController.navigate(R.id.action_titleFragment_to_loginFragment)
        }
        binding.btnRegister.setOnClickListener {
            navController.navigate(R.id.action_titleFragment_to_registerFragment)
        }
    }
}