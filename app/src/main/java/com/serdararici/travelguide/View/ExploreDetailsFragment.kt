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
import com.serdararici.travelguide.Adapter.ProfileDetailsAdapter
import com.serdararici.travelguide.R
import com.serdararici.travelguide.ViewModel.ExploreDetailsViewModel
import com.serdararici.travelguide.ViewModel.ExploreViewModel
import com.serdararici.travelguide.ViewModel.ProfileViewModel
import com.serdararici.travelguide.databinding.FragmentExploreDetailsBinding
import com.squareup.picasso.Picasso
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

class ExploreDetailsFragment : Fragment() {
    private var _binding: FragmentExploreDetailsBinding?=null
    private val binding get() = _binding!!
    private lateinit var navController: NavController
    private lateinit var viewModelExploreDetails: ExploreDetailsViewModel
    private lateinit var viewModelExplore: ExploreViewModel

    private val categories = ArrayList<String>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val tempViewModel: ExploreDetailsViewModel by viewModels()
        this.viewModelExploreDetails = tempViewModel
        val tempViewModelExplore: ExploreViewModel by viewModels()
        this.viewModelExplore = tempViewModelExplore
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
        val exploreDetailsCountry = bundle.exploreDetails.exploreCountry
        val exploreDetailsPlace = bundle.exploreDetails.explorePlace
        val exploreDetailsCategory = bundle.exploreDetails.exploreCategory
        val exploreDetailsImage = bundle.exploreDetails.exploreImageUri
        val exploreDetailsCreatedDate = bundle.exploreDetails.exploreCreatedDate

        val date = Date(exploreDetailsCreatedDate!!)

        // Tarihi insan tarafından okunabilir formata dönüştür
        val dateFormat = SimpleDateFormat("dd/MM/yyyy HH:mm:ss", Locale.getDefault())
        val formattedDate = dateFormat.format(date) // Dönüştürülmüş tarih

        val explorePlaceName = "$exploreDetailsPlace - $exploreDetailsCountry"
        val number = exploreDetailsRatingNumber?.toFloat()

        val userEmail = bundle.exploreDetails.userEmail
        viewModelExplore.getProfileFromExploreViewModel(userEmail!!)
        viewModelExplore.profileListLive.observe(viewLifecycleOwner){
            if(it!= null) {
                val profile = it[0]
                val userName = profile.userName
                binding.tvExploreDetailsUserName.text = userName
                val imageView = binding.ivPExploreDetailsImg
                Picasso.get()
                    .load(profile.profileImgUri)
                    .into(imageView)
            }

        }

        categories.add(getString(R.string.all))
        categories.add(getString(R.string.history))
        categories.add(getString(R.string.transportationAndAccommodation))
        categories.add(getString(R.string.food))
        categories.add(getString(R.string.natureAndAdvanture))
        categories.add(getString(R.string.Entertainment))
        categories.add(getString(R.string.ExpreinceAndSuggestion))

        if (exploreDetailsCategory?.toInt() in categories.indices) {
            val selectedCategory = categories[exploreDetailsCategory!!.toInt()]
            binding.tvExploreDetailsCategory.text = selectedCategory
        }
        binding.tvExploreDetailsTitle.text = exploreDetailsTitle
        binding.tvExploreDetailsTxt.text = exploreDetailsDetails
        binding.ratingBarExploreDetails.rating = number!!
        binding.tvExploreDetailsPlaceName.text = explorePlaceName
        binding.tvExploreDetailsCreatedDateResult.text = formattedDate
        val imageView = binding.ivExploreDetails
        Picasso.get()
            .load(bundle.exploreDetails.exploreImageUri)
            .into(imageView);
    }

}