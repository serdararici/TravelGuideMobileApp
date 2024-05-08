package com.serdararici.travelguide.ViewModel

import androidx.lifecycle.ViewModel
import com.google.firebase.auth.FirebaseUser
import com.serdararici.travelguide.Repository.AuthRepository

class AuthViewModel(): ViewModel() {
    val auth = AuthRepository()

    fun signUpViewModel(email:String, password: String, onComplete: (Boolean, String?) -> Unit) {
        auth.signUpRepository(email,password){
                success, message ->
            if (success) {
                onComplete(success, message)
                // Kayıt başarılı, ek işlemleri yapabilirsiniz.
                val user = auth.getCurrentUserRepository()
            }
            // User nesnesini kullanabilir veya UI'yi güncelleyebilirsiniz.
            else {
                onComplete(success, message)
            }

        }
    }

    fun signInViewModel(email:String,password: String, onComplete: (Boolean, String?) -> Unit){
        auth.signInRepository(email,password){ success, message ->
            onComplete(success, message)
            val user = auth.getCurrentUserRepository()
        }
    }

    fun signOutViewModel(onComplete: (Boolean) -> Unit) {
        auth.signOutRepository(){
            onComplete(true)
        }
    }

    fun forgetPasswordViewModel(email:String){
        auth.forgetPassword(email)
    }
    fun currentUserViewModel(): FirebaseUser?{
        return auth.getCurrentUserRepository()
    }
}