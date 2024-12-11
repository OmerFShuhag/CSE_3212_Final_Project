package com.example.traction

import android.widget.VideoView
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.google.firebase.auth.FirebaseAuth

class AuthViewModel : ViewModel(){
    private val auth : FirebaseAuth = FirebaseAuth.getInstance()

    private val _authState = MutableLiveData<AuthState>()
    val authState: LiveData<AuthState> = _authState

    init {
        checkAuthStatus()
    }


    fun checkAuthStatus(){
        val currentUser = auth.currentUser

        if(currentUser == null){
            _authState.value = AuthState.Unauthenticated
        }
        else{
            if(currentUser.isEmailVerified)_authState.value = AuthState.Authenticated
            _authState.value = AuthState.EmailUnverfied
        }
    }

    fun login(email: String, password: String){

        if(email.isEmpty() || password.isEmpty()){
            _authState.value = AuthState.Error("Email or Password can not be Empty")
        }

        _authState.value = AuthState.Loading
        auth.signInWithEmailAndPassword(email, password)
            .addOnCompleteListener{task->
                if (task.isSuccessful){
                    val user = auth.currentUser

                    if(user?.isEmailVerified == true){
                        _authState.value = AuthState.Authenticated
                    }
                    else{
                        _authState.value = AuthState.EmailUnverfied
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

    fun signout(){
        auth.signOut()
        _authState.value = AuthState.Unauthenticated
    }
}

sealed class AuthState{
    object Authenticated : AuthState()
    object Unauthenticated : AuthState()
    object Loading : AuthState()
    object EmailUnverfied : AuthState()
    object EmailSent : AuthState()
    data class Error(val message : String) : AuthState()
}