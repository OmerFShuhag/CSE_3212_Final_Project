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
import kotlinx.coroutines.launch

@Composable
fun MyAppNavigation(
    modifier: Modifier = Modifier,
    authViewModel: AuthViewModel,
    databaseViewModel: DatabaseViewModel
) {
    var navController = rememberNavController()
    val authState by authViewModel.authState.observeAsState()
    val startDestination = when(authState){
        is AuthState.Authenticated -> "homepage"
        else -> "login"
    }


    LaunchedEffect(authState) {

        when (authState) {
            is AuthState.Authenticated -> {
                navController.navigate("homepage") {

                    popUpTo(0) { inclusive = true }
                    launchSingleTop = true
                }
            }
            is AuthState.Unauthenticated -> {
                if(navController.currentDestination?.route != "login"){
                    navController.navigate("login"){
                        popUpTo(0){inclusive = true}
                        launchSingleTop = true
                    }
                }
            }
            else -> Unit
        }
    }

    NavHost(navController = navController, startDestination = startDestination){
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
