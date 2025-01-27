package com.example.traction.student

import StudentViewModel
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Divider
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.contentColorFor
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
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.example.traction.R
import com.example.traction.ui.theme.Coral
import com.example.traction.ui.theme.CoralRed
import com.example.traction.ui.theme.DeepTeal
import com.example.traction.ui.theme.LightTeal
import com.example.traction.ui.theme.MutedTeal
import com.example.traction.ui.theme.SoftTeal

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
                    Text("Student Detail", color = Color.White)
                },
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(Icons.Default.ArrowBack, contentDescription = "Back", tint = Color.White)
                    }
                },
                colors = TopAppBarDefaults.centerAlignedTopAppBarColors(
                    containerColor = DeepTeal
                ),
                actions = {
                    IconButton(onClick = { showDeleteDialog = true }) {
                        Icon(
                            painter = painterResource(R.drawable.bin),
                            contentDescription = "Delete Student",
                            tint = Color.Unspecified,
                            modifier = Modifier.size(30.dp)
                        )
                    }
                }
            )
        },
        floatingActionButton = {
            FloatingActionButton(
                onClick = {
                    navController.navigate("edit_student/${studentId}")
                },
                containerColor = DeepTeal
            ) {
                Icon(
                    painter = painterResource(R.drawable.pen),
                    contentDescription = "Edit Student",
                    tint = Color.Unspecified,
                    modifier = Modifier.size(30.dp))
            }
        }
    ) { padding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .background(brush = Brush.verticalGradient(colors = listOf(LightTeal, SoftTeal))),
            contentAlignment = Alignment.Center
        ) {
            when {
                error != null -> {
                    Text(text = "Error: ${error}", color = MaterialTheme.colorScheme.error)
                }

                selectedStudent == null -> {
                    CircularProgressIndicator()
                }

                else -> {
                    val student = selectedStudent
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(16.dp),
                        verticalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        Divider(modifier = Modifier.padding(bottom = 16.dp))
                        DetailCard(
                            label = "Name",
                            value = student?.name ?: "N/A"
                        )
                        DetailCard(
                            label = "Address",
                            value = student?.address ?: "N/A"
                        )
                        DetailCard(
                            label = "Teaching Days",
                            value = student?.teachingDays.toString() ?: "N/A"
                        )
                        DetailCard(
                            label = "Teaching Time",
                            value = student?.teachingTime ?: "N/A"
                        )
                        DetailCard(
                            label = "Monthly Fee",
                            value = student?.salary ?: "N/A"
                        )
                        DetailCard(
                            label = "Phone",
                            value = student?.phoneNumber ?: "N/A"
                        )

                        TextButton(onClick = {
                            navController.navigate("attendance/${student?.id}")
                        }) {
                            Text("Manage Attendance", color = Color.Black)
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
                        studentViewModel.deleteStudent(studentId)
                        showDeleteDialog = false
                        navController.popBackStack()
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

@Composable
fun DetailCard(label: String, value: String) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        elevation = CardDefaults.cardElevation(4.dp),
        colors = CardDefaults.cardColors(
            containerColor = MutedTeal,
        )
    ) {
        Column(
            modifier = Modifier.padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(4.dp)
        ) {
            Text(
                text = label,
                style = MaterialTheme.typography.bodySmall,
                color = DeepTeal
            )
            Text(
                text = value,
                style = MaterialTheme.typography.bodyLarge,
                color = DeepTeal
            )
        }
    }
}
