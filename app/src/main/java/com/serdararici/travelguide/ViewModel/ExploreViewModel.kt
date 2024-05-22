package com.serdararici.travelguide.ViewModel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.serdararici.travelguide.Model.Explore
import com.serdararici.travelguide.Model.Profile
import com.serdararici.travelguide.Repository.ExploreRepository
import com.serdararici.travelguide.Repository.ProfileRepository

class ExploreViewModel : ViewModel() {
    val exploreRepo = ExploreRepository()
    val profileRepo = ProfileRepository()
    var profileListLive = MutableLiveData<List<Profile>>()
    var exploreListLive = MutableLiveData<List<Explore>>()
    var exploreListFiveLive = MutableLiveData<List<Explore>>()
    var categoryExploreListLive = MutableLiveData<List<Explore>>()
    //var profileListFromExploreLive = MutableLiveData<List<Profile>>()

    init {
        getExploreViewModel()
        exploreListLive = exploreRepo.getExploreLive()
        exploreListFiveLive = exploreRepo.getExploreFiveLive()
        categoryExploreListLive = exploreRepo.getCategoryExploreListLive()
        profileListLive = profileRepo.getProfiles()
        //profileListFromExploreLive = profileRepo.getProfileFromExploreRepository(useremail:String)
    }

    fun searchViewModel(searchingWord:String){
        exploreRepo.searchRepository(searchingWord)
    }

    fun getExploreViewModel(){
        exploreRepo.getExploreRepository()
    }

    fun getExploreForCategoryViewModel(exploreCategory:String){
        exploreRepo.getExploreForCategoryRepository(exploreCategory)
    }

    fun getProfileFromExploreViewModel(userEmail:String){
        profileRepo.getProfileFromExploreRepository(userEmail)
    }

    fun deleteExploreViewModel(exploreId:String){
        exploreRepo.deleteExploreRepository(exploreId)
    }
}