package com.serdararici.travelguide.ViewModel

import androidx.lifecycle.ViewModel
import com.serdararici.travelguide.Repository.ExploreRepository

class ExploreDetailsViewModel : ViewModel() {
    val exploreRepo = ExploreRepository()
}