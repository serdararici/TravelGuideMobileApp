package com.serdararici.travelguide.Repository

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser

class AuthRepository {

    private val auth: FirebaseAuth = FirebaseAuth.getInstance()

    fun signUpRepository(email: String, password: String, onComplete: (Boolean, String?) -> Unit) {
        auth.createUserWithEmailAndPassword(email,password)
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    val user = task.result?.user
                    onComplete(true, user?.uid)
                } else {
                    onComplete(false, task.exception?.message)
                }
            }
    }

    fun signInRepository(email: String, password: String, onComplete: (Boolean, String?) -> Unit) {
        auth.signInWithEmailAndPassword(email, password)
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    onComplete(true, null)

                } else {
                    onComplete(false, task.exception?.message)
                }
            }
    }

    fun signOutRepository(onComplete: (Boolean) -> Unit) {
        auth.signOut()
        onComplete(true)
    }

    fun forgetPassword(email:String){
        auth.sendPasswordResetEmail(email)
    }

    fun getCurrentUserRepository(): FirebaseUser? {
        return auth.currentUser
    }

    fun isUserLoggedInRepository(): Boolean {
        return auth.currentUser != null
    }
}