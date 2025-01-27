package com.example.traction

import android.widget.Toast
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.navigation.NavController
import com.example.traction.ui.theme.DeepTeal
import com.example.traction.ui.theme.LightTeal
import com.example.traction.ui.theme.SoftTeal
import com.google.firebase.auth.FirebaseAuth

//@Preview(showBackground = true)
@Composable
fun ProfileSetup(
    databseViewModel: DatabaseViewModel,
    navController: NavController
){
    var name by remember { mutableStateOf("") }
    var nameError by remember { mutableStateOf<String?>(null) }

    var phone by remember { mutableStateOf("") }
    var phoneError by remember { mutableStateOf<String?>(null) }

    var address by remember { mutableStateOf("") }
    var addressError by remember { mutableStateOf<String?>(null) }

    val context = LocalContext.current

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(brush = Brush.verticalGradient(colors = listOf(LightTeal, SoftTeal)))
    ) {


        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        )
        {
//            Image(
//                painter = painterResource(id = R.drawable.personal_info_yzls),
//                contentDescription = "profile setup",
//                modifier = Modifier
//                    .padding(bottom = 2.dp),
//                alignment = Alignment.Center
//            )
            Text(
                text = "Lets Set Your Profile Information",
                style = MaterialTheme.typography.headlineSmall.copy(color = Color.Black), // White text
                modifier = Modifier.padding(top = 4.dp)
            )
            OutlinedTextField(
                value = name,
                onValueChange = {
                    name = it
                    nameError = Validator.validateName(it)
                },
                label = { Text("Name") },
                isError = nameError != null,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(8.dp),
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
            nameError?.let {
                Text(
                    it,
                    color = Color.Red,
                    style = MaterialTheme.typography.bodySmall)
            }
            OutlinedTextField(
                value = phone,
                onValueChange = { phone = it
                    phoneError = Validator.validatePhone(it)},
                label = { Text("Phone Number") },
                isError = phoneError != null,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(8.dp),
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
            phoneError?.let {
                Text(
                    it,
                    color = Color.Red,
                    style = MaterialTheme.typography.bodySmall)
            }
            OutlinedTextField(
                value = address,
                onValueChange = {
                    address = it
                    addressError = Validator.validateAddress(it)},
                isError = addressError != null,
                label = { Text("Address") },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(8.dp),
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
            addressError?.let {
                Text(
                    it,
                    color = Color.Red,
                    style = MaterialTheme.typography.bodySmall)
            }
            Spacer(modifier = Modifier.height(24.dp))
            Button(onClick = {
            if (name.isNotEmpty() && phone.isNotEmpty() && address.isNotEmpty()) {
                val user = User(
                    name = name,
                    phone = phone,
                    email = FirebaseAuth.getInstance().currentUser?.email ?: "",
                    address = address
                )
                databseViewModel.saveUser(
                    user = user,
                    onSuccess = {
                        Toast.makeText(context, "Profile saved successfully!", Toast.LENGTH_SHORT).show()
                        navController.navigate("homepage")
                    },
                    onFailure = {
                        Toast.makeText(context, "Failed to save profile: ${it.message}", Toast.LENGTH_SHORT).show()
                    }
                )
            } else {
                Toast.makeText(context, "All fields are required", Toast.LENGTH_SHORT).show()
            }
            },
                modifier = Modifier
                    .weight(9f, fill = false)
                    .height(50.dp),
                colors = ButtonDefaults.buttonColors(containerColor = DeepTeal)) {
                Text("Save Profile")
            }
        }
    }
}

