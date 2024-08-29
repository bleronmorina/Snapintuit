package com.example.snapintuit

import android.content.Intent
import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.snapintuit.features.auth.activities.AuthActivity
import com.example.snapintuit.features.docs.activities.DocsActivity
import com.example.snapintuit.utils.UserPreferences

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_main)

        var userPreferences = UserPreferences(this)
        if(userPreferences.getUserId() == -1) {
            val intent = Intent(this, AuthActivity::class.java)
            startActivity(intent)
        }
        else {
            val intent = Intent(this, DocsActivity::class.java)
            startActivity(intent)
        }
        finish()
    }
}