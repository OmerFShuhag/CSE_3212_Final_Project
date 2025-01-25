package com.example.traction

import AddStudent
import HomePage
import StudentViewModel
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Modifier
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController

@Composable
fun MyAppNavigation(
    modifier: Modifier = Modifier,
    authViewModel: AuthViewModel,
    databaseViewModel: DatabaseViewModel,
    studentViewModel: StudentViewModel
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
            HomePage(modifier,
                navController,
                authViewModel,
                databaseViewModel,
                studentViewModel)
        }
        composable("forget_pass"){
            Forgetpass(modifier, navController, authViewModel)
        }
        composable(
            "profile_setup"
        ){
            ProfileSetup(
                databaseViewModel, navController
            )
        }
        composable("add_student"){
            AddStudent(navController,studentViewModel)
        }

    }
}
