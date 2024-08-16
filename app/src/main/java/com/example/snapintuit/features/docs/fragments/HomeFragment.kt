package com.example.snapintuit.features.docs.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.result.IntentSenderRequest
import com.example.snapintuit.databinding.FragmentHomeBinding
import com.example.snapintuit.features.docs.activities.DocsActivity
import com.example.snapintuit.utils.showToast
import com.google.mlkit.vision.documentscanner.GmsDocumentScannerOptions
import com.google.mlkit.vision.documentscanner.GmsDocumentScanning

class HomeFragment : Fragment() {
    lateinit var binding: FragmentHomeBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentHomeBinding.inflate(layoutInflater,container,false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val options = GmsDocumentScannerOptions.Builder()
            .setGalleryImportAllowed(true)
            .setPageLimit(1)
            .setResultFormats(GmsDocumentScannerOptions.RESULT_FORMAT_PDF)
            .setScannerMode(GmsDocumentScannerOptions.SCANNER_MODE_FULL)
            .build()

        val scanner = GmsDocumentScanning.getClient(options)

        binding.scanButton.setOnClickListener {
            scanner.getStartScanIntent(requireActivity()).addOnSuccessListener { intentSender ->
                (activity as DocsActivity).scannerLauncher.launch(
                    IntentSenderRequest.Builder(intentSender).build()
                )
            }.addOnFailureListener {
                it.printStackTrace()
                context?.showToast(it.message.toString())
            }
        }

    }
}