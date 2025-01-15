package com.example.traction

import HomePage
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.example.traction.ui.theme.TracTionTheme

@Composable
fun MyAppNavigation(
    modifier: Modifier = Modifier,
    authViewModel: AuthViewModel,
    databaseViewModel: DatabaseViewModel
) {
    var navController = rememberNavController()
    val authState by authViewModel.authState.observeAsState()
//    val startDestination = when(authState.value){
//        is AuthState.Authenticated -> "homepage"
//        else -> "login"
//    }

    LaunchedEffect(authState) {
        // Check the authState and navigate to the appropriate screen
        when (authState) {
            is AuthState.Authenticated -> {
                navController.navigate("homepage") {
                    // Pop up to login so user cannot go back to the login screen
                    popUpTo("login") { inclusive = true }
                }
            }
            is AuthState.Unauthenticated -> {
                navController.navigate("login") {
                    // Prevent going back to login if user is already logged out
                    popUpTo("login") { inclusive = true }
                }
            }
            else -> Unit
        }
    }

    NavHost(navController = navController, startDestination = "login"){
        composable("login"){
            LoginActivity(
                modifier, 
                navController, 
                authViewModel, 
                databaseViewModel
            )
        }
        composable("signup"){
            SIgnUpActivity(
                modifier, 
                navController, 
                authViewModel
            )
        }
        composable("homepage"){
            HomePage(modifier, navController, authViewModel, databaseViewModel)
        }
        composable("forget_pass"){
            Forgetpass(modifier, navController, authViewModel)
        }
        composable(
            "profile_setup"
        ){
            ProfileSetup(databaseViewModel, navController)
        }

    }
}
