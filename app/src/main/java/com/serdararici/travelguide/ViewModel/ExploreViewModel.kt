package com.serdararici.travelguide.ViewModel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.serdararici.travelguide.Model.Explore
import com.serdararici.travelguide.Repository.ExploreRepository

class ExploreViewModel : ViewModel() {
    val exploreRepo = ExploreRepository()
    var exploreListLive = MutableLiveData<List<Explore>>()
    var categoryExploreListLive = MutableLiveData<List<Explore>>()

    init {
        getExploreViewModel()
        exploreListLive = exploreRepo.getExploreLive()
        categoryExploreListLive = exploreRepo.getCategoryExploreListLive()
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

    fun deleteExploreViewModel(exploreId:String){
        exploreRepo.deleteExploreRepository(exploreId)
    }
}