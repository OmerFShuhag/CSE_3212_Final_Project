package com.example.traction.student

import StudentViewModel
import TimeDropdownMenu
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
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
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import java.util.UUID

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun editStudent(
    studentId : String,
    studentViewModel: StudentViewModel,
    navController: NavController
){
    val selectedStudent = studentViewModel.selectedStudent.observeAsState()
    val context = LocalContext.current

    LaunchedEffect(studentId) {
        studentViewModel.fetchStudentById(studentId)
    }

    val student = selectedStudent.value

    if(student != null){
        var name by remember { mutableStateOf(student.name) }
        var address by remember { mutableStateOf(student.address) }
        var phoneNumber by remember { mutableStateOf(student.phoneNumber) }
        var salary by remember { mutableStateOf(student.salary) }
        var selectedHour by remember { mutableStateOf(student.teachingTime.substringBefore(":")) }
        var selectedMinute by remember { mutableStateOf(student.teachingTime.substringAfter(":").substringBefore(" ")) }
        var selectedPeriod by remember { mutableStateOf(student.teachingTime.substringAfter(" ")) }
        val hours = (1..12).map { it.toString().padStart(2, '0') }
        val minutes = (0..59).map { it.toString().padStart(2, '0') }
        val periods = listOf("AM", "PM")
        val selectedDays = remember { mutableStateOf(student.teachingDays.toMutableSet()) }
        val daysOfWeek = listOf("Sat", "Sun", "Mon", "Tue", "Wed", "Thu", "Fri")
        var showDialog by remember { mutableStateOf(false) }

        Scaffold(
            topBar = {
                TopAppBar(
                    title = { Text("Edit Student") },
                    navigationIcon = {
                        IconButton(onClick = { navController.popBackStack() }) {
                            Icon(Icons.Default.ArrowBack, contentDescription = "Back")
                        }
                    }
                )
            }
        ){padding ->
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(padding)
                    .padding(16.dp),
                verticalArrangement = Arrangement.Top,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                OutlinedTextField(
                    value = name,
                    onValueChange = { name = it },
                    label = { Text("Name") },
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(8.dp)
                )
                OutlinedTextField(
                    value = address,
                    onValueChange = { address = it },
                    label = { Text("Address") },
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(8.dp)
                )
                OutlinedTextField(
                    value = phoneNumber,
                    onValueChange = { phoneNumber = it },
                    label = { Text("Phone Number") },
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(8.dp)
                )
                OutlinedTextField(
                    value = salary,
                    onValueChange = { salary = it },
                    label = { Text("Salary") },
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(8.dp)
                )
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 8.dp),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    TimeDropdownMenu(
                        label = "Hour",
                        options = hours,
                        selectedOption = selectedHour,
                        onOptionSelected = { selectedHour = it }
                    )
                    TimeDropdownMenu(
                        label = "Minute",
                        options = minutes,
                        selectedOption = selectedMinute,
                        onOptionSelected = { selectedMinute = it }
                    )
                    TimeDropdownMenu(
                        label = "AM/PM",
                        options = periods,
                        selectedOption = selectedPeriod,
                        onOptionSelected = { selectedPeriod = it }
                    )
                }

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceEvenly
                ) {
                    daysOfWeek.forEach { day ->
                        Box(
                            modifier = Modifier
                                .size(40.dp)
                                .background(
                                    if (selectedDays.value.contains(day)) Color.Cyan else Color.Gray,
                                    shape = RoundedCornerShape(4.dp)
                                )
                                .clickable {
                                    selectedDays.value = selectedDays.value.toMutableSet().apply {
                                        if (contains(day)) remove(day) else add(day)
                                    }
                                },
                            contentAlignment = Alignment.Center
                        ) {
                            Text(day, color = Color.White, fontSize = 12.sp)
                        }
                    }
                }

                Spacer(modifier = Modifier.height(16.dp))

                Button(onClick = { showDialog = true }) {
                    Text("Submit")
                }

                if (showDialog) {
                    AlertDialog(
                        onDismissRequest = {},
                        title = { Text("Confirm Student Information") },
                        text = {
                            Text(
                                """
                        Name: $name
                        Address: $address
                        Phone: $phoneNumber
                        Salary: $salary
                        Teaching Time: $selectedHour:$selectedMinute $selectedPeriod
                        Days: ${selectedDays.value.joinToString(", ")}
                        """.trimIndent()
                            )
                        },
                        confirmButton = {
                            TextButton(onClick = {
                                saveStudentInfo(
                                    student.id,
                                    name, address, phoneNumber, salary,
                                    "$selectedHour:$selectedMinute $selectedPeriod",
                                    selectedDays.value,
                                    studentViewModel
                                )
                                showDialog = false
                                navController.popBackStack()
                            }) {
                                Text("OK")
                            }
                        },
                        dismissButton = {
                            TextButton(onClick = { showDialog = false }) {
                                Text("Cancel")
                            }
                        }
                    )
                }
            }

        }

    }
    else{
        Box(
            modifier = Modifier
                .fillMaxSize(),
            contentAlignment = Alignment.Center
        ) {
            CircularProgressIndicator()
        }
    }
}


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TimeDropdownMenu(
    label: String,
    options: List<String>,
    selectedOption: String,
    onOptionSelected: (String) -> Unit
) {
    var expanded by remember { mutableStateOf(false) }

    ExposedDropdownMenuBox(
        expanded = expanded,
        onExpandedChange = { expanded = it }
    ) {
        OutlinedTextField(
            value = selectedOption,
            onValueChange = {},
            readOnly = true,
            label = { Text(label) },
            trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = expanded) },
            modifier = Modifier
                .menuAnchor()
                .width(100.dp)
        )

        ExposedDropdownMenu(
            expanded = expanded,
            onDismissRequest = { expanded = false }
        ) {
            options.forEach { option ->
                DropdownMenuItem(
                    text = { Text(option) },
                    onClick = {
                        onOptionSelected(option)
                        expanded = false
                    }
                )
            }
        }
    }
}

fun saveStudentInfo(
    id: String,
    name: String,
    address: String,
    phoneNumber: String,
    salary: String,
    teachingTime: String,
    days: Set<String>,
    studentViewModel: StudentViewModel
) {
    val updatedstudent = Student(
        name = name,
        address = address,
        phoneNumber = phoneNumber,
        salary = salary,
        teachingTime = teachingTime,
        teachingDays = days.toList()
    )
    studentViewModel.updateStudent(updatedstudent, id)

}
