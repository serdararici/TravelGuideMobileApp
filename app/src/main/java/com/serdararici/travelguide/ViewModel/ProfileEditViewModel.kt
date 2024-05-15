package com.serdararici.travelguide.ViewModel

import android.net.Uri
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.serdararici.travelguide.Repository.ProfileRepository

class ProfileEditViewModel : ViewModel() {
    val profileRepo = ProfileRepository()
    var profileImgUrl = MutableLiveData<String>()

    init {
        // Repository'deki LiveData'yı ViewModel'deki LiveData'ya bağla
        profileImgUrl = profileRepo.profileImgUrl
    }

    fun updateProfileViewModel(profileId: String, userName:String, userEmail:String,birthDate:String,userBio:String,userNumberOfPosts:Int,profileImgUri:String,profileCreatedDate:Long){
        profileRepo.profileUpdateRepository(profileId, userName, userEmail,birthDate,userBio,userNumberOfPosts,profileImgUri,profileCreatedDate)
    }

    fun saveProfileImgViewModel(uri: Uri, profileId:String){
        profileRepo.saveProfileImgRepository(uri,profileId)
    }
}