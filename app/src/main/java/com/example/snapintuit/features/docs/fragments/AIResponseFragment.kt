package com.example.snapintuit.features.docs.fragments


import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer
import com.example.snapintuit.databinding.FragmentAiResponseBinding
import com.example.snapintuit.features.base.BaseFragment
import com.example.snapintuit.features.docs.viewmodel.DocViewModel
import com.example.snapintuit.network.ApiService
import com.example.snapintuit.network.Resource
import com.example.snapintuit.repositories.DocRepository

class AIResponseFragment : BaseFragment<DocViewModel, FragmentAiResponseBinding, DocRepository>() {

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        var pdfId = arguments?.getInt("pdfId")
        var prompt = arguments?.getString("prompt")

        binding.promptTextView.text = prompt

        if(prompt == "Summarize"){
            prompt = "Summarize the given document"
        } else if(prompt == "Legal Clauses"){
            prompt = "Identify the legal clauses in the document"
        } else if(prompt == "Report Generate"){
            prompt = "Generate a report based on the document"
        }



        binding.progressBar.visibility = View.VISIBLE
        viewModel.getAIResponse(pdfId!!, prompt!!)
        viewModel.aiResponse.observe(viewLifecycleOwner, Observer {
            when(it){
                is Resource.Success -> {
                    binding.responseContentTextView.text = it.data.content
                    binding.progressBar.visibility = View.GONE
                    binding.responseContentTextView.visibility = View.VISIBLE
                }
                is Resource.Failure -> {
                    Log.d("AIResponseFragment", "Error: ${it.errorBody}")
                    binding.responseContentTextView.text = "Failed to get Response"
                    binding.progressBar.visibility = View.GONE

                }

                Resource.Loading -> {
                }
            }
        })

        binding.backButton.setOnClickListener {
            requireActivity().onBackPressed()
        }

    }



    override fun getViewModel() = DocViewModel::class.java

    override fun getFragmentBinding(
        inflater: LayoutInflater,
        container: ViewGroup?
    ) = FragmentAiResponseBinding.inflate(inflater, container, false)

    override fun getFragmentRepository() =
    DocRepository(remoteDataSource.buildApi(ApiService::class.java))
}