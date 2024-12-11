package com.example.traction

import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.FavoriteBorder
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.traction.ui.theme.TracTionTheme


@Composable
fun SIgnUpActivity(modifier: Modifier, navController: NavController, authViewModel: AuthViewModel)
{
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var passwordVisible by remember { mutableStateOf(false) }
    var name by remember { mutableStateOf(("")) }
    var confirmPassword by remember { mutableStateOf("") }

    var emailError by remember { mutableStateOf<String?>(null) }
    var nameError by remember { mutableStateOf<String?>(null) }
    var passwordError by remember { mutableStateOf<String?>(null) }
    var confirmPassError by remember { mutableStateOf<String?>(null) }


    val authState = authViewModel.authState.observeAsState()
    val context = LocalContext.current

    LaunchedEffect(authState.value) {
        when(authState.value){
            is AuthState.Authenticated -> {
                navController.navigate("homepage")
            }
            is AuthState.EmailSent -> {
                navController.navigate("login")
                Toast.makeText(context, "Verificaiton Mail Sent", Toast.LENGTH_SHORT).show()}
            is AuthState.EmailUnverfied -> Toast.makeText(context, "Mail not Varified", Toast.LENGTH_SHORT).show()
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

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(20.dp),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(text = "SignUp", style = MaterialTheme.typography.headlineSmall)
            Spacer(modifier = Modifier.height(16.dp))

            OutlinedTextField(
                value = name,
                onValueChange = {
                    name = it
                    nameError = Validator.validateName(it)
                    },
                label = { Text("Name") },
                isError = nameError != null,
                modifier = Modifier.fillMaxWidth(),
                colors = OutlinedTextFieldDefaults.colors(
                    focusedLabelColor = Color.Green,
                    unfocusedLabelColor = Color.Red
                )
            )
            nameError?.let { Text(it, color = Color.Red) }

            Spacer(modifier = Modifier.height(16.dp))
            OutlinedTextField(
                value = email,
                onValueChange = {
                    email = it
                    emailError = Validator.validateEmail(it)
                },
                label = { Text("Email") },
                isError = emailError != null,
                modifier = Modifier.fillMaxWidth(),
                colors = OutlinedTextFieldDefaults.colors(
                    focusedLabelColor = Color.Green,
                    unfocusedLabelColor = Color.Red,
                    focusedBorderColor = Color.DarkGray,
                    unfocusedBorderColor = Color.DarkGray
                )
            )
            emailError?.let { Text(it, color = Color.Red) }

            Spacer(modifier = Modifier.height(16.dp))
            OutlinedTextField(
                value = password,
                onValueChange = { password = it
                                passwordError = Validator.validatePassword(it)
                                },
                label = { Text("Password") },
                isError = passwordError != null,
                visualTransformation = if (passwordVisible) VisualTransformation.None else PasswordVisualTransformation(),
                modifier = Modifier.fillMaxWidth(),
                colors = OutlinedTextFieldDefaults.colors(
                    focusedLabelColor = Color.Green,
                    unfocusedLabelColor = Color.Red,
                    focusedBorderColor = Color.DarkGray,
                    unfocusedBorderColor = Color.DarkGray
                ),

                trailingIcon = {
                    IconButton(onClick = {passwordVisible = !passwordVisible}) {
                        Icon(
                            imageVector = if (passwordVisible) Icons.Default.Favorite else Icons.Default.FavoriteBorder,
                            contentDescription = if (passwordVisible) "Hide password" else "Show password"
                        )
                    }
                }
                //enabled = false
            )
            passwordError?.let { Text(it, color = Color.Red) }

            Spacer(modifier = Modifier.height(16.dp))
            OutlinedTextField(
                value = confirmPassword,
                onValueChange = { confirmPassword = it
                                confirmPassError = Validator.validateConfirmPassword(it, password)
                                },
                label = { Text("Confirm Password") },
                isError = confirmPassError != null,
                visualTransformation = if (passwordVisible) VisualTransformation.None else PasswordVisualTransformation(),
                modifier = Modifier.fillMaxWidth(),
                colors = OutlinedTextFieldDefaults.colors(
                    focusedLabelColor = Color.Green,
                    unfocusedLabelColor = Color.Red,
                    focusedBorderColor = Color.DarkGray,
                    unfocusedBorderColor = Color.DarkGray
                ),

                trailingIcon = {
                    IconButton(onClick = {passwordVisible = !passwordVisible}) {
                        Icon(
                            imageVector = if (passwordVisible) Icons.Default.Favorite else Icons.Default.FavoriteBorder,
                            contentDescription = if (passwordVisible) "Hide password" else "Show password"
                        )
                    }
                }
                //enabled = false
            )
            confirmPassError?.let { Text(it, color = Color.Red) }
            Spacer(modifier = Modifier.height(16.dp))
            Button(
                onClick = {
                   if(nameError == null
                       && emailError == null
                       && passwordError == null
                       && confirmPassError == null)authViewModel.signup(email, password)
                    else{
                       Toast.makeText(context, "Password Not match", Toast.LENGTH_SHORT).show()
                   }
                },
                modifier = Modifier.fillMaxWidth()
            ) {
                Text(text = "Sign Up")
            }
            Spacer(modifier = Modifier.height(16.dp))
            TextButton(
                onClick = {
                    navController.navigate("login")
                }
            ) {
                Text(text = "Back To Login")
            }
        }

    }

}