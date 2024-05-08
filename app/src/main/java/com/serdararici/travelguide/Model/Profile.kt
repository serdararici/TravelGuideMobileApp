package com.serdararici.travelguide.Model

import com.google.firebase.database.IgnoreExtraProperties
import java.io.Serializable

@IgnoreExtraProperties
data class Profile(var profileId:String?="",
                   var userName: String?="",
                   var userEmail: String?="",
                   var phoneNumber:String?="",
                   var birthDate:String?="", ) : Serializable {
}