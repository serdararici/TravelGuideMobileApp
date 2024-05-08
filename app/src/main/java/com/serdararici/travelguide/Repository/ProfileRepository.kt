package com.serdararici.travelguide.Repository

import androidx.lifecycle.MutableLiveData
import com.google.firebase.Firebase
import com.google.firebase.Timestamp
import com.google.firebase.auth.FirebaseAuth
import com.serdararici.travelguide.Model.Profile
import java.sql.Date
import java.util.Calendar

class ProfileRepository {

    /*

    //private val database = Firebase.firestore
    var profileList = ArrayList<Profile>()
    //val userEmail = FirebaseAuth.getInstance().currentUser?.email as Any
    var profileListLive: MutableLiveData<ArrayList<Profile>>

    init {
        profileListLive = MutableLiveData()
    }

    fun getProfiles() : MutableLiveData<ArrayList<Profile>> {
        return profileListLive
    }
    fun addProfile(userName:String, userEmail:String,phoneNumber:String,birthDate:String,height:Double,weight:Double){
        val profileHashMap = hashMapOf<String, Any>(
            "userEmail" to userEmail,
            "userName" to userName,
            "phoneNumber" to phoneNumber,
            "birthDate" to birthDate,
            "height" to height,
            "weight" to weight,
            "addedDate" to Timestamp.now())


        database.collection("profile").add(profileHashMap)
    }
    fun deleteProfile(profileId:String){
        database.collection("profile").document(profileId).delete()
    }
    fun updateProfile(profileId:String,userName:String, userEmail:String,phoneNumber:String,birthDate:String,height:Double,weight:Double){
        val profileHashMap = hashMapOf<String,Any>(
            "profileId" to profileId,
            "userName" to userName,
            "userEmail" to userEmail,
            "phoneNumber" to phoneNumber,
            "birthDate" to birthDate,
            "height" to height,
            "weight" to weight,
            "addedDate" to Timestamp.now())
        database.collection("profile").document(profileId).update(profileHashMap)
    }

    fun getProfile(){
        val userEmail = FirebaseAuth.getInstance().currentUser?.email as Any
        database.collection("profile").whereEqualTo("userEmail", userEmail)
            .addSnapshotListener { snapshot, exception ->
                if(exception!=null){

                }else{
                    if(snapshot!=null){
                        if(!snapshot.isEmpty){
                            val documents = snapshot.documents

                            profileList.clear()

                            for (document in documents) {
                                val userEmail = document.get("userEmail") as String
                                val userName = document.get("userName") as String
                                val phoneNumber = document.get("phoneNumber") as String
                                val birthDate = document.get("birthDate") as String
                                val height = document.getDouble("height") as Double
                                val weight = document.getDouble("weight") as Double
                                val profileId = document.id

                                val profiles = Profile(profileId,userName,userEmail,phoneNumber,birthDate,height,weight)
                                profileList.add(profiles)
                            }
                            profileListLive.postValue(profileList)
                        }
                    }
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

     */
}

