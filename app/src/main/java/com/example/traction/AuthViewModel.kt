package com.example.traction

import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.navigation.NavController
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore

class AuthViewModel : ViewModel(){
    private val auth : FirebaseAuth = FirebaseAuth.getInstance()
    private val db = FirebaseFirestore.getInstance()

    private val _authState = MutableLiveData<AuthState>()
    val authState: LiveData<AuthState> = _authState

    init {
        checkAuthStatus()
        auth.addAuthStateListener { firebaseAuth ->
            val user = firebaseAuth.currentUser
            when {
                user == null -> _authState.value = AuthState.Unauthenticated
                user.isEmailVerified -> _authState.value = AuthState.Authenticated
                else -> _authState.value = AuthState.EmailUnverified
            }
        }


    }


    fun checkAuthStatus(){
        val currentUser = auth.currentUser

        if(currentUser == null){
            _authState.value = AuthState.Unauthenticated
        }
        else{
            if(currentUser.isEmailVerified)_authState.value = AuthState.Authenticated
            _authState.value = AuthState.EmailUnverified
        }
    }

    fun login(email: String, password: String, navController: NavController){
        if(email.isEmpty() || password.isEmpty()){
            _authState.value = AuthState.Error("Email or Password can not be Empty")
            return
        }

        _authState.value = AuthState.Loading
        auth.signInWithEmailAndPassword(email, password)
            .addOnCompleteListener{task->
                if (task.isSuccessful){
                    val user = auth.currentUser

                    if(user?.isEmailVerified == true){
                        _authState.value = AuthState.Authenticated
                        val userId = FirebaseAuth.getInstance().currentUser
                        val db = FirebaseFirestore.getInstance()

                        userId?.let {
                            db.collection("user").document(userId.uid).get()
                                .addOnSuccessListener { document ->
                                    if (document.exists()) {
                                        navController.navigate("homepage")
                                    } else {
                                        navController.navigate("profile_setup")
                                    }
                                }
                        }
                    }
                    else{
                        _authState.value = AuthState.EmailUnverified

                    }
                }
                else{
                    _authState.value = AuthState.Error(task.exception?.message?:"Something went wrong")
                }
            }
    }

    fun signup(email: String, password: String){
        if(email.isEmpty() || password.isEmpty()){
            _authState.value = AuthState.Error("Email or Password can not be Empty")
            return
        }

        _authState.value = AuthState.Loading

        auth.createUserWithEmailAndPassword(email, password)
            .addOnCompleteListener{task->
                if (task.isSuccessful){
                    val user = auth.currentUser
                    user?.sendEmailVerification()
                        ?.addOnCompleteListener{emailTask ->
                            if (emailTask.isSuccessful)_authState.value = AuthState.EmailSent
                            else _authState.value = AuthState.Error("Failed to Sent Email")
                        }
                }
                else{
                    _authState.value = AuthState.Error(task.exception?.message?:"Something went wrong")
                }
            }
    }

    fun resendEmail(){
        val user = auth.currentUser
        if(user != null && !user.isEmailVerified){
            user.sendEmailVerification()
                .addOnCompleteListener{task ->
                    if(task.isSuccessful){
                        _authState.value = AuthState.EmailSent
                    }else{
                        _authState.value = AuthState.Error("Failed TO Send Email")
                    }
                }
        }
    }

    fun sendPassResetEmail(email: String){
        if(email.isEmpty()){
            _authState.value = AuthState.Error("Need An Email To Sent the Link Mate")
            return
        }

        auth.sendPasswordResetEmail(email)
            .addOnCompleteListener{task ->
                if (task.isSuccessful){
                    _authState.value = AuthState.EmailSent
                }else{
                    _authState.value = AuthState.Error(task.exception?.message?:"Something went wrong")
                }
            }
    }

    fun signout(){
        auth.signOut()
        FirebaseAuth.getInstance().signOut()
        _authState.value = AuthState.Unauthenticated
    }
}

sealed class AuthState{
    object Authenticated : AuthState()
    object Unauthenticated : AuthState()
    object Loading : AuthState()
    object EmailUnverified : AuthState()
    object EmailSent : AuthState()
    data class Error(val message : String) : AuthState()
}

@Composable
fun showAlert(
    title: String,
    content: String,
    onDismiss:() -> Unit
){
    AlertDialog(
        onDismissRequest = {},
        title = { Text(text = title) },
        text = { Text(text = content) },
        confirmButton = {
            TextButton(onClick = { onDismiss() }) {
                Text(text = "OK")
            }
        }
    )
}

