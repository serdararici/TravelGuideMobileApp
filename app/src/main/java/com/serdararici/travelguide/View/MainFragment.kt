package com.serdararici.travelguide.View

import android.os.Bundle
import android.os.Handler
import android.os.Looper
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.MotionEvent
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.core.graphics.createBitmap
import androidx.fragment.app.viewModels
import androidx.navigation.NavController
import androidx.navigation.Navigation
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.viewpager2.widget.CompositePageTransformer
import androidx.viewpager2.widget.MarginPageTransformer
import androidx.viewpager2.widget.ViewPager2
import com.serdararici.travelguide.Adapter.HomeExploreAdapter
import com.serdararici.travelguide.Adapter.ImageAdapter
import com.serdararici.travelguide.Adapter.ProfileDetailsAdapter
import com.serdararici.travelguide.R
import com.serdararici.travelguide.ViewModel.ExploreViewModel
import com.serdararici.travelguide.ViewModel.ProfileViewModel
import com.serdararici.travelguide.databinding.FragmentMainBinding
import com.serdararici.travelguide.databinding.FragmentProfileDetailsBinding
import com.squareup.picasso.Picasso
import kotlin.math.abs

class MainFragment : Fragment() {
    private var _binding: FragmentMainBinding?=null
    private val binding get() = _binding!!
    private lateinit var navController: NavController
    private lateinit var viewModelProfile: ProfileViewModel
    private lateinit var viewModelExplore: ExploreViewModel
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val tempViewModelProfile: ProfileViewModel by viewModels()
        this.viewModelProfile = tempViewModelProfile
        val tempViewModelExplore: ExploreViewModel by viewModels()
        this.viewModelExplore = tempViewModelExplore
    }
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View? {
        _binding= FragmentMainBinding.inflate(inflater,container,false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        val toolbar = (activity as AppCompatActivity).supportActionBar?.setTitle(R.string.app_name)
        navController = Navigation.findNavController(view)

        binding.rvHomeExplore.layoutManager = LinearLayoutManager(requireContext())
        binding.rvHomeExplore.setHasFixedSize(true)

        var viewFlipper = binding.viewFlipperHome
        binding.viewFlipperHome.startFlipping()

        viewModelProfile.profileListLive.observe(viewLifecycleOwner){
            if(it!= null){
                val profile = it[0]
                binding.tvHomeUserName.text = profile.userName
                val imageView = binding.ivProfileImg
                Picasso.get()
                    .load(profile.profileImgUri)
                    .into(imageView)
                binding.progressBarHomeForProfile.visibility = View.GONE
                binding.ivProfileImg.setOnClickListener {
                    navController.navigate(R.id.action_mainFragment_to_profileFragment)
                }
            }
        }
        binding.cvHomeMenuExplore.setOnClickListener {
            navController.navigate(R.id.action_mainFragment_to_exploreFragment)
        }
        binding.cvHomeMenuCountry.setOnClickListener {
            navController.navigate(R.id.action_mainFragment_to_countryFragment)
        }
        binding.cvHomeMenuChatbot.setOnClickListener {
            navController.navigate(R.id.action_mainFragment_to_chatBotFragment)
        }
        binding.cvHomeMenuMap.setOnClickListener {
            navController.navigate(R.id.action_mainFragment_to_mapFragment)
        }
        binding.cvHomeMenuSettings.setOnClickListener {
            navController.navigate(R.id.action_mainFragment_to_settingsFragment)
        }
        binding.cvHomeMenuProfile.setOnClickListener {
            navController.navigate(R.id.action_mainFragment_to_profileFragment)
        }

        viewModelExplore.exploreListFiveLive.observe(viewLifecycleOwner){
            val adapter = HomeExploreAdapter(requireContext(),it)
            binding.rvHomeExplore.adapter = adapter
            /*val imageView1 = binding.imagiViewHome1
            Picasso.get()
                .load(it[2].exploreImageUri)
                .into(imageView1)*/
        }
        binding.tvSeeAllHome.setOnClickListener {
            navController.navigate(R.id.action_mainFragment_to_exploreFragment)
        }
    }

}