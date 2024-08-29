package com.example.snapintuit.utils

import android.content.Context
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.navigation.NavController
import androidx.navigation.NavDirections
import androidx.navigation.fragment.findNavController
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import okhttp3.MediaType
import okhttp3.MultipartBody
import okhttp3.RequestBody
import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.io.File
import java.io.FileOutputStream


fun Context.showToast (message: String){
    Toast.makeText(this, message, Toast.LENGTH_LONG).show()
}

fun Fragment.safeNavigate(destination: NavDirections, bundle: Bundle? = null) {
    findNavControllerSafely()?.currentDestination?.getAction(destination.actionId)?.let {
        if (bundle != null) {
            destination.arguments.putAll(bundle)
        }
        findNavController().navigate(destination)
    }
}

private fun Fragment.findNavControllerSafely(): NavController? {
    return if (isAdded) {
        findNavController()
    } else {
        null
    }
}


fun getFileFromUri(uri: Uri, context: Context): File {
    val file = File.createTempFile("tempfile", ".pdf", context.cacheDir)
    context.contentResolver.openInputStream(uri)?.use { inputStream ->
        FileOutputStream(file).use { outputStream ->
            inputStream.copyTo(outputStream)
        }
    }
    return file
}


