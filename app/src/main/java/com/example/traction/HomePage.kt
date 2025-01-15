import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.traction.AuthState
import com.example.traction.AuthViewModel
import com.example.traction.DatabaseViewModel
import com.example.traction.User
import com.google.firebase.auth.FirebaseAuth
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)

@Composable
fun HomePage(modifier: Modifier, navController: NavController, authViewModel: AuthViewModel,
databaseViewModel: DatabaseViewModel) {

    val authState = authViewModel.authState.observeAsState()
    val user = databaseViewModel.userdata.observeAsState()
    val drawerState = rememberDrawerState(initialValue = DrawerValue.Closed)
    val Scope = rememberCoroutineScope()

    LaunchedEffect(authState.value) {
        when (authState.value) {
            is AuthState.Unauthenticated -> navController.navigate("login")
            is AuthState.Authenticated -> {
                val userId = FirebaseAuth.getInstance().currentUser?.uid
                if (userId != null) {
                    databaseViewModel.fetchUserInfo(userId)
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
                    title = { Text("Homepage")},
                    navigationIcon = {
                        IconButton(onClick = {
                            Scope.launch { drawerState.open() }
                        }) {
                            Icon(Icons.Default.Menu,
                                contentDescription = "Menu")
                        }
                    }
                )
            }
        ) {padding ->
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(padding),
                contentAlignment = Alignment.Center
            ){
                Text(text = "Student List")
            }

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
