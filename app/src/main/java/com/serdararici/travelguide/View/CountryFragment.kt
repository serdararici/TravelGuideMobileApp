package com.serdararici.travelguide.View

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Spinner
import android.widget.Toast
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.lifecycle.lifecycleScope
import androidx.navigation.NavController
import androidx.navigation.Navigation
import com.google.ai.client.generativeai.GenerativeModel
import com.serdararici.travelguide.BuildConfig
import com.serdararici.travelguide.R
import com.serdararici.travelguide.ViewModel.CountryGeminiResponseViewModel
import com.serdararici.travelguide.ViewModel.CountryViewModel
import com.serdararici.travelguide.databinding.FragmentCountryBinding
import com.squareup.picasso.Picasso
import kotlinx.coroutines.launch

class CountryFragment : Fragment() {
    private var _binding: FragmentCountryBinding? = null
    private val binding get() = _binding!!
    private lateinit var navController: NavController
    private lateinit var viewModelCountry: CountryViewModel
    private lateinit var viewModelCountryGeminiResponse: CountryGeminiResponseViewModel
    private var googleMaps:String? = null
    private val apiKey = BuildConfig.API_KEY_GEMINI
    private var selectedCountryName: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val tempViewModel: CountryViewModel by viewModels()
        this.viewModelCountry = tempViewModel
        val tempViewModelCountryGeminiResponse: CountryGeminiResponseViewModel by viewModels()
        this.viewModelCountryGeminiResponse = tempViewModelCountryGeminiResponse
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        _binding = FragmentCountryBinding.inflate(inflater, container ,false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        navController = Navigation.findNavController(view)
        val spinner: Spinner = binding.spinnerCountry
        binding.progressBar.visibility = View.VISIBLE

        viewModelCountry.countries.observe(viewLifecycleOwner, Observer { countries->
            if (countries != null){
                val countryNames = countries.map { it.name.common }
                binding.progressBar.visibility = View.GONE

                val adapter = ArrayAdapter(
                    requireContext(),
                    android.R.layout.simple_spinner_item,
                    countryNames
                )
                adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
                spinner.adapter = adapter
                viewModelCountry.selectedCountryIndex.value?.let {
                    spinner.setSelection(it)
                }
            }else{
                Toast.makeText(requireActivity(),R.string.noInternet, Toast.LENGTH_LONG).show()
            }

        })
        spinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener{
            override fun onItemSelected(
                parent: AdapterView<*>?,
                view: View?,
                position: Int,
                id: Long) {
                viewModelCountry.selectedCountryIndex.value = position
                val selectedCountry = viewModelCountry.countries.value?.get(position)
                selectedCountryName = selectedCountry?.name?.common
                if (selectedCountry != null){
                    val countryName = selectedCountry.name.common
                    val capitalName = selectedCountry.capital?.joinToString()
                    val languages = selectedCountry.languages?.values?.joinToString(separator = ", ")
                    val population = selectedCountry.population
                    val populationString = String.format("%,d", population)
                    val area = selectedCountry.area
                    val areaString = String.format("%,d", area?.toInt()) + " km²"
                    val region = selectedCountry.region
                    //val currency = selectedCountry.currencies?.values?.joinToString()
                    val currency = selectedCountry.currencies?.values?.first()?.name
                    val currencySembol = selectedCountry.currencies?.values?.first()?.symbol
                    val currecyResult = currency + " (${currencySembol})"
                    val flagUrl = selectedCountry.flags?.png
                    googleMaps = selectedCountry.maps?.googleMaps
                    binding.tVCountryName.text = countryName
                    binding.tvCapitalCityResult.text = capitalName
                    binding.tvLanguageResult.text = languages
                    binding.tvPopulationResult.text = populationString
                    binding.tvAreaResult.text = areaString
                    binding.tvRegionResult.text = region
                    binding.tvCurrencyResult.text = currecyResult
                    val imageView = binding.iVFlag
                    Picasso.get()
                        .load(flagUrl)
                        .into(imageView);

                    Log.d("Country", "Name: ${selectedCountry.name.common}")
                    Log.d("Country", "Capital: ${selectedCountry.capital?.joinToString()}")
                    Log.d("Country", "Population: ${selectedCountry.population}")
                    Log.d("Country", "Area: ${selectedCountry.area}")
                    Log.d("Country", "Currency: ${selectedCountry.currencies?.values?.joinToString()}")
                    Log.d("Country", "Google Maps: ${selectedCountry.maps?.googleMaps}")
                }else{
                    Toast.makeText(requireActivity(),R.string.selectCountry,Toast.LENGTH_LONG).show()
                }

            }

            override fun onNothingSelected(p0: AdapterView<*>?) {
                Toast.makeText(requireActivity(),R.string.selectCountry,Toast.LENGTH_LONG).show()
            }

        }
        binding.btnShowGoogleMaps.setOnClickListener {
            val mapUri = Uri.parse(googleMaps)
            val mapIntent = Intent(Intent.ACTION_VIEW, mapUri)

            startActivity(mapIntent)
        }

        binding.cvCountry1Overview.setOnClickListener{
            val category = getString(R.string.overview)
            sendDatatoOtherFragemnt(category)
        }
        binding.cvCountry2MustSeePlaces.setOnClickListener {
            val category = getString(R.string.mustSeePlaces)
            sendDatatoOtherFragemnt(category)
        }
        binding.cvCountry3FoodCulture.setOnClickListener{
            val category = getString(R.string.foodCulture)
            sendDatatoOtherFragemnt(category)
        }
        binding.cvCountry4ArtandHistory.setOnClickListener {
            val category = getString(R.string.artandHistory)
            sendDatatoOtherFragemnt(category)
        }
        binding.cvCountry5Events.setOnClickListener{
            val category = getString(R.string.events)
            sendDatatoOtherFragemnt(category)
        }
        binding.cvCountry6Transportation.setOnClickListener {
            val category = getString(R.string.transportation)
            sendDatatoOtherFragemnt(category)
        }
        binding.cvCountry7NaturalPlaces.setOnClickListener{
            val category = getString(R.string.naturalPlaces)
            sendDatatoOtherFragemnt(category)
        }
        binding.cvCountry8Climate.setOnClickListener {
            val category = getString(R.string.climate)
            sendDatatoOtherFragemnt(category)
        }
        binding.cvCountry9Accommodation.setOnClickListener{
            val category = getString(R.string.accommodation)
            sendDatatoOtherFragemnt(category)
        }
        binding.cvCountry10NightLife.setOnClickListener {
            val category = getString(R.string.nightLife)
            sendDatatoOtherFragemnt(category)
        }
        binding.cvCountry11SecurityandHealt.setOnClickListener{
            val category = getString(R.string.healthandSecurity)
            sendDatatoOtherFragemnt(category)
        }
        binding.cvCountry12PracticalInfo.setOnClickListener {
            val category = getString(R.string.practicalInfo)
            sendDatatoOtherFragemnt(category)
        }

    }

    private fun sendDatatoOtherFragemnt(category:String){
        val forCountry = getString(R.string.forCountry)
        val getInfoAbout = getString(R.string.getInfoAbout)
        selectedCountryName?.let { countryName ->
            val prompt = "$countryName $forCountry $category $getInfoAbout"
            val action = CountryFragmentDirections.actionCountryFragmentToCountryGeminiResponseFragment(countryName,category,prompt)
            navController.navigate(action)
        } ?: Toast.makeText(requireActivity(), R.string.selectCountry, Toast.LENGTH_SHORT).show()
    }

}