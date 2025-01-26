package com.example.traction.student

import StudentViewModel
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.navigation.NavController
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun StudentDetail(
    studentId: String,
    studentViewModel: StudentViewModel,
    navController: NavController
) {
    val selectedStudent by studentViewModel.selectedStudent.observeAsState(null)
    val error by studentViewModel.errorMessage.observeAsState()
    var showDeleteDialog by remember { mutableStateOf(false) }

    // Fetch student data when the page is loaded
    LaunchedEffect(studentId) {
        studentViewModel.fetchStudentById(studentId)
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text("Student Detail")
                },
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(Icons.Default.ArrowBack, contentDescription = "Back")
                    }
                },
                actions = {
                    IconButton(onClick = { showDeleteDialog = true }) {
                        Icon(Icons.Default.Delete, contentDescription = "Delete Student")
                    }
                }
            )
        },
        floatingActionButton = {
            FloatingActionButton(
                onClick = {
                    navController.navigate("edit_student/${studentId}") // Navigate to EditStudentPage
                },
                containerColor = MaterialTheme.colorScheme.primary
            ) {
                Icon(Icons.Default.Edit, contentDescription = "Edit Student")
            }
        }
    ) { padding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding),
            contentAlignment = Alignment.Center
        ) {
            when {
                // Error condition
                error != null -> {
                    Text(text = "Error: ${error}", color = MaterialTheme.colorScheme.error)
                }
                // Loading condition
                selectedStudent == null -> {
                    CircularProgressIndicator()
                }
                // Data loaded successfully
                else -> {
                    val student = selectedStudent
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(16.dp),
                        verticalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        Text(text = "Name: ${student?.name}", style = MaterialTheme.typography.titleLarge)
                        Text(text = "Teaching Days: ${student?.teachingDays}", style = MaterialTheme.typography.bodyLarge)
                        Text(text = "Teaching Time: ${student?.teachingTime}", style = MaterialTheme.typography.bodyLarge)
                        Text(text = "Monthly Fee: ${student?.salary}", style = MaterialTheme.typography.bodyLarge)
                        Text(text = "Phone: ${student?.phoneNumber}", style = MaterialTheme.typography.bodyLarge)

                        // Button to navigate to Attendance Screen
                        TextButton(onClick = {
                            navController.navigate("attendance/${student?.id}")
                        }) {
                            Text("Manage Attendance")
                        }
                    }
                }
            }
        }
    }

    if (showDeleteDialog) {
        AlertDialog(
            onDismissRequest = { showDeleteDialog = false },
            title = { Text("Delete Student") },
            text = { Text("Are you sure you want to delete this student? This action cannot be undone.") },
            confirmButton = {
                TextButton(
                    onClick = {
                        studentViewModel.deleteStudent(studentId) // Call the delete function
                        showDeleteDialog = false
                        navController.popBackStack() // Navigate back after deletion
                    }
                ) {
                    Text("OK")
                }
            },
            dismissButton = {
                TextButton(onClick = { showDeleteDialog = false }) {
                    Text("Cancel")
                }
            }
        )
    }
}
