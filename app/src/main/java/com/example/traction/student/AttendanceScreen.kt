package com.example.traction.student

import StudentViewModel
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.example.traction.ui.theme.DeepTeal

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AttendanceScreen(
    studentId: String,
    studentViewModel: StudentViewModel,
    navController: NavController
) {
    val student by studentViewModel.selectedStudent.observeAsState()
    val attendanceList = student?.attendance ?: emptyList()
    val context = LocalContext.current

    LaunchedEffect(studentId) {
        studentViewModel.fetchStudentById(studentId)
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text("Attendance for ${student?.name ?: "Loading..."}",
                        color = Color.White
                    )
                },
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(Icons.Default.ArrowBack, contentDescription = "Back",
                            tint = Color.White)
                    }
                },

                colors = TopAppBarDefaults.centerAlignedTopAppBarColors(
                    containerColor = DeepTeal
                ),
            )
        },
        floatingActionButton = {
            FloatingActionButton(
                onClick = {
                    studentViewModel.markAttendance(studentId, context)
                },
                containerColor = MaterialTheme.colorScheme.primary
            ) {
                Icon(Icons.Default.Add, contentDescription = "Add Attendance")
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
                student == null -> {
                    CircularProgressIndicator()
                }
                attendanceList.isNotEmpty() -> {
                    LazyColumn {
                        items(attendanceList) { date ->
                            Text(text = "Attendance Date: $date", style = MaterialTheme.typography.bodyLarge)
                        }
                    }
                }
                else -> {
                    Text(text = "No attendance recorded yet.", style = MaterialTheme.typography.bodyLarge)
                }
            }
        }
    }
}
