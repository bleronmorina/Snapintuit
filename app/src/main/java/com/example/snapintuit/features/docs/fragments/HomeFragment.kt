package com.example.snapintuit.features.docs.fragments

import android.app.Activity
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.LinearLayout
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.IntentSenderRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.content.ContextCompat
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.snapintuit.R
import com.example.snapintuit.adapters.PdfAdapter
import com.example.snapintuit.databinding.FragmentHomeBinding
import com.example.snapintuit.features.base.BaseFragment
import com.example.snapintuit.features.docs.activities.DocsActivity
import com.example.snapintuit.features.docs.fragments.HomeFragmentDirections
import com.example.snapintuit.features.docs.viewmodel.DocViewModel
import com.example.snapintuit.models.PdfData
import com.example.snapintuit.network.ApiService
import com.example.snapintuit.network.Resource
import com.example.snapintuit.repositories.DocRepository
import com.example.snapintuit.utils.UserPreferences
import com.example.snapintuit.utils.getFileFromUri
import com.example.snapintuit.utils.safeNavigate
import com.example.snapintuit.utils.showToast
import com.google.gson.Gson
import com.google.mlkit.vision.documentscanner.GmsDocumentScannerOptions
import com.google.mlkit.vision.documentscanner.GmsDocumentScanning
import com.google.mlkit.vision.documentscanner.GmsDocumentScanningResult
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.asRequestBody
import okhttp3.RequestBody.Companion.toRequestBody

class HomeFragment : BaseFragment<DocViewModel, FragmentHomeBinding, DocRepository>() {
    lateinit var pdfAdapter: PdfAdapter
    private lateinit var userPreferences: UserPreferences
    lateinit var scannerLauncher: ActivityResultLauncher<IntentSenderRequest>


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        getDocs()

        setupScanner()
        binding.scanButton.setOnClickListener {
            startDocumentScan()
        }
        binding.profileButton.setOnClickListener() {
            safeNavigate(HomeFragmentDirections.actionHomeToProfileFragment())
        }

        binding.swipeRefreshLayout.setOnRefreshListener {
            viewModel.getDocs(userPreferences.getUserId())
            binding.swipeRefreshLayout.isRefreshing = false
        }

//        val buttonContainer = binding.buttonContainer
//
//        val categories = arrayOf("All", "Biology", "Geography", "Contract")
//
//        for (category in categories) {
//            val button = Button(requireContext())
//            button.background = ContextCompat.getDrawable(requireContext(), R.drawable.category_button_background)
//            button.setTextColor(ContextCompat.getColor(requireContext(), R.color.white))
//            button.text = category
//            button.setOnClickListener {
//
//            }
//            val params = LinearLayout.LayoutParams(
//                LinearLayout.LayoutParams.MATCH_PARENT,
//                LinearLayout.LayoutParams.MATCH_PARENT
//            )
//            params.setMargins(8, 0, 8, 0)
//            buttonContainer.addView(button, params)
//        }


    }



    override fun getViewModel() = DocViewModel::class.java

    override fun getFragmentBinding(
        inflater: LayoutInflater,
        container: ViewGroup?
    ) = FragmentHomeBinding.inflate(inflater, container, false)

    override fun getFragmentRepository() =
        DocRepository(remoteDataSource.buildApi(ApiService::class.java))


    private fun setupScanner() {
        scannerLauncher = registerForActivityResult(
            ActivityResultContracts.StartIntentSenderForResult()
        ) { result ->
            if (result.resultCode == Activity.RESULT_OK) {
                val scanningResult =
                    GmsDocumentScanningResult.fromActivityResultIntent(result.data)
                scanningResult?.pdf?.let { pdf ->
                    val bundle = Bundle().apply {
                        putString("pdfUri", pdf.uri.toString())
                    }

                    safeNavigate(HomeFragmentDirections.actionHomeToDocView(), bundle)
                }
            }
        }
    }

    private fun startDocumentScan() {
        val options = GmsDocumentScannerOptions.Builder()
            .setGalleryImportAllowed(true)
            .setPageLimit(5)
            .setGalleryImportAllowed(true)
            .setResultFormats(GmsDocumentScannerOptions.RESULT_FORMAT_PDF)
            .setScannerMode(GmsDocumentScannerOptions.SCANNER_MODE_FULL)
            .build()

        val scanner = GmsDocumentScanning.getClient(options)
        scanner.getStartScanIntent(requireActivity()).addOnSuccessListener { intentSender ->
            scannerLauncher.launch(IntentSenderRequest.Builder(intentSender).build())
        }.addOnFailureListener { exception ->
            exception.printStackTrace()
            context?.showToast(exception.message.toString())
        }
    }


    private fun getDocs() {
        viewModel.docs.observe(viewLifecycleOwner, Observer {
            when (it) {
                is Resource.Success -> {
                    val pdfDataList = it.data
                    pdfAdapter = PdfAdapter(pdfDataList)
                    pdfAdapter.customListener = object : PdfAdapter.CustomListener {
                        override fun onItemClicked(pdfData: PdfData) {
                            Log.d("HomeFragment", "PdfData: ${Gson().toJson(pdfData)}")
                            val bundle = Bundle().apply {
                                putString("pdfUrl", pdfData.originalUrl)
                                putInt("pdfId", pdfData.id)
                            }
                            safeNavigate(HomeFragmentDirections.actionHomeToDocView(), bundle)
                        }
                    }
                    binding.pdfRecyclerView.apply {
                        visibility = View.VISIBLE
                        layoutManager = LinearLayoutManager(context)
                        adapter = pdfAdapter

                    }
                    binding.progressBar.visibility = View.GONE
                }

                is Resource.Failure -> {
                    context?.showToast("Error: ${it.errorCode}, Network error: ${it.isNetworkFailure}")
                    binding.progressBar.visibility = View.GONE
                }

                else -> {
                    context?.showToast("Unknown error")
                    binding.progressBar.visibility = View.GONE
                }
            }
        })
        userPreferences = UserPreferences(requireContext())
        viewModel.getDocs(userPreferences.getUserId())
    }
}


