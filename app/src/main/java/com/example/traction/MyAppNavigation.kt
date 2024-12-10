package com.example.traction

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.traction.ui.theme.TracTionTheme

@Composable
fun MyAppNavigation(modifier: Modifier = Modifier, authViewModel: AuthViewModel) {
    var navController = rememberNavController()

    NavHost(navController = navController, startDestination = "Login", builder = {
        composable("login"){
            LoginActivity(modifier, navController, authViewModel)
        }
        composable("signup"){
            SIgnUpActivity(modifier, navController, authViewModel)
        }
        composable("homepage"){
            HomePage(modifier, navController, authViewModel)
        }
    })
}
