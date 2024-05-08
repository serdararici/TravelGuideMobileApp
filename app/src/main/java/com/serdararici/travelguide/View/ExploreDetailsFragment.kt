package com.serdararici.travelguide.View

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.viewModels
import androidx.navigation.NavController
import androidx.navigation.fragment.navArgs
import com.serdararici.travelguide.R
import com.serdararici.travelguide.ViewModel.ExploreDetailsViewModel
import com.serdararici.travelguide.databinding.FragmentExploreDetailsBinding

class ExploreDetailsFragment : Fragment() {
    private var _binding: FragmentExploreDetailsBinding?=null
    private val binding get() = _binding!!
    private lateinit var navController: NavController
    private lateinit var viewModelExploreDetails: ExploreDetailsViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val tempViewModel: ExploreDetailsViewModel by viewModels()
        this.viewModelExploreDetails = tempViewModel
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        _binding= FragmentExploreDetailsBinding.inflate(inflater,container,false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        val toolbar = (activity as AppCompatActivity).supportActionBar?.setTitle(R.string.explore)

        val bundle:ExploreDetailsFragmentArgs by navArgs()
        val exploreDetailsTitle = bundle.exploreDetails.exploreTitle
        val exploreDetailsDetails = bundle.exploreDetails.exploreDetails
        val exploreDetailsRatingNumber = bundle.exploreDetails.exploreRatingNumber
        val exploreDetailsPlaceName = bundle.exploreDetails.explorePlaceName

        val number = exploreDetailsRatingNumber?.toFloat()

        binding.tvExploreDetailsTitle.text = exploreDetailsTitle
        binding.tvExploreDetailsTxt.text = exploreDetailsDetails
        binding.ratingBarExploreDetails.rating = number!!
        binding.tvExploreDetailsPlaceName.text = exploreDetailsPlaceName
    }

}