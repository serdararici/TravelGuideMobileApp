package com.serdararici.travelguide.View

import android.net.Uri
import android.os.Bundle
import android.util.Log
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
import androidx.navigation.fragment.navArgs
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.serdararici.travelguide.R
import com.serdararici.travelguide.ViewModel.ExploreEditViewModel
import com.serdararici.travelguide.databinding.FragmentExploreEditBinding
import com.squareup.picasso.Picasso
import java.util.UUID

class ExploreEditFragment : Fragment() {
    private var _binding: FragmentExploreEditBinding?=null
    private val binding get() = _binding!!
    private lateinit var navController: NavController
    private lateinit var viewModelExploreEdit: ExploreEditViewModel

    private val categories = ArrayList<String>()
    private lateinit var categoryAdapter: ArrayAdapter<String>
    var selectedCategory = ""
    var selectedCategoryPosition =0

    private var uri: Uri?=null
    private var profileImageUri:String?=null
    val uuid = UUID.randomUUID()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val tempViewModel: ExploreEditViewModel by viewModels()
        this.viewModelExploreEdit = tempViewModel

        val bottomNavigationView =
            requireActivity()!!.findViewById<BottomNavigationView>(R.id.bottomNav)
        bottomNavigationView.visibility = View.GONE
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        _binding = FragmentExploreEditBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        val toolbar = (activity as AppCompatActivity).supportActionBar?.setTitle(R.string.explore)


        navController = Navigation.findNavController(view)

        val bundle:ExploreEditFragmentArgs by navArgs()
        val explore = bundle.exploreEdit

        binding.etExploreEditTitle.setText(explore.exploreTitle)
        binding.etExploreEditCountry.setText(explore.exploreCountry)
        binding.etExploreEditPlace.setText(explore.explorePlace)
        //binding.spinnerExploreEdit
        binding.etExploreEditTxt.setText(explore.exploreDetails)
        binding.exploreEditRatingBar.rating = (explore.exploreRatingNumber!!.toFloat())

        val imageView = binding.ivExploreEditImage
        Picasso.get()
            .load(explore.exploreImageUri)
            .into(imageView);

        val pickImage = registerForActivityResult(ActivityResultContracts.GetContent()){
            binding.ivExploreEditImage.setImageURI(it)
            if(it != null){
                uri = it
            }
        }
        binding.btnExploreEditImg.setOnClickListener {
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
        binding.spinnerExploreEdit.adapter = categoryAdapter

        binding.spinnerExploreEdit.onItemSelectedListener = object  : AdapterView.OnItemSelectedListener{
            override fun onItemSelected(p0: AdapterView<*>?, p1: View?, position: Int, p3: Long) {
                selectedCategory = categories[position]
                selectedCategoryPosition =position

            }

            override fun onNothingSelected(p0: AdapterView<*>?) {

            }

        }

        binding.btnExploreEditSave.setOnClickListener {
            var exploreTitle = binding.etExploreEditTitle.text.toString()
            var exploreDetails = binding.etExploreEditTxt.text.toString()
            var ratingExplore = binding.exploreEditRatingBar.rating.toString()
            var exploreCountry = binding.etExploreEditCountry.text.toString()
            var explorePlace = binding.etExploreEditPlace.text.toString()
            var exploreCategory = selectedCategoryPosition.toString()
            var exploreCreatedDate = explore.exploreCreatedDate
            //binding.spinnerExploreEdit



            var exploreId = explore.exploreId

            if (checkAll()){

                uri?.let {
                    viewModelExploreEdit.saveExploreImgViewModel(it, exploreId!!)
                    viewModelExploreEdit.exploreImgUrl.removeObservers(viewLifecycleOwner) // Önceki gözlemcileri kaldır
                    viewModelExploreEdit.exploreImgUrl.observe(viewLifecycleOwner) { url ->
                        viewModelExploreEdit.updateExploreViewModel(exploreId!!, exploreTitle, exploreDetails, ratingExplore, exploreCountry,explorePlace,exploreCategory,exploreCreatedDate!!, url.toString())
                        navController.navigate(R.id.action_exploreEditFragment_to_profileFragment)
                        Toast.makeText(requireActivity(),R.string.exploreUpdateMessage,Toast.LENGTH_LONG).show()
                    }
                } ?: run {
                    // Resim seçilmediyse mevcut resmi kullan
                    val existingImageUrl = explore.exploreImageUri ?: ""
                    viewModelExploreEdit.updateExploreViewModel(exploreId!!, exploreTitle, exploreDetails, ratingExplore, exploreCountry,explorePlace,exploreCategory,exploreCreatedDate!!, existingImageUrl)
                    navController.navigate(R.id.action_exploreEditFragment_to_profileFragment)
                    Toast.makeText(requireActivity(),R.string.exploreUpdateMessage,Toast.LENGTH_LONG).show()
                }
            }
        }
    }

    private fun checkAll():Boolean {
        val exploreTitle = binding.etExploreEditTitle.text.toString()
        val exploreCountry = binding.etExploreEditCountry.text.toString()
        val explorePlace = binding.etExploreEditPlace.text.toString()
        val exploreDetails = binding.etExploreEditTxt.text.toString()
        clearErrors()
        if(exploreTitle == ""){
            binding.textInputExploreEditTitle.error = getString(R.string.exploreTitleRequired)
            Toast.makeText(requireContext(), R.string.exploreTitleRequired, Toast.LENGTH_LONG).show()
            return false
        }
        if(exploreCountry == ""){
            binding.textInputExploreEditCountry.error = getString(R.string.exploreCountryRequired)
            Toast.makeText(requireContext(), R.string.exploreCountryRequired, Toast.LENGTH_LONG).show()
            return false
        }
        if(explorePlace == ""){
            binding.textInputExploreEditPlace.error = getString(R.string.explorePlaceRequired)
            Toast.makeText(requireContext(), R.string.explorePlaceRequired, Toast.LENGTH_LONG).show()
            return false
        }
        if(exploreDetails == ""){
            binding.textInputExploreEditTxt.error = getString(R.string.exploreDetailsRequired)
            Toast.makeText(requireContext(), R.string.exploreDetailsRequired, Toast.LENGTH_LONG).show()
            return false
        }

        return true
    }

    private fun clearErrors() {
        binding.textInputExploreEditTitle.error = null
        binding.textInputExploreEditCountry.error = null
        binding.textInputExploreEditPlace.error = null
        binding.textInputExploreEditTxt.error = null
    }

    override fun onDestroyView() {
        super.onDestroyView()
        val bottomNavigationView =
            requireActivity().findViewById<BottomNavigationView>(R.id.bottomNav)
        bottomNavigationView.visibility = View.VISIBLE
        _binding = null
    }

}