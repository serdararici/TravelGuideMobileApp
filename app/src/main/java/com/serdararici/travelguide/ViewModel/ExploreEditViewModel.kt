package com.serdararici.travelguide.ViewModel

import android.net.Uri
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.serdararici.travelguide.Model.Explore
import com.serdararici.travelguide.Repository.ExploreRepository

class ExploreEditViewModel : ViewModel() {
    val exploreRepo = ExploreRepository()
    var exploreListLive = MutableLiveData<List<Explore>>()
    var exploreImgUrl = MutableLiveData<String>()

    init {
        exploreListLive = exploreRepo.getExploreLive()
        exploreImgUrl = exploreRepo.exploreImgUrl
    }

    fun updateExploreViewModel(exploreId: String, exploreTitle:String, exploreDetails:String, exploreRatingNumber:String, exploreCountry:String,explorePlace:String,exploreCategory:String,exploreCreatedDate:Long, exploreImageUri:String){
        exploreRepo.exploreUpdateRepository(exploreId, exploreTitle, exploreDetails, exploreRatingNumber, exploreCountry,explorePlace,exploreCategory,exploreCreatedDate, exploreImageUri)
    }

    fun saveExploreImgViewModel(uri: Uri, imageId:String){
        exploreRepo.saveExploreImgRepository(uri,imageId)
    }
}