import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import com.example.traction.student.Student
import java.util.UUID

@Composable
fun AddStudent(navController: NavHostController, studentViewModel: StudentViewModel) {
    var name by remember { mutableStateOf("") }
    var address by remember { mutableStateOf("") }
    var phoneNumber by remember { mutableStateOf("") }
    var salary by remember { mutableStateOf("") }
    var selectedHour by remember { mutableStateOf("01") }
    var selectedMinute by remember { mutableStateOf("00") }
    var selectedPeriod by remember { mutableStateOf("AM") }
    val hours = (1..12).map { it.toString().padStart(2, '0') }
    val minutes = (0..59).map { it.toString().padStart(2, '0') }
    val periods = listOf("AM", "PM")
    val selectedDays = remember { mutableStateOf(mutableSetOf<String>()) }
    val daysOfWeek = listOf("Sat", "Sun", "Mon", "Tue", "Wed", "Thu", "Fri")
    var showDialog by remember { mutableStateOf(false) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.Top,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text("Enter Student Information", fontSize = 20.sp, modifier = Modifier.padding(8.dp))

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
                            name, address, phoneNumber, salary,
                            "$selectedHour:$selectedMinute $selectedPeriod",
                            selectedDays.value, studentViewModel
                        )
                        showDialog = false
                        navController.navigate("homepage")
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
    name: String,
    address: String,
    phoneNumber: String,
    salary: String,
    teachingTime: String,
    days: Set<String>,
    studentViewModel: StudentViewModel
) {
    println("Student info saved:")
    println("Name: $name, " +
            "Address: $address, " +
            "Phone: $phoneNumber, " +
            "Salary: $salary, " +
            "Teaching Time: $teachingTime, " +
            "Days: $days")
    val student = Student(
        id = UUID.randomUUID().toString(), // Generate unique ID for the student
        name = name,
        address = address,
        phoneNumber = phoneNumber,
        salary = salary,
        teachingTime = teachingTime,
        teachingDays = days.toList()
    )
    studentViewModel.addStudent(student)

}