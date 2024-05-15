package com.serdararici.travelguide.View

import android.net.Uri
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.viewModels
import androidx.navigation.NavController
import androidx.navigation.Navigation
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.firebase.Timestamp
import com.serdararici.travelguide.R
import com.serdararici.travelguide.ViewModel.AuthViewModel
import com.serdararici.travelguide.ViewModel.ExploreCreateViewModel
import com.serdararici.travelguide.ViewModel.ProfileEditViewModel
import com.serdararici.travelguide.ViewModel.ProfileViewModel
import com.serdararici.travelguide.databinding.FragmentExploreCreateBinding
import java.text.SimpleDateFormat
import java.util.Locale
import java.util.UUID

class ExploreCreateFragment : Fragment() {
    private var _binding: FragmentExploreCreateBinding?=null
    private val binding get() = _binding!!
    private lateinit var navController: NavController
    private lateinit var viewModelExploreCreate: ExploreCreateViewModel
    private lateinit var viewModelAuth: AuthViewModel
    private lateinit var viewModelProfile: ProfileViewModel
    private lateinit var viewModelProfileEdit: ProfileEditViewModel

    private val categories = ArrayList<String>()
    private lateinit var categoryAdapter: ArrayAdapter<String>
    var selectedCategory = ""

    private var uri: Uri?=null
    private var profileImageUri:String?=null
    val uuid = UUID.randomUUID()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val tempViewModel: ExploreCreateViewModel by viewModels()
        this.viewModelExploreCreate = tempViewModel
        val tempViewModelAuth: AuthViewModel by viewModels()
        this.viewModelAuth = tempViewModelAuth
        val tempViewModelProfile: ProfileViewModel by viewModels()
        this.viewModelProfile = tempViewModelProfile
        val tempViewModelProfileEdit: ProfileEditViewModel by viewModels()
        this.viewModelProfileEdit = tempViewModelProfileEdit

        val bottomNavigationView =
            requireActivity()!!.findViewById<BottomNavigationView>(R.id.bottomNav)
        bottomNavigationView.visibility = View.GONE
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        _binding = FragmentExploreCreateBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        val toolbar = (activity as AppCompatActivity).supportActionBar?.setTitle(R.string.explore)
        navController = Navigation.findNavController(view)

        val pickImage = registerForActivityResult(ActivityResultContracts.GetContent()){
            binding.ivExploreCreateImage.setImageURI(it)
            if(it != null){
                uri = it
            }
        }
        binding.btnExploreCreateImg.setOnClickListener {
            pickImage.launch("image/*")
        }

        categories.add(getString(R.string.all))
        categories.add(getString(R.string.history))
        categories.add(getString(R.string.transportationAndAccommodation))
        categories.add(getString(R.string.food))
        categories.add(getString(R.string.natureAndAdvanture))
        categories.add(getString(R.string.Entertainment))
        categories.add(getString(R.string.ExpreinceAndSuggestion))

        categoryAdapter = ArrayAdapter(requireContext(),android.R.layout.simple_list_item_1,android.R.id.text1,categories)
        binding.spinnerExploreCreate.adapter = categoryAdapter

        binding.spinnerExploreCreate.onItemSelectedListener = object  : AdapterView.OnItemSelectedListener{
            override fun onItemSelected(p0: AdapterView<*>?, p1: View?, position: Int, p3: Long) {
                selectedCategory = categories[position]

            }

            override fun onNothingSelected(p0: AdapterView<*>?) {

            }

        }

        binding.btnExploreCreateSave.setOnClickListener {
            var userEmail = viewModelAuth.currentUserViewModel()?.email.toString()
            var exploreTitle = binding.etExploreCreateTitle.text.toString()
            var exploreDetails = binding.etExploreCreateTxt.text.toString()
            var exploreRatingNumber = binding.exploreCreateRatingBar.rating.toString()
            var exploreCountry = binding.etExploreCreateCountry.text.toString()
            var explorePlace = binding.etExploreCreatePlace.text.toString()
            var exploreCategory = selectedCategory
            var explorePlaceName = "${exploreCountry} - ${explorePlace}"
            var exploreCreateDate = System.currentTimeMillis()

            viewModelProfile.profileListLive.observe(viewLifecycleOwner) {
                if (it != null) {
                    val profile = it[0]
                    var profileId = profile.profileId
                    var userName = profile.userName
                    var birthDate = profile.birthDate
                    var userBio = profile.userBio
                    var numberOfPosts = profile.userNumberOfPosts
                    var profileImageUrl = profile.profileImgUri
                    var profileCreatedDate = profile.profileCreatedDate


                    if (checkAll()){

                        val imageId = "${uuid}.jpg"

                        uri?.let {
                            viewModelExploreCreate.saveExploreImgViewModel(it,imageId)
                            viewModelExploreCreate.exploreImgUrl.removeObservers(viewLifecycleOwner) // Önceki gözlemcileri kaldır
                            viewModelExploreCreate.exploreImgUrl.observe(viewLifecycleOwner) { url ->
                                viewModelExploreCreate.exploreCreateViewModel(userEmail,exploreTitle,exploreDetails,exploreRatingNumber,exploreCountry,explorePlace,exploreCategory,exploreCreateDate,url.toString())
                                numberOfPosts = numberOfPosts!! + 1
                                viewModelProfileEdit.updateProfileViewModel(profileId!!,userName!!,userEmail!!,birthDate!!,userBio!!,numberOfPosts!!,profileImageUrl!!,profileCreatedDate!!)
                                navController.navigate(R.id.action_exploreCreateFragment_to_exploreFragment)
                                Toast.makeText(requireActivity(),R.string.exploreCreateMessage,Toast.LENGTH_LONG).show()
                            }
                        } ?: run {
                            // Resim seçilmediyse mevcut resmi kullan
                            val existingImageUrl = "https://firebasestorage.googleapis.com/v0/b/finalhomework-1140c.appspot.com/o/imagesExplore%2F394db0b0-d58c-413b-b634-23a0145ab81e.jpg?alt=media&token=11a9af3b-8395-4bb5-be28-1ea8d60e0184"
                            //val existingImageUrl = explore.profileImgUri ?: ""    //güncelleme kısmında bu şekilde olabilir.
                            viewModelExploreCreate.exploreCreateViewModel(userEmail,exploreTitle,exploreDetails,exploreRatingNumber,exploreCountry,explorePlace,exploreCategory,exploreCreateDate,existingImageUrl)
                            numberOfPosts = numberOfPosts!! + 1
                            viewModelProfileEdit.updateProfileViewModel(profileId!!,userName!!,userEmail!!,birthDate!!,userBio!!,numberOfPosts!!,profileImageUrl!!,profileCreatedDate!!)
                            navController.navigate(R.id.action_exploreCreateFragment_to_exploreFragment)
                            Toast.makeText(requireActivity(),R.string.exploreCreateMessage,Toast.LENGTH_LONG).show()
                        }


                        /*viewModelExploreCreate.exploreCreateViewModel(userEmail,exploreTitle,exploreDetails,exploreRatingNumber,exploreCountry,explorePlace,exploreCreateDate)
                        numberOfPosts = numberOfPosts!! + 1
                        viewModelProfileEdit.updateProfileViewModel(profileId!!,userName!!,userEmail!!,birthDate!!,userBio!!,numberOfPosts!!,profileImageUrl!!,profileCreatedDate!!)
                        navController.navigate(R.id.action_exploreCreateFragment_to_exploreFragment)
                        Toast.makeText(requireActivity(),R.string.exploreCreateMessage,Toast.LENGTH_LONG).show()*/
                    }
                }
            }
        }
    }
    private fun checkAll():Boolean {
        val exploreTitle = binding.etExploreCreateTitle.text.toString()
        val exploreCountry = binding.etExploreCreateCountry.text.toString()
        val explorePlace = binding.etExploreCreatePlace.text.toString()
        val exploreDetails = binding.etExploreCreateTxt.text.toString()
        clearErrors()
        if(exploreTitle == ""){
            binding.textInputExploreCreateTitle.error = getString(R.string.exploreTitleRequired)
            Toast.makeText(requireContext(), R.string.exploreTitleRequired, Toast.LENGTH_LONG).show()
            return false
        }
        if(exploreCountry == ""){
            binding.textInputExploreCreateCountry.error = getString(R.string.exploreCountryRequired)
            Toast.makeText(requireContext(), R.string.exploreCountryRequired, Toast.LENGTH_LONG).show()
            return false
        }
        if(explorePlace == ""){
            binding.textInputExploreCreatePlace.error = getString(R.string.explorePlaceRequired)
            Toast.makeText(requireContext(), R.string.explorePlaceRequired, Toast.LENGTH_LONG).show()
            return false
        }
        if(exploreDetails == ""){
            binding.textInputExploreCreateTxt.error = getString(R.string.exploreDetailsRequired)
            Toast.makeText(requireContext(), R.string.exploreDetailsRequired, Toast.LENGTH_LONG).show()
            return false
        }

        return true
    }

    private fun clearErrors() {
        binding.textInputExploreCreateTitle.error = null
        binding.textInputExploreCreateCountry.error = null
        binding.textInputExploreCreatePlace.error = null
        binding.textInputExploreCreateTxt.error = null
    }

    fun formatTimestampToDate(timestamp: Timestamp): String {
        val date = timestamp.toDate()

        // Tarihi dd/MM/yyyy formatında dönüştürmek için SimpleDateFormat kullanın
        val dateFormat = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault())
        return dateFormat.format(date) // Formatlanmış tarih
    }

    override fun onDestroyView() {
        super.onDestroyView()
        val bottomNavigationView =
            requireActivity().findViewById<BottomNavigationView>(R.id.bottomNav)
        bottomNavigationView.visibility = View.VISIBLE
        _binding = null
    }

}