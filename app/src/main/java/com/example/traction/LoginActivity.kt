package com.example.traction

import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.unit.sp
import androidx.compose.material.icons.Icons
//import androidx.compose.material.icons.filled.Visibility
//import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.res.imageResource
import androidx.compose.foundation.layout.Box
//import androidx.compose.foundation.layout.BoxScopeInstance.align
//import androidx.compose.foundation.layout.FlowRowScopeInstance.align
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.FavoriteBorder
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.input.VisualTransformation
import androidx.navigation.NavController
import com.example.traction.ui.theme.TracTionTheme
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore


@Composable
fun LoginActivity(
    modifier: Modifier = Modifier,
    navController: NavController,
    authViewModel: AuthViewModel,
    databaseViewModel: DatabaseViewModel
){

    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var passwordVisible by remember { mutableStateOf(false) }

    var emailError by remember { mutableStateOf<String?>(null) }
    var passwordError by remember { mutableStateOf<String?>(null) }

    val authState = authViewModel.authState.observeAsState()
    val context = LocalContext.current


    LaunchedEffect(authState.value) {
        when(val state = authState.value){
            is AuthState.Authenticated -> {
                val userId = FirebaseAuth.getInstance().currentUser
                val db = FirebaseFirestore.getInstance()

                userId?.let{
                    db.collection("user").document(userId.uid).get()
                        .addOnSuccessListener{ document ->
                            if (document.exists()){
                                navController.navigate("homepage")
                            }
                            else{
                                navController.navigate("profile_setup")
                            }
                        }

                }


            }
            is AuthState.Error -> Toast.makeText(context, (authState.value as AuthState.Error).message, Toast.LENGTH_SHORT).show()
            else -> Unit
        }
    }


    Box(
        modifier = Modifier
            .fillMaxSize()
            //.padding(16.dp)
            .background(color = Color.White)

    ) {
//        Image(
//            painter = painterResource(R.drawable.a7),
//            contentDescription = null,
//            contentScale = ContentScale.Fit,
//            modifier = Modifier.fillMaxSize()
//                .alpha(0.5f)
//        )
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(20.dp),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(text = "LogIn", style = MaterialTheme.typography.headlineSmall)
            Spacer(modifier = Modifier.height(16.dp))
            OutlinedTextField(
                value = email,
                onValueChange = { email = it
                                emailError = Validator.validateEmail(it)
                                },
                label = { Text("Email") },
                isError = emailError != null,
                modifier = Modifier.fillMaxWidth(),
                colors = OutlinedTextFieldDefaults.colors(
                    focusedLabelColor = Color.Red,
                    unfocusedLabelColor = Color.Cyan,
                    focusedBorderColor = Color.Blue,
                    unfocusedBorderColor = Color.Green
                )
            )
            emailError?.let { Text(it, color = Color.Red) }
            Spacer(modifier = Modifier.height(16.dp))
            OutlinedTextField(
                value = password,
                onValueChange = { password = it
                                passwordError = Validator.validatePassword(it)},
                label = { Text("Password") },
                isError = passwordError != null,
                visualTransformation = if (passwordVisible) VisualTransformation.None else PasswordVisualTransformation(),
                modifier = Modifier.fillMaxWidth(),
                colors = OutlinedTextFieldDefaults.colors(

                    focusedLabelColor = Color.Red,
                    unfocusedLabelColor = Color.Cyan,
                    focusedBorderColor = Color.Blue,
                    unfocusedBorderColor = Color.Green,
                    disabledContainerColor = Color.Cyan,
                    focusedContainerColor = Color.Yellow,
                    unfocusedContainerColor = Color.Yellow
                ),

                trailingIcon = {
                    IconButton(onClick = {passwordVisible = !passwordVisible}) {
                        Icon(
                            imageVector = if (passwordVisible)Icons.Default.Favorite else Icons.Default.FavoriteBorder,
                                    contentDescription = if (passwordVisible) "Hide password" else "Show password"
                        )
                    }
                }
                //enabled = false
            )
            passwordError?.let { Text(it, color = Color.Red) }
            Spacer(modifier = Modifier.height(16.dp))
            Button(
                onClick = {
                    authViewModel.login(email, password)
                },
                modifier = Modifier.fillMaxWidth()
            ) {
                Text(text = "Log In")
            }
            Spacer(modifier = Modifier.height(16.dp))
            TextButton(
                onClick = {
                    navController.navigate("forget_pass")
                }
            ) {
                Text(text = "Forget Password")
            }
        }
        Row(
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .padding(bottom = 100.dp),
            horizontalArrangement = Arrangement.Center
        ) {
            TextButton(onClick = {
                navController.navigate("signup")
            }) {
                Text(text = "Create A New Account")
            }
        }
    }
}
