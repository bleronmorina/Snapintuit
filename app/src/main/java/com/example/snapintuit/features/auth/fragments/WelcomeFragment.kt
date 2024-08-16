package com.example.snapintuit.features.auth.fragments

import android.os.Bundle
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
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_welcome, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding = FragmentWelcomeBinding.bind(view)

        binding.loginButton.setOnClickListener(){
            safeNavigate(WelcomeFragmentDirections.welcomeToLoginFragment())
        }

        binding.registerButton.setOnClickListener(){
            safeNavigate(WelcomeFragmentDirections.actionWelcomeFragmentToRegisterFragment())
        }
    }

}