package com.example.snapintuit.features.docs.fragments

import android.graphics.Bitmap
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.webkit.WebChromeClient
import android.webkit.WebSettings
import android.webkit.WebView
import android.webkit.WebViewClient
import androidx.core.net.toUri
import androidx.fragment.app.clearFragmentResult
import com.example.snapintuit.databinding.FragmentDocViewBinding
import com.example.snapintuit.features.base.BaseFragment
import com.example.snapintuit.features.docs.viewmodel.DocViewModel
import com.example.snapintuit.network.ApiService
import com.example.snapintuit.network.Resource
import com.example.snapintuit.repositories.DocRepository
import com.example.snapintuit.utils.UserPreferences
import com.example.snapintuit.utils.getFileFromUri
import com.example.snapintuit.utils.safeNavigate
import com.github.barteksc.pdfviewer.util.FitPolicy
import com.google.gson.Gson
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.asRequestBody
import okhttp3.RequestBody.Companion.toRequestBody
import java.io.File
import java.io.FileOutputStream
import java.net.HttpURLConnection
import java.net.URL
import java.net.URLEncoder


class DocViewFragment : BaseFragment<DocViewModel, FragmentDocViewBinding, DocRepository>() {
    lateinit var userPreferences: UserPreferences
    lateinit var args: Bundle

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        userPreferences = UserPreferences(requireContext())

        val pdfUriStr = arguments?.getString("pdfUri")
        val pdfUrl = arguments?.getString("pdfUrl")

        args = Bundle()
        args.putInt("pdfId", arguments?.getInt("pdfId")!!)

        if (pdfUriStr != null) {
            val pdfUri = pdfUriStr.toUri()
            displayPdfFromUri(pdfUri)
            uploadDocument(pdfUri)
        }
        if (pdfUrl != null) {
            Log.d("DocViewFragment", "Pdf Url: $pdfUrl")
            displayPdfFromUrl(pdfUrl)

        }

        binding.aiButton.setOnClickListener {
            safeNavigate(DocViewFragmentDirections.actionDocViewToBottomSheetFragment(), args)
        }

        binding.backButton.setOnClickListener() {

            safeNavigate(DocViewFragmentDirections.actionDocViewToHome())
        }
    }


    override fun getViewModel() = DocViewModel::class.java

    override fun getFragmentBinding(
        inflater: LayoutInflater,
        container: ViewGroup?
    ) = FragmentDocViewBinding.inflate(inflater, container, false)

    override fun getFragmentRepository() =
        DocRepository(remoteDataSource.buildApi(ApiService::class.java))


    private fun uploadDocument(uri: Uri) {
        binding.progressBar.visibility = View.VISIBLE
        binding.aiButton.pointerIcon = null
        binding.aiButton.isClickable = false
        binding.aiButton.text = ""
        val file = getFileFromUri(uri, requireContext())
        val filePart = MultipartBody.Part.createFormData("file", file.name, file.asRequestBody())
        val namePart = "document_name".toRequestBody("text/plain".toMediaTypeOrNull())
        val userIdPart =
            userPreferences.getUserId().toString().toRequestBody("text/plain".toMediaTypeOrNull())

        viewModel.uploadDoc(filePart, namePart, userIdPart)

        viewModel.uploadResponse.observe(
            viewLifecycleOwner
        ) { response ->
            when (response) {
                is Resource.Success -> {
                    val uploadResponse = response.data
                    Log.d("DocViewFragment", "Upload Response: ${Gson().toJson(uploadResponse)}")
                    binding.aiButton.text = " More with AI"
                    binding.aiButton.isClickable = true
                    binding.progressBar.visibility = View.GONE
                    viewModel.isFileUploaded = true
                    var pdfID = uploadResponse.id
                    args.putInt("pdfId", pdfID)

                }

                is Resource.Failure -> {
                    Log.d("DocViewFragment", "Upload Error: ${response.errorBody}")
                }

                is Resource.Loading -> {
                    Log.d("DocViewFragment", "Uploading...")
                }
            }
        }
    }

    private fun displayPdfFromUri(pdfUri: Uri) {
        binding.pdfView.visibility = View.VISIBLE
        binding.webView.visibility = View.GONE
        binding.pdfView.fromUri(pdfUri)
            .enableSwipe(true)
            .swipeHorizontal(false)
            .enableDoubletap(true)
            .defaultPage(0)
            .enableAnnotationRendering(false)
            .password(null)
            .scrollHandle(null)
            .enableAntialiasing(true)
            .spacing(0)
            .autoSpacing(false)
            .pageFitPolicy(FitPolicy.WIDTH)
            .pageSnap(true)
            .pageFling(false)
            .nightMode(false)
            .load()
    }

    private fun displayPdfFromUrl(pdfUrl: String) {
        binding.pdfView.visibility = View.VISIBLE
        CoroutineScope(Dispatchers.IO).launch {
            try {
                val file = downloadPdf(pdfUrl)
                withContext(Dispatchers.Main) {
                    binding.pdfView.fromFile(file).load()
                }
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }

    private fun downloadPdf(pdfUrl: String): File {
        val file = File(requireContext().cacheDir, "temp.pdf")
        val url = URL(pdfUrl)
        val connection = url.openConnection() as HttpURLConnection
        connection.inputStream.use { inputStream ->
            FileOutputStream(file).use { outputStream ->
                inputStream.copyTo(outputStream)
            }
        }
        return file
    }

}