package com.example.snapintuit.utils

import android.content.Context
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.navigation.NavController
import androidx.navigation.NavDirections
import androidx.navigation.fragment.findNavController


fun Context.showToast (message: String){
    Toast.makeText(this, message, Toast.LENGTH_LONG).show()
}

fun Fragment.safeNavigate(destination: NavDirections) = with(findNavControllerSafely()) {
    this?.currentDestination?.getAction(destination.actionId)
        ?.let { navigate(destination) }
}

private fun Fragment.findNavControllerSafely(): NavController? {
    return if (isAdded) {
        findNavController()
    } else {
        null
    }
}