package com.serdararici.travelguide.Repository

import android.net.Uri
import android.util.Log
import androidx.lifecycle.MutableLiveData
import com.google.android.gms.auth.api.signin.internal.Storage
import com.google.firebase.Firebase
import com.google.firebase.Timestamp
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.google.firebase.storage.FirebaseStorage
import com.serdararici.travelguide.Model.Explore
import com.serdararici.travelguide.Model.Profile
import java.sql.Date
import java.util.Calendar

class ProfileRepository {
    val db = FirebaseDatabase.getInstance()
    var reference = db.getReference("profile")
    val referenceStorage = FirebaseStorage.getInstance().getReference("images")
    var profileListLive:MutableLiveData<List<Profile>>
    var profileImgUrl:MutableLiveData<String>

    val userEmail = FirebaseAuth.getInstance().currentUser?.email

    init {
        profileListLive = MutableLiveData()
        profileImgUrl = MutableLiveData()
    }

    fun getProfiles() : MutableLiveData<List<Profile>> {
        return profileListLive
    }


    fun profileCreateRepository(userName:String, userEmail:String,birthDate:String,userBio:String,userNumberOfPosts:Int,profileImgUri:String,profileCreatedDate:Long){
        val newProfile = Profile("",userName, userEmail,birthDate,userBio,userNumberOfPosts,profileImgUri,profileCreatedDate)
        reference.push().setValue(newProfile)
    }

    fun profileUpdateRepository(profileId: String, userName:String, userEmail:String,birthDate:String,userBio:String?="",userNumberOfPosts:Int,profileImgUri:String?="",profileCreatedDate:Long){
        val map = HashMap<String,Any>()
        map["userName"] = userName
        map["userEmail"] = userEmail
        map["birthDate"] = birthDate
        userBio?.let { map["userBio"] = it }
        map["userNumberOfPosts"] = userNumberOfPosts
        profileImgUri?.let { map["profileImgUri"] = it }
        map["profileCreatedDate"] = profileCreatedDate
        reference.child(profileId).updateChildren(map)
    }

    fun searchRepository(searchingWord:String){

        reference.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                val list = ArrayList<Profile>()

                for (d in snapshot.children){
                    val profile = d.getValue(Profile::class.java)
                    if (profile != null){
                        if (profile.userName!!.lowercase().contains(searchingWord.lowercase())
                            || profile.userEmail!!.lowercase().contains(searchingWord.lowercase()))
                        {
                            profile.profileId = d.key
                            list.add(profile)
                        }
                    }
                }
                profileListLive.value = list
            }

            override fun onCancelled(error: DatabaseError) {
                Log.e("FirebaseError", "Firebase error")
            }

        })
    }

    fun getProfileRepository(){

        reference.orderByChild("userEmail").equalTo(userEmail).addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                val list = ArrayList<Profile>()

                for (d in snapshot.children){
                    val profile = d.getValue(Profile::class.java)
                    if (profile != null){
                        profile.profileId = d.key
                        list.add(profile)
                    }
                }
                profileListLive.value = list
            }

            override fun onCancelled(error: DatabaseError) {
                Log.e("FirebaseError", "Firebase error")
            }

        })
    }

    fun deleteProfileRepository(profileId:String){
        reference.child(profileId).removeValue()
    }

    fun saveProfileImgRepository(uri: Uri,profileId:String){
            referenceStorage.child(profileId).putFile(uri).addOnSuccessListener {task->
                task.metadata!!.reference!!.downloadUrl.addOnSuccessListener {url->
                    val imgUrl = url.toString()
                    profileImgUrl.value = imgUrl
                }
            }
    }

    fun calculateAge(birthDate: String): Int {
        // Şuanki tarihi al
        val currentDate = Timestamp.now().toDate()

        // Doğum tarihini ayrıştır
        val birthDateParts = birthDate.split("/")
        val birthDay = birthDateParts[0].toInt()
        val birthMonth = birthDateParts[1].toInt()
        val birthYear = birthDateParts[2].toInt()

        // Doğum tarihini oluştur
        val birthDateObject = Date(birthYear - 1900, birthMonth - 1, birthDay)

        // Doğum tarihini içeren bir Calendar nesnesi oluştur
        val birthDateCalendar = Calendar.getInstance()
        birthDateCalendar.time = birthDateObject

        // Şuanki tarih ile doğum tarihi arasındaki farkı hesapla
        val ageInMillis = currentDate.time - birthDateCalendar.timeInMillis
        val ageCalendar = Calendar.getInstance()
        ageCalendar.timeInMillis = ageInMillis

        // Yıl olarak yaş değerini al
        val age = ageCalendar.get(Calendar.YEAR) - 1970

        return age
    }

}

