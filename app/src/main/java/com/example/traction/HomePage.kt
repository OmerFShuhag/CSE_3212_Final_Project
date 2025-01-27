import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.background
import androidx.compose.foundation.border
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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.capitalize
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.traction.AuthState
import com.example.traction.AuthViewModel
import com.example.traction.DatabaseViewModel
import com.example.traction.User
import com.example.traction.student.Student
import com.example.traction.ui.theme.DeepTeal
import com.example.traction.ui.theme.SoftTeal
import com.google.firebase.auth.FirebaseAuth
import kotlinx.coroutines.launch
import androidx.compose.material3.CardDefaults
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Brush.Companion.verticalGradient
import androidx.compose.ui.res.painterResource
import com.example.traction.R
//import androidx.compose.ui.text.style.TextForegroundStyle.Unspecified.brush
import com.example.traction.ui.theme.CoralRed
import com.example.traction.ui.theme.LightTeal
import com.example.traction.ui.theme.MutedTeal

//import java.time.LocalDate
//import java.time.format.DateTimeFormatter

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
        modifier = Modifier.systemBarsPadding(),
    ) {
        Scaffold(
            topBar = {
                TopAppBar(
                    title = {
                        Column{
                            Text("Homepage", style = MaterialTheme.typography.titleLarge,
                                color = Color.White)
                            Text(
                                "Welcome back! Manage your students here.",
                                style = MaterialTheme.typography.bodySmall,
                                color = Color.White.copy(alpha = 0.7f)
                            )
                            }
                            },
                    navigationIcon = {
                        IconButton(onClick = {
                            Scope.launch { drawerState.open() }
                        }) {
                            Icon(Icons.Default.Menu, contentDescription = "Menu")
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
                        navController.navigate("add_student")
                        },
                    containerColor = DeepTeal
                ) {
                    Icon(painter = painterResource(id = R.drawable.user),
                        contentDescription = "Add Student",
                        modifier = Modifier.size(30.dp),
                        tint = Color.Unspecified)
                }
            }
        )
        { innerpadding ->
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(innerpadding)
                    .background(brush = Brush.verticalGradient(colors = listOf(LightTeal, SoftTeal))),
                contentAlignment = Alignment.TopCenter
            ){
                Column (
                    modifier = Modifier
                        .fillMaxSize(),
                ) {
                    Spacer(modifier = Modifier.height(16.dp))

                    if (students.value.isEmpty()) {
                        Box(
                            modifier = Modifier
                                .fillMaxSize()
                                .padding(16.dp),
                            contentAlignment = Alignment.Center
                        ) {
                            Text(
                                "No students found. Click the + button to add a student.",
                                style = MaterialTheme.typography.bodyMedium,
                                color = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.7f)
                            )
                        }
                    }

                    else {
                        LazyColumn(
                            modifier = Modifier.fillMaxSize(),
                            contentPadding = PaddingValues(16.dp),
                            verticalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                            items(students.value) { student ->
                                StudentCard(student = student) {
                                    navController.navigate("student_detail/${student.id}")

                                }
                            }
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
        colors = CardDefaults.cardColors(
            containerColor = MutedTeal
        )
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
            .width(300.dp)
            .fillMaxHeight()
            .background(SoftTeal)
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
                ProfileField(label = "Name", value = user.name)
                Spacer(modifier = Modifier.height(8.dp))

                ProfileField(label = "Email", value = user.email)
                Spacer(modifier = Modifier.height(8.dp))

                ProfileField(label = "Phone", value = user.phone)
                Spacer(modifier = Modifier.height(8.dp))

                ProfileField(label = "Address", value = user.address)
                Spacer(modifier = Modifier.height(8.dp))
            } else {
                Text(text = "Loading...")
            }
            Spacer(modifier = Modifier.height(8.dp))


        }
        Button(
            onClick = { authViewModel.signout() },
            modifier = Modifier.align(Alignment.CenterHorizontally),
            colors = ButtonDefaults.buttonColors(containerColor = CoralRed)
        ) {
            Text(text = "Sign Out", color = Color.White, fontSize = 16.sp, modifier = Modifier.padding(8.dp))
        }
    }
}

@Composable
fun ProfileField(label: String, value: String) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .background(
                color = Color.White.copy(alpha = 0.7f),
                shape = MaterialTheme.shapes.medium
            )
            .border(
                width = 1.dp,
                color = Color.Black,
                shape = MaterialTheme.shapes.medium
            )
            .padding(16.dp)
    ) {
        Column {
            Text(
                text = label,
                style = MaterialTheme.typography.labelSmall,
                color = Color.Black
            )
            Spacer(modifier = Modifier.height(4.dp))
            Text(
                text = value,
                style = MaterialTheme.typography.bodyMedium,
                color = Color.Black
            )
        }
    }
}
