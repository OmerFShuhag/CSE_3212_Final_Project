package com.example.traction

import android.widget.Toast
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
import androidx.compose.material3.*
import androidx.compose.ui.Modifier
import androidx.navigation.NavController
import com.google.firebase.auth.FirebaseAuth


@Composable
fun ProfileSetup(
    databseViewModel: DatabaseViewModel,
    navController: NavController
){
    var name by remember { mutableStateOf("") }
    var phone by remember { mutableStateOf("") }
    var address by remember { mutableStateOf("") }
    val context = LocalContext.current

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    )
    {
        TextField(
            value = name,
            onValueChange = { name = it },
            label = { Text("Name") },
            modifier = Modifier.fillMaxWidth()
        )
        TextField(
            value = phone,
            onValueChange = { phone = it },
            label = { Text("Phone") },
            modifier = Modifier.fillMaxWidth()
        )
        TextField(
            value = address,
            onValueChange = { address = it },
            label = { Text("Address") },
            modifier = Modifier.fillMaxWidth()
        )
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
        }) {
            Text("Save Profile")
        }
    }
}