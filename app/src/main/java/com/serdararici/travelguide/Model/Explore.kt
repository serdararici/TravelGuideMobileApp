package com.serdararici.travelguide.Model

import com.google.firebase.Timestamp
import com.google.firebase.database.IgnoreExtraProperties
import java.io.Serializable

@IgnoreExtraProperties
data class Explore(var exploreId:String?="",
                   var userEmail:String?="",
                   var exploreTitle:String?="",
                   var exploreDetails:String?="",
                   var exploreRatingNumber:String?="",
                   var exploreCountry:String?="",
                   var explorePlace:String?="",
                   var exploreCategory:String?="",
                   var exploreCreatedDate:Long?=0,
                   var exploreImageUri:String?="https://firebasestorage.googleapis.com/v0/b/finalhomework-1140c.appspot.com/o/imagesExplore%2F394db0b0-d58c-413b-b634-23a0145ab81e.jpg?alt=media&token=11a9af3b-8395-4bb5-be28-1ea8d60e0184"): Serializable {

}