package com.serdararici.travelguide.View

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.viewModels
import androidx.navigation.NavController
import androidx.navigation.Navigation
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.serdararici.travelguide.Adapter.ExploreAdapter
import com.serdararici.travelguide.Adapter.ProfileAdapter
import com.serdararici.travelguide.Model.Explore
import com.serdararici.travelguide.R
import com.serdararici.travelguide.ViewModel.ProfileEditViewModel
import com.serdararici.travelguide.ViewModel.ProfileViewModel
import com.serdararici.travelguide.databinding.FragmentProfileBinding
import com.squareup.picasso.Picasso
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

class ProfileFragment : Fragment() {
    private var _binding: FragmentProfileBinding?=null
    private val binding get() = _binding!!
    private lateinit var navController: NavController
    private lateinit var viewModelProfile: ProfileViewModel
    private lateinit var viewModelProfileEdit: ProfileEditViewModel

    private  var exploreCountforUser:Long = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val tempViewModel: ProfileViewModel by viewModels()
        this.viewModelProfile = tempViewModel
        val tempViewModelProfileEdit: ProfileEditViewModel by viewModels()
        this.viewModelProfileEdit = tempViewModelProfileEdit

        val bottomNavigationView =
            requireActivity().findViewById<BottomNavigationView>(R.id.bottomNav)
        bottomNavigationView.visibility = View.VISIBLE
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        _binding = FragmentProfileBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        val toolbar = (activity as AppCompatActivity).supportActionBar?.setTitle(R.string.profile)

        navController = Navigation.findNavController(view)

        binding.rvProfile.layoutManager = LinearLayoutManager(requireContext())
        binding.rvProfile.setHasFixedSize(true)

        viewModelProfile.userExploreListLive.observe(viewLifecycleOwner){
            val adapter = ProfileAdapter(requireContext(),it,viewModelProfile, viewModelProfileEdit)
            binding.rvProfile.adapter = adapter
        }



        viewModelProfile.profileListLive.observe(viewLifecycleOwner){
            if(it!= null){
                val profile = it[0]
                binding.tvProfileUserName.text = profile.userName
                viewModelProfile.exploreCountForUserLive.observe(viewLifecycleOwner){
                    //bu kısım profil her açıldığında post sayısını veritabanında güncelliyor. Bu kısım daha etkili bir şekilde yapılabilir. Kontro et.
                    viewModelProfileEdit.updateProfileViewModel(profile.profileId!!,profile.userName!!,profile.userEmail!!,profile.birthDate!!,profile.userBio!!,it.toInt(),profile.profileImgUri!!,profile.profileCreatedDate!!)
                    var exploreCountforUser = it
                    binding.tvProfilePostResult.text = exploreCountforUser.toString()
                }
                //binding.tvProfilePostResult.text = profile.userNumberOfPosts.toString()

                val imageView = binding.ivProfileImg
                Picasso.get()
                    .load(profile.profileImgUri)
                    .into(imageView)
                binding.tvProfileBio.text = profile.userBio
                var age= viewModelProfile.caculateAge(profile.birthDate!!).toString()
                binding.tvProfileAgeResult.text = age
                var profileCreatedDate = profile.profileCreatedDate
                val date = Date(profileCreatedDate!!)

                // Tarihi insan tarafından okunabilir formata dönüştür
                val dateFormat = SimpleDateFormat("dd/MM/yyyy HH:mm:ss", Locale.getDefault())
                val formattedDate = dateFormat.format(date) // Dönüştürülmüş tarih
                binding.tvProfileJoinedDate.text = formattedDate

                binding.progressBarProfile.visibility = View.GONE

                binding.btnProfileToProfileEdit.setOnClickListener {
                    val action = ProfileFragmentDirections.actionProfileFragmentToProfileEditFragment(profile)
                    navController.navigate(action)
                }
            }
        }


    }

}