package com.example.snapintuit.features.docs.activities

import android.os.Bundle
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import com.example.snapintuit.R
import com.example.snapintuit.features.docs.viewmodel.DocViewModel

class DocsActivity : AppCompatActivity() {

    val docsDocViewModel : DocViewModel by viewModels()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_docs)



    }
}