import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.capitalize
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.traction.AuthState
import com.example.traction.AuthViewModel
import com.example.traction.DatabaseViewModel
import com.example.traction.User
import com.example.traction.student.Student
import com.google.firebase.auth.FirebaseAuth
import kotlinx.coroutines.launch
import java.time.LocalDate
import java.time.format.DateTimeFormatter

@RequiresApi(Build.VERSION_CODES.O)
@OptIn(ExperimentalMaterial3Api::class)

@Composable
fun HomePage(modifier: Modifier,
             navController: NavController,
             authViewModel: AuthViewModel,
             databaseViewModel: DatabaseViewModel,
             studentViewModel: StudentViewModel) {

    val authState = authViewModel.authState.observeAsState()
    val user = databaseViewModel.userdata.observeAsState()
    val students = studentViewModel.students.observeAsState(emptyList())
    val drawerState = rememberDrawerState(initialValue = DrawerValue.Closed)
    val Scope = rememberCoroutineScope()

    val currentDate = LocalDate.now()
    val formattedDate = currentDate.format(DateTimeFormatter.ofPattern("MMMM dd, yyyy"))
    val currentDay = currentDate.dayOfWeek.toString().capitalize()

    LaunchedEffect(authState.value) {
        when (authState.value) {
            is AuthState.Unauthenticated -> navController.navigate("login")
            is AuthState.Authenticated -> {
                val userId = FirebaseAuth.getInstance().currentUser?.uid
                if (userId != null) {
                    databaseViewModel.fetchUserInfo(userId)
                    studentViewModel.fetchStudents()
                }
                else{
                    navController.navigate("login")
                }
            }
            else -> Unit
        }
    }

    ModalNavigationDrawer(
        drawerState = drawerState,
        drawerContent = {
            DrawerContent(user = user.value, authViewModel = authViewModel)
        },
        modifier = Modifier.systemBarsPadding()
    ) {
        Scaffold(
            topBar = {
                TopAppBar(
                    title = { Text("Homepage", style = MaterialTheme.typography.titleLarge) },
                    navigationIcon = {
                        IconButton(onClick = {
                            Scope.launch { drawerState.open() }
                        }) {
                            Icon(Icons.Default.Menu, contentDescription = "Menu")
                        }
                    },
                    colors = TopAppBarDefaults.centerAlignedTopAppBarColors(
                        containerColor = MaterialTheme.colorScheme.primaryContainer,
                        titleContentColor = MaterialTheme.colorScheme.onPrimaryContainer
                    ),

                )
            },
            floatingActionButton = {
                FloatingActionButton(
                    onClick = {
                        //onAddStudentClick()
                        navController.navigate("add_student")
                        },
                    containerColor = MaterialTheme.colorScheme.primary
                ) {
                    Icon(Icons.Default.Add, contentDescription = "Add Student")
                }
            }
        ) { padding ->
            Column (
                modifier = Modifier
                    .fillMaxSize()
                    .padding(padding),
            ) {
                Text(
                    text = "$formattedDate($currentDate)",
                    style = MaterialTheme.typography.bodyMedium,
                    modifier = Modifier
                        .padding(16.dp)
                        .align(Alignment.CenterHorizontally)
                )
                Spacer(modifier = Modifier.height(16.dp))

                LazyColumn (
                    modifier = Modifier.fillMaxSize(),
                    contentPadding = PaddingValues(16.dp),
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ){
                    items(students.value){ student ->
                        StudentCard(student = student){

                        }
                    }
                }

            }
        }

        }



}
@Composable
fun StudentCard(student: Student, onClick: () -> Unit){
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(onClick = onClick)
            .padding(8.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface)
    ) {
        Column(
            modifier = Modifier.padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            Text(text = "Name: ${student.name}", style = MaterialTheme.typography.titleMedium)
            Text(text = "Teaching Time: ${student.teachingTime}", style = MaterialTheme.typography.bodyMedium)
            Text(text = "Teaching Days: ${student.teachingDays}", style = MaterialTheme.typography.bodyMedium)
        }
    }
}


@Composable
fun DrawerContent(user: User?, authViewModel: AuthViewModel){
    Column(
        modifier = Modifier
            .width(300.dp) // Set a fixed width for the drawer
            .fillMaxHeight() // Allow it to take the full height
            .background(MaterialTheme.colorScheme.surface) // Add a background color
            .padding(16.dp)
            .systemBarsPadding(),
        verticalArrangement = Arrangement.SpaceBetween
    ) {
        Column{
            Text(
                text = "Profile",
                style = MaterialTheme.typography.titleLarge,
                modifier = Modifier.padding(16.dp)
            )
            Divider()
            if (user != null) {
                Text(text = "Name: ${user.name}")
                Text(text = "Email: ${user.email}")
                Text(text = "Phone: ${user.phone}")
                Text(text = "Address: ${user.address}")
            } else {
                Text(text = "Loading...")
            }
            Spacer(modifier = Modifier.height(8.dp))


        }
        Button(
            onClick = { authViewModel.signout() },
            modifier = Modifier.align(Alignment.CenterHorizontally)
        ) {
            Text(text = "Sign Out")
        }
    }
}
