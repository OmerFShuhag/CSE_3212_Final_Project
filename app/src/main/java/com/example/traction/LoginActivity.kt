package com.example.traction

import android.widget.Toast
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.sp
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.input.VisualTransformation
import androidx.navigation.NavController
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.ui.text.font.FontWeight
import com.example.traction.ui.theme.DeepTeal
import com.example.traction.ui.theme.LightTeal
import com.example.traction.ui.theme.SoftTeal


@Composable
fun LoginActivity(
    modifier: Modifier = Modifier,
    navController: NavController,
    authViewModel: AuthViewModel,
    databaseViewModel: DatabaseViewModel
) {
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var passwordVisible by remember { mutableStateOf(false) }

    var emailError by remember { mutableStateOf<String?>(null) }
    var passwordError by remember { mutableStateOf<String?>(null) }

    val authState = authViewModel.authState.observeAsState()
    val context = LocalContext.current

    LaunchedEffect(authState.value) {
        when (val state = authState.value) {
            is AuthState.Authenticated -> {
                val userId = FirebaseAuth.getInstance().currentUser
                val db = FirebaseFirestore.getInstance()

                userId?.let {
                    db.collection("user").document(userId.uid).get()
                        .addOnSuccessListener { document ->
                            if (document.exists()) {
                                navController.navigate("homepage")
                            } else {
                                navController.navigate("profile_setup")
                            }
                        }
                }
            }
            is AuthState.Error -> Toast.makeText(context, (state as AuthState.Error).message, Toast.LENGTH_SHORT).show()
            else -> Unit
        }
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(brush = Brush.verticalGradient(colors = listOf(LightTeal, SoftTeal)))
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(24.dp),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            AnimatedGif()
            Text(
                text = "Welcome to Traction",
                style = MaterialTheme.typography.headlineMedium.copy(color = Color.Black), // White text
                modifier = Modifier.padding(bottom = 8.dp)
            )

            Spacer(modifier = Modifier.height(16.dp))

            // Email TextField
            OutlinedTextField(
                value = email,
                onValueChange = {
                    email = it
                    emailError = Validator.validateEmail(it)
                },
                label = { Text("Email") },
                isError = emailError != null,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 8.dp),
                shape = RoundedCornerShape(12.dp),
                colors = OutlinedTextFieldDefaults.colors(
                    focusedLabelColor = DeepTeal,
                    unfocusedLabelColor = DeepTeal,
                    focusedBorderColor = DeepTeal,
                    unfocusedBorderColor = DeepTeal,

                    focusedTextColor = DeepTeal,
                    unfocusedTextColor = DeepTeal,

                )
            )
            emailError?.let { Text(it, color = Color(0xFFFF6F61)) } // Red error text

            Spacer(modifier = Modifier.height(16.dp))

            // Password TextField
            OutlinedTextField(
                value = password,
                onValueChange = {
                    password = it
                    passwordError = Validator.validatePassword(it)
                },
                label = { Text("Password") },
                isError = passwordError != null,
                visualTransformation = if (passwordVisible) VisualTransformation.None else PasswordVisualTransformation(),
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 8.dp),
                shape = RoundedCornerShape(12.dp),
                trailingIcon = {
                    IconButton(onClick = { passwordVisible = !passwordVisible }) {
                        val icon = if (passwordVisible) R.drawable.visible else R.drawable.invisible
                        Icon(
                            painter = painterResource(id = icon),
                            contentDescription = if (passwordVisible) "Hide password" else "Show password",

                        )
                    }
                },
                colors = OutlinedTextFieldDefaults.colors(
                    focusedLabelColor = DeepTeal,
                    unfocusedLabelColor = DeepTeal,
                    focusedBorderColor = DeepTeal,
                    unfocusedBorderColor = DeepTeal,

                    focusedTextColor = DeepTeal,
                    unfocusedTextColor = DeepTeal,
                )
            )
            passwordError?.let { Text(it, color = Color(0xFFFF6F61)) }

            Spacer(modifier = Modifier.height(24.dp))

            // Log In Button
            Button(
                onClick = {
                    authViewModel.login(email, password,navController)
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(50.dp),
                colors = ButtonDefaults.buttonColors(containerColor = DeepTeal)
            ) {

                Text(text = "Log In", color = Color.White, fontSize = 16.sp)
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Forgot Password TextButton
            TextButton(
                onClick = {
                    navController.navigate("forget_pass")
                },
                modifier = Modifier.align(Alignment.CenterHorizontally)
            ) {
                Text(text = "Forgot Password?", color = Color.Black)
            }
        }

        // Sign Up Section
        Row(
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .padding(bottom = 20.dp),
            horizontalArrangement = Arrangement.Center,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = "Don't have an account?",
                color = Color.Black.copy(alpha = 0.8f),
            )
            TextButton(onClick = {
                navController.navigate("signup")
            }) {
                Text(text = "Sign Up", color = DeepTeal, fontWeight = FontWeight.Bold)
            }
        }

    }
}

@Composable
fun AnimatedGif(){


    Image(
        painter = painterResource(id = R.drawable.authentication),
        contentDescription = "ANimated GIf",
        modifier = Modifier
            .size(200.dp)
            .padding(bottom = 32.dp)
    )
}



