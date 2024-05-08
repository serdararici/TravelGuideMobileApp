package com.serdararici.travelguide.Model

import com.google.firebase.database.IgnoreExtraProperties
import java.io.Serializable

@IgnoreExtraProperties
data class Explore(var exploreId:String?="",
                    var exploreTitle:String?="",
                    var exploreDetails:String?="",
                    var exploreRatingNumber:String?="",
                    var explorePlaceName:String?=""): Serializable {

}