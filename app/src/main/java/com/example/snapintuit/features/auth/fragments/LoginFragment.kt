package com.example.snapintuit.features.auth.fragments



import android.app.AlertDialog
import android.app.Dialog
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer
import com.example.snapintuit.databinding.FragmentLoginBinding
import com.example.snapintuit.features.auth.viewmodel.AuthViewModel
import com.example.snapintuit.features.base.BaseFragment
import com.example.snapintuit.features.docs.activities.DocsActivity
import com.example.snapintuit.network.ApiService
import com.example.snapintuit.network.Resource
import com.example.snapintuit.repositories.AuthRepository
import com.example.snapintuit.utils.UserPreferences
import com.example.snapintuit.utils.safeNavigate
import com.example.snapintuit.utils.showToast

class LoginFragment : BaseFragment<AuthViewModel, FragmentLoginBinding, AuthRepository>() {
    private lateinit var userPreferences: UserPreferences


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.progressBar.visibility = View.GONE

        viewModel.authResponse.observe(viewLifecycleOwner, Observer {
            binding.progressBar.visibility = View.GONE

            when(it){
                is Resource.Success -> {
                    val userId = it.data.user.id
                    userPreferences = UserPreferences(requireContext())
                    userPreferences.saveUserId(userId)
                    userPreferences.saveUser(it.data.user.name, it.data.user.email)
                    val intent = Intent(activity, DocsActivity::class.java)
                    startActivity(intent)
                    activity?.finish()
                }

                is Resource.Failure -> {
                    AlertDialog.Builder(requireContext())
                        .setTitle("Login Failed")
                        .setIcon(android.R.drawable.ic_dialog_alert)
                        .setMessage("Email or password is incorrect")
                        .setPositiveButton("Ok") { dialog, which -> }
                        .show()
                    binding.loginButton.text = "Log In"
                }

                Resource.Loading -> {

                }
            }
        })

        binding.loginButton.setOnClickListener() {
            val email = binding.emailInput.editText?.text.toString()
            val password = binding.passwordInput.editText?.text.toString()

            if (email.isEmpty() || password.isEmpty()) {
                context?.showToast("Please fill in both fields")
                return@setOnClickListener
            }
            binding.progressBar.visibility = View.VISIBLE
            binding.loginButton.text = ""
            viewModel.login(email, password)

        }

        binding.signupText.setOnClickListener {
            safeNavigate(LoginFragmentDirections.actionLoginFragmentToRegisterFragment())
        }


    }



    override fun getViewModel() = AuthViewModel::class.java

    override fun getFragmentBinding(
        inflater: LayoutInflater,
        container: ViewGroup?
    ) = FragmentLoginBinding.inflate(inflater, container, false)

    override fun getFragmentRepository() = AuthRepository(remoteDataSource.buildApi(ApiService::class.java))

}
