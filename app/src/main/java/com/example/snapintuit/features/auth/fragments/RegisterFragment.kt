package com.example.snapintuit.features.auth.fragments

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AlertDialog
import androidx.lifecycle.Observer
import com.example.snapintuit.databinding.FragmentRegisterBinding
import com.example.snapintuit.features.auth.viewmodel.AuthViewModel
import com.example.snapintuit.features.base.BaseFragment
import com.example.snapintuit.features.docs.activities.DocsActivity
import com.example.snapintuit.models.RegisterRequest
import com.example.snapintuit.network.ApiService
import com.example.snapintuit.network.Resource
import com.example.snapintuit.repositories.AuthRepository
import com.example.snapintuit.utils.UserPreferences
import com.example.snapintuit.utils.safeNavigate
import com.example.snapintuit.utils.showToast

class RegisterFragment : BaseFragment<AuthViewModel, FragmentRegisterBinding, AuthRepository>() {
    private lateinit var userPreferences: UserPreferences


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel.authResponse.observe(viewLifecycleOwner, Observer {
            when(it) {
                is Resource.Success -> {
                    val userId = it.data.user.id
                    userPreferences = UserPreferences(requireContext())
                    userPreferences.saveUserId(userId)
                    context?.showToast("Register successful")
                    val intent = Intent(activity, DocsActivity::class.java)
                    startActivity(intent)
                    activity?.finish()
                }

                is Resource.Failure -> {
                    AlertDialog.Builder(requireContext())
                        .setTitle("Registration Failed")
                        .setIcon(android.R.drawable.ic_dialog_alert)
                        .setMessage("An error occurred while registering")
                        .setPositiveButton("Ok") { dialog, which -> }
                        .show()
                    binding.progressBar.visibility = View.GONE
                    binding.signUpButton.text = "Sign Up"

                }

                Resource.Loading -> {


                }
            }
        })

        binding.signUpButton.setOnClickListener {
            val name = binding.nameInput.editText?.text.toString()
            val email = binding.emailInput.editText?.text.toString()
            val password = binding.passwordInput.editText?.text.toString()
            val confirmPassword = binding.confirmPasswordInput.editText?.text.toString()

            if (password != confirmPassword) {
                context?.showToast("The passwords do not match!")
                return@setOnClickListener
            }
            binding.progressBar.visibility = View.VISIBLE
            binding.signUpButton.text = ""
            viewModel.register(name, email, password)

        }

        binding.loginText.setOnClickListener {
            safeNavigate(RegisterFragmentDirections.actionRegisterFragmentToLoginFragment())
        }

    }

    override fun getViewModel() = AuthViewModel::class.java

    override fun getFragmentBinding(
        inflater: LayoutInflater,
        container: ViewGroup?
    ) = FragmentRegisterBinding.inflate(inflater, container, false)

    override fun getFragmentRepository() =
        AuthRepository(remoteDataSource.buildApi(ApiService::class.java))


}
