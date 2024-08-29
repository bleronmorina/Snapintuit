package com.example.snapintuit.features.docs.fragments

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.snapintuit.R
import com.example.snapintuit.databinding.FragmentProfileBinding
import com.example.snapintuit.features.auth.activities.AuthActivity
import com.example.snapintuit.features.base.BaseFragment
import com.example.snapintuit.features.docs.viewmodel.DocViewModel
import com.example.snapintuit.network.ApiService
import com.example.snapintuit.repositories.DocRepository
import com.example.snapintuit.utils.UserPreferences


class ProfileFragment : BaseFragment<DocViewModel, FragmentProfileBinding, DocRepository>() {
    lateinit var userPreferences: UserPreferences

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        userPreferences = UserPreferences(requireContext())
        binding.nameTextView.text = userPreferences.getUserName()
        binding.emailTextView.text = userPreferences.getUserEmail()

        binding.logoutButton.setOnClickListener {
            val dialog = androidx.appcompat.app.AlertDialog.Builder(requireContext())
                .setTitle("Logout")
                .setMessage("Are you sure you want to logout?")
                .setPositiveButton("Yes") { dialog, which ->
                    userPreferences.clear()
                    val intent = Intent(requireContext(), AuthActivity::class.java)
                    startActivity(intent)
                    requireActivity().finish()
                }
                .setNegativeButton("No") { dialog, which -> }
                .create()
            dialog.show()

        }
        binding.backButton.setOnClickListener {
            requireActivity().onBackPressed()
        }

    }
    override fun getViewModel() = DocViewModel::class.java
    override fun getFragmentBinding(
        inflater: LayoutInflater,
        container: ViewGroup?
    ) = FragmentProfileBinding.inflate(inflater, container, false)

    override fun getFragmentRepository() =  DocRepository(remoteDataSource.buildApi(ApiService::class.java))

}