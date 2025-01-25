package com.example.traction

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore

open class DatabaseViewModel : ViewModel(){

    private val db = FirebaseFirestore.getInstance()


    
    fun saveUser(
        user: User,
        onSuccess: () -> Unit,
        onFailure: (Exception) -> Unit
    ){
        val uid = FirebaseAuth.getInstance().currentUser?.uid
        if(uid != null){
            db.collection("user")
                .document(uid)
                .set(user)
                .addOnSuccessListener { onSuccess() }
                .addOnFailureListener { onFailure(it) }
        }
        else{
            onFailure(Exception("User Not Authenticated"))
        }

    }

    private val _userData = MutableLiveData<User?>()
    val userdata: LiveData<User?> = _userData

    fun fetchUserInfo(userId:String){
        db.collection("user").document(userId).get()
            .addOnSuccessListener { document ->
                if(document != null && document.exists()){
                    val user = document.toObject(User::class.java)
                    _userData.postValue(user)
                }
            }
            .addOnFailureListener {
                _userData.postValue(null)
            }
    }

}
