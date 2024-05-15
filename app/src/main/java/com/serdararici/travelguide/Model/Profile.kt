package com.serdararici.travelguide.Model

import com.google.firebase.database.IgnoreExtraProperties
import java.io.Serializable

@IgnoreExtraProperties
data class Profile(var profileId:String?="",
                   var userName: String?="",
                   var userEmail: String?="",
                   var birthDate:String?="",
                   var userBio:String?="",
                   var userNumberOfPosts:Int?=0,
                   var profileImgUri:String?="",
                   var profileCreatedDate:Long?=0) : Serializable {
}