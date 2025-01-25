package com.example.traction

import StudentViewModel
import android.os.Bundle
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.ui.Modifier

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_main)
        val authViewModel : AuthViewModel by viewModels()
        val databaseViewModel : DatabaseViewModel by viewModels()
        val studentViewModel : StudentViewModel by viewModels()

        setContent {
            Scaffold {
                innerpadding ->
                MyAppNavigation(modifier = Modifier.padding(innerpadding),
                    authViewModel = authViewModel,
                    databaseViewModel = databaseViewModel,
                    studentViewModel = studentViewModel)
            }
        }
    }
}



