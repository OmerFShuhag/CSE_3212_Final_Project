package com.example.traction


//@OptIn(ExperimentalMaterial3Api::class)
//@Composable
//fun AddStudent(navController: NavController, viewModel: AddStudentViewModel = viewModel()) {
//
//    var name by remember { mutableStateOf(TextFieldValue("")) }
//    var address by remember { mutableStateOf(TextFieldValue("")) }
//    var phone by remember { mutableStateOf(TextFieldValue("")) }
//    var salary by remember { mutableStateOf(TextFieldValue("")) }
//    var teachTime by remember { mutableStateOf(TextFieldValue("")) }
//    var teachHours by remember { mutableStateOf(TextFieldValue("")) }
//    var selectedDaysInWeek by remember { mutableStateOf(0) }
//    var selectedDays by remember { mutableStateOf(mutableSetOf<String>()) }
//    var showDialog by remember { mutableStateOf(false) }
//
//    val daysOfWeek = listOf("Mon", "Tue", "Wed", "Thu", "Fri", "Sat", "Sun")
//
//    Scaffold (
//        topBar = {
//            TopAppBar(
//                title = { Text("Add Student") },
//                navigationIcon = {
//                    IconButton(onClick = {navController.popBackStack() }) {
//                       Icon(Icons.Default.Close,
//                           contentDescription = "Close")
//                    }
//                }
//            )
//        },
//        content = {padding ->
//            Column(
//                modifier = Modifier
//                    .fillMaxSize()
//                    .padding(padding)
//                    .padding(16.dp)
//            ){
//                OutlinedTextField(
//                    value = name,
//                    onValueChange = { name = it },
//                    label = { Text("Name") },
//                    modifier = Modifier.fillMaxWidth()
//                )
//
//                OutlinedTextField(
//                    value = address,
//                    onValueChange = { address = it },
//                    label = { Text("Address") },
//                    modifier = Modifier.fillMaxWidth()
//                )
//
//                OutlinedTextField(
//                    value = phone,
//                    onValueChange = { phone = it },
//                    label = { Text("Phone") },
//                    modifier = Modifier.fillMaxWidth()
//                )
//
//                Text(
//                    text = "Number of Days Teaching in a Week"
//                )
//
//            }
//
//        }
//    ){  }
//
//}