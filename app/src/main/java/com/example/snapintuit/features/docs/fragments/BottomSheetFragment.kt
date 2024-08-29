package com.example.snapintuit.features.docs.fragments

import android.app.Dialog
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.snapintuit.databinding.BottomSheetLayoutBinding
import com.example.snapintuit.features.base.BaseFragment
import com.example.snapintuit.features.docs.viewmodel.DocViewModel
import com.example.snapintuit.network.ApiService
import com.example.snapintuit.network.RemoteDataSource
import com.example.snapintuit.repositories.DocRepository
import com.example.snapintuit.utils.safeNavigate
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.android.material.bottomsheet.BottomSheetDialogFragment

class BottomSheetFragment : BottomSheetDialogFragment() {
    lateinit var args: Bundle

    private var _binding: BottomSheetLayoutBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = BottomSheetLayoutBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        var dialog = super.onCreateDialog(savedInstanceState) as BottomSheetDialog
        val heightPx = 1500
        dialog.behavior.peekHeight = heightPx

        return dialog
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.summarizeButton.setOnClickListener {
            goToAIResponseFragment("Summarize")
        }
        binding.legalClausesButton.setOnClickListener {
            goToAIResponseFragment("Legal Clauses")
        }
        binding.reportGenerateButton.setOnClickListener {
            goToAIResponseFragment("Report Generate")
        }

        binding.sendCustomPrompt.setOnClickListener {
            val customPrompt = binding.customPromptInput.text.toString()
            goToAIResponseFragment(customPrompt)
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun goToAIResponseFragment(prompt: String) {
        args = Bundle()
        args.putString("prompt", prompt)
        Log.d("BottomSheetFragment", "Pdf Id: ${requireArguments().getInt("pdfId")}")
        args.putInt("pdfId", requireArguments().getInt("pdfId"))
        safeNavigate(
            BottomSheetFragmentDirections.actionBottomSheetFragmentToAIResponseFragment2(),
            args
        )
    }
}