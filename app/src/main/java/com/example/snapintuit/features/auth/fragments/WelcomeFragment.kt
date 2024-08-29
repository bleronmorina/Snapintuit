package com.example.snapintuit.features.auth.fragments

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.snapintuit.R
import com.example.snapintuit.databinding.FragmentWelcomeBinding
import com.example.snapintuit.utils.safeNavigate


class WelcomeFragment : Fragment() {
    lateinit var binding: FragmentWelcomeBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentWelcomeBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.loginButton.setOnClickListener {
            Log.d("WelcomeFragment", "Login Button Clicked")
            safeNavigate(WelcomeFragmentDirections.actionWelcomeFragmentToLoginFragment())
        }

        binding.registerButton.setOnClickListener {
            Log.d("WelcomeFragment", "Register Button Clicked")
            safeNavigate(WelcomeFragmentDirections.actionWelcomeFragmentToRegisterFragment())
        }
        Log.i("TAG", "onViewCreated:${binding.loginButton.height} && ${binding.registerButton.height}")
    }


}