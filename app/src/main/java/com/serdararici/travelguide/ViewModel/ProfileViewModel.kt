package com.serdararici.travelguide.ViewModel

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener
import com.serdararici.travelguide.Model.Explore
import com.serdararici.travelguide.Model.Profile
import com.serdararici.travelguide.Repository.ExploreRepository
import com.serdararici.travelguide.Repository.ProfileRepository

class ProfileViewModel : ViewModel() {
    val profileRepo = ProfileRepository()
    val exploreRepo = ExploreRepository()
    var exploreListLive = MutableLiveData<List<Explore>>()
    var userExploreListLive = MutableLiveData<List<Explore>>()
    var profileListLive = MutableLiveData<List<Profile>>()
    var exploreCountForUserLive = MutableLiveData<Long>()

    init {
        exploreListLive = exploreRepo.getExploreLive()
        userExploreListLive = exploreRepo.getUserExploreListLive()
        profileListLive = profileRepo.getProfiles()
        exploreCountForUserLive = exploreRepo.getExploreCountForUserLive()
        getProfileViewModel()
        getExploreForUserViewModel()
    }

    fun profileCreateViewModel(userName:String, userEmail:String,birthDate:String,userBio:String,userNumberOfPosts:Int,profileImgUri:String,profileCreatedDate:Long){
        profileRepo.profileCreateRepository(userName, userEmail,birthDate,userBio,userNumberOfPosts,profileImgUri,profileCreatedDate)
    }

    fun profileUpdateViewModel(profileId: String, userName:String, userEmail:String,birthDate:String,userBio:String?,userNumberOfPosts:Int,profileImgUri:String?,profileCreatedDate:Long){
        profileRepo.profileUpdateRepository(profileId, userName, userEmail,birthDate,userBio,userNumberOfPosts,profileImgUri,profileCreatedDate)
    }

    fun getProfileViewModel(){
        profileRepo.getProfileRepository()
    }

    fun deleteProfileViewModel(profileId:String){
        profileRepo.deleteProfileRepository(profileId)
    }



    fun searchViewModel(searchingWord:String){
        exploreRepo.searchRepository(searchingWord)
    }

    fun getExploreViewModel(){
        exploreRepo.getExploreRepository()
    }

    fun getExploreForUserViewModel(){
        exploreRepo.getExploreForUserRepository()
    }

    fun getExploreForUserInProfileDetailsViewModel(userEmail:String){
        exploreRepo.getExploreForUserInProfileDetailsRepository(userEmail)
    }

    fun deleteExploreViewModel(exploreId:String){
        exploreRepo.deleteExploreRepository(exploreId)
    }

    fun caculateAge(birthDate:String):Int{
        return profileRepo.calculateAge(birthDate)
    }
}