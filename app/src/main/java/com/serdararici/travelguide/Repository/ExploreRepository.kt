package com.serdararici.travelguide.Repository

import android.net.Uri
import android.util.Log
import androidx.lifecycle.MutableLiveData
import com.google.firebase.Timestamp
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.google.firebase.storage.FirebaseStorage
import com.serdararici.travelguide.Model.Explore

class ExploreRepository () {
    val db = FirebaseDatabase.getInstance()
    var reference = db.getReference("explore")
    val referenceStorage = FirebaseStorage.getInstance().getReference("imagesExplore")
    var exploreListLive:MutableLiveData<List<Explore>>
    var exploreListFiveLive:MutableLiveData<List<Explore>>
    var userExploreLiveData:MutableLiveData<List<Explore>>
    var categoryExploreLiveData:MutableLiveData<List<Explore>>
    var exploreCountForUserLiveData:MutableLiveData<Long>
    var exploreImgUrl:MutableLiveData<String>

    val userEmail = FirebaseAuth.getInstance().currentUser?.email as String?

    init {
        exploreListLive = MutableLiveData()
        exploreListFiveLive = MutableLiveData()
        userExploreLiveData = MutableLiveData()
        categoryExploreLiveData = MutableLiveData()
        exploreCountForUserLiveData = MutableLiveData()
        exploreImgUrl = MutableLiveData()

    }

    fun getExploreLive() : MutableLiveData<List<Explore>> {
        return exploreListLive
    }
    fun getExploreFiveLive() : MutableLiveData<List<Explore>> {
        return exploreListFiveLive
    }
    fun getUserExploreListLive() : MutableLiveData<List<Explore>> {
        return userExploreLiveData
    }
    fun getCategoryExploreListLive() : MutableLiveData<List<Explore>> {
        return categoryExploreLiveData
    }
    fun getExploreCountForUserLive() : MutableLiveData<Long>{
        return exploreCountForUserLiveData
    }

    fun exploreCreateRepository(userEmail:String,exploreTitle:String, exploreDetails:String, exploreRatingNumber:String, exploreCountry:String,explorePlace:String,exploreCategory:String,exploreCreatedDAte:Long, exploreImageUri:String){
        val newExplore = Explore("",userEmail,exploreTitle,exploreDetails,exploreRatingNumber,exploreCountry,explorePlace,exploreCategory,exploreCreatedDAte,exploreImageUri)
        reference.push().setValue(newExplore)
    }

    fun exploreUpdateRepository(exploreId: String, exploreTitle:String, exploreDetails:String, exploreRatingNumber:String, exploreCountry:String,explorePlace:String,exploreCategory:String,exploreCreatedDate:Long, exploreImageUri:String){
        val map = HashMap<String,Any>()
        map["exploreTitle"] = exploreTitle
        map["exploreDetails"] = exploreDetails
        map["exploreRatingNumber"] = exploreRatingNumber
        map["exploreCountry"] = exploreCountry
        map["explorePlace"] = explorePlace
        map["exploreCategory"] = exploreCategory
        map["exploreCreatedDate"] = exploreCreatedDate
        map["exploreImageUri"] = exploreImageUri
        reference.child(exploreId).updateChildren(map)
    }

    fun searchRepository(searchingWord:String){

        reference.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                val list = ArrayList<Explore>()

                for (d in snapshot.children){
                    val explore = d.getValue(Explore::class.java)
                    if (explore != null){
                        if (explore.exploreTitle!!.lowercase().contains(searchingWord.lowercase())
                            || explore.exploreDetails!!.lowercase().contains(searchingWord.lowercase())
                            || explore.exploreCountry!!.lowercase().contains(searchingWord.lowercase())
                            || explore.explorePlace!!.lowercase().contains(searchingWord.lowercase()))
                        {
                            explore.exploreId = d.key
                            list.add(explore)
                        }
                    }
                }
                list.sortByDescending { it.exploreCreatedDate }
                exploreListLive.value = list
            }

            override fun onCancelled(error: DatabaseError) {
                Log.e("FirebaseError", "Firebase error")
            }

        })
    }

    fun getExploreRepository(){

        reference.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                val list = ArrayList<Explore>()

                for (d in snapshot.children){
                    val explore = d.getValue(Explore::class.java)
                    if (explore != null){
                        explore.exploreId = d.key
                        list.add(explore)
                    }
                }
                list.sortByDescending { it.exploreCreatedDate }
                val firstFiveItems = list.take(5)
                exploreListFiveLive.value = firstFiveItems
                exploreListLive.value = list
            }

            override fun onCancelled(error: DatabaseError) {
                Log.e("FirebaseError", "Firebase error")
            }

        })
    }

    fun getExploreForUserRepository(){

        reference.orderByChild("userEmail").equalTo(userEmail).addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                val list = ArrayList<Explore>()

                for (d in snapshot.children){
                    val explore = d.getValue(Explore::class.java)
                    if (explore != null){
                        explore.exploreId = d.key
                        list.add(explore)
                    }
                }
                list.sortByDescending { it.exploreCreatedDate }
                userExploreLiveData.value = list

                val exploreCountForUser = snapshot.childrenCount
                exploreCountForUserLiveData.value = exploreCountForUser
            }
            override fun onCancelled(error: DatabaseError) {
                Log.e("FirebaseError", "Firebase error")
            }
        })
    }

    fun getExploreForUserInProfileDetailsRepository(userEmail:String){

        reference.orderByChild("userEmail").equalTo(userEmail).addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                val list = ArrayList<Explore>()

                for (d in snapshot.children){
                    val explore = d.getValue(Explore::class.java)
                    if (explore != null){
                        explore.exploreId = d.key
                        list.add(explore)
                    }
                }
                list.sortByDescending { it.exploreCreatedDate }
                userExploreLiveData.value = list

                val exploreCountForUser = snapshot.childrenCount
                exploreCountForUserLiveData.value = exploreCountForUser
            }
            override fun onCancelled(error: DatabaseError) {
                Log.e("FirebaseError", "Firebase error")
            }
        })
    }

    fun getExploreForCategoryRepository(exploreCategory:String){
        reference.orderByChild("exploreCategory").equalTo(exploreCategory).addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                val list = ArrayList<Explore>()

                for (d in snapshot.children){
                    val explore = d.getValue(Explore::class.java)
                    if (explore != null){
                        explore.exploreId = d.key
                        list.add(explore)
                    }
                }
                list.sortByDescending { it.exploreCreatedDate }
                exploreListLive.value = list

            }
            override fun onCancelled(error: DatabaseError) {
                Log.e("FirebaseError", "Firebase error")
            }
        })
    }

    fun deleteExploreRepository(exploreId:String){
        reference.child(exploreId).removeValue()
        exploreCountForUserLiveData.value =-1
    }

    fun saveExploreImgRepository(uri: Uri, imageId:String){
        referenceStorage.child(imageId).putFile(uri).addOnSuccessListener {task->
            task.metadata!!.reference!!.downloadUrl.addOnSuccessListener {url->
                val imgUrl = url.toString()
                exploreImgUrl.value = imgUrl
            }
        }
    }
}