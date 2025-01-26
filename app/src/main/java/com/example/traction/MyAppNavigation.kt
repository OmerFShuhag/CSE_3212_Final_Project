package com.example.traction

import AddStudent
import HomePage
import StudentViewModel
import android.os.Build
//import androidx.annotation.RequiresApi
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Modifier
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.example.traction.student.AttendanceScreen
import com.example.traction.student.StudentDetail
import com.example.traction.student.editStudent

//@RequiresApi(Build.VERSION_CODES.O)
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
        composable(
            "student_detail/{studentId}",
            //arguments = listOf(navArgument("studentId"){type = NavType.StringType})
        ){backStackEntry ->
            val studentId = backStackEntry.arguments?.getString("studentId")?: return@composable
            StudentDetail(studentId,studentViewModel,navController)

        }
        composable("edit_student/{studentId}"){backStackEntry ->
            val studentId = backStackEntry.arguments?.getString("studentId") ?: ""
            editStudent(studentId, studentViewModel, navController)
        }

        composable("attendance/{studentId}") { backStackEntry ->
            val studentId = backStackEntry.arguments?.getString("studentId") ?: ""
            AttendanceScreen(
                navController = navController,
                studentViewModel = studentViewModel,
                studentId = studentId
            )
        }

    }
}
