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
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.LinearLayoutManager
import com.serdararici.travelguide.Adapter.ProfileAdapter
import com.serdararici.travelguide.Adapter.ProfileDetailsAdapter
import com.serdararici.travelguide.R
import com.serdararici.travelguide.ViewModel.ExploreViewModel
import com.serdararici.travelguide.ViewModel.ProfileViewModel
import com.serdararici.travelguide.databinding.FragmentProfileDetailsBinding
import com.squareup.picasso.Picasso
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

class ProfileDetailsFragment : Fragment() {
    private var _binding: FragmentProfileDetailsBinding?=null
    private val binding get() = _binding!!
    private lateinit var navController: NavController
    private lateinit var viewModelExplore: ExploreViewModel
    private lateinit var viewModelProfile: ProfileViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val tempViewModelExplore: ExploreViewModel by viewModels()
        this.viewModelExplore = tempViewModelExplore
        val tempViewModelProfile: ProfileViewModel by viewModels()
        this.viewModelProfile = tempViewModelProfile
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        _binding= FragmentProfileDetailsBinding.inflate(inflater,container,false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        val toolbar = (activity as AppCompatActivity).supportActionBar?.setTitle(R.string.explore)
        navController = Navigation.findNavController(view)

        binding.rvProfileDetails.layoutManager = LinearLayoutManager(requireContext())
        binding.rvProfileDetails.setHasFixedSize(true)


        val bundle:ProfileDetailsFragmentArgs by navArgs()
        val profile = bundle.profileDetails

        var userEmail = profile.userEmail

        viewModelExplore.getProfileFromExploreViewModel(userEmail!!)
        viewModelExplore.profileListLive.observe(viewLifecycleOwner){
            if(it!= null){
                val profile = it[0]
                binding.tvProfileDetailsUserName.text = profile.userName
                binding.tvProfileDetailsPostResult.text = profile.userNumberOfPosts.toString()
                //binding.tvProfilePostResult.text = profile.userNumberOfPosts.toString()

                val imageView = binding.ivProfileDetailsImg
                Picasso.get()
                    .load(profile.profileImgUri)
                    .into(imageView)
                binding.tvProfileDetailsBio.text = profile.userBio
                var age= viewModelProfile.caculateAge(profile.birthDate!!).toString()
                binding.tvProfileDetailsAgeResult.text = age
                var profileCreatedDate = profile.profileCreatedDate
                val date = Date(profileCreatedDate!!)

                // Tarihi insan tarafından okunabilir formata dönüştür
                val dateFormat = SimpleDateFormat("dd/MM/yyyy HH:mm:ss", Locale.getDefault())
                val formattedDate = dateFormat.format(date) // Dönüştürülmüş tarih
                binding.tvProfileDetailsJoinedDate.text = formattedDate

                binding.progressBarProfileDetails.visibility = View.GONE

            }
        }
        viewModelProfile.getExploreForUserInProfileDetailsViewModel(userEmail!!)
        viewModelProfile.userExploreListLive.observe(viewLifecycleOwner){
            val adapter = ProfileDetailsAdapter(requireContext(),it,viewModelProfile)
            binding.rvProfileDetails.adapter = adapter
        }

    }
}