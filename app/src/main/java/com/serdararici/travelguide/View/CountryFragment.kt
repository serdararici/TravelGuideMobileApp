package com.serdararici.travelguide.View

import android.content.Context
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
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.lifecycle.lifecycleScope
import androidx.navigation.NavController
import androidx.navigation.Navigation
import com.google.ai.client.generativeai.GenerativeModel
import com.serdararici.travelguide.BuildConfig
import com.serdararici.travelguide.Model.Countries
import com.serdararici.travelguide.R
import com.serdararici.travelguide.Service.CountryDao
import com.serdararici.travelguide.Utils.RoomDb
import com.serdararici.travelguide.ViewModel.CountryGeminiResponseViewModel
import com.serdararici.travelguide.ViewModel.CountryViewModel
import com.serdararici.travelguide.databinding.FragmentCountryBinding
import com.squareup.picasso.Picasso
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
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

    private var savedCountryNames : Set<String>? = null
    private var countryFromSpinner: String? = null

    private lateinit var db: RoomDb
    private lateinit var cdao: CountryDao

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val tempViewModel: CountryViewModel by viewModels()
        this.viewModelCountry = tempViewModel
        val tempViewModelCountryGeminiResponse: CountryGeminiResponseViewModel by viewModels()
        this.viewModelCountryGeminiResponse = tempViewModelCountryGeminiResponse

        db = RoomDb.databaseAccess(requireContext())!!
        cdao = db.getCountriesDao()
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
        val toolbar = (activity as AppCompatActivity).supportActionBar?.setTitle(R.string.countries)
        navController = Navigation.findNavController(requireView())
        val spinner: Spinner = binding.spinnerCountry
        binding.progressBar.visibility = View.VISIBLE

        savedCountryNames = getCountryNames(requireContext())
        if(savedCountryNames!=null){
            binding.progressBar.visibility = View.GONE
            val sortedCountryNames = savedCountryNames!!.sorted()
            val adapter = ArrayAdapter(
                requireContext(),
                android.R.layout.simple_spinner_item,
                sortedCountryNames
            )
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
            spinner.adapter = adapter
            viewModelCountry.selectedCountryIndex.value?.let {
                spinner.setSelection(it)
            }
            for (countries in sortedCountryNames){
                Log.e("Countries", countries )
            }
        }else{
            viewModelCountry.countries.observe(viewLifecycleOwner, Observer { countries->
                if (countries != null){
                    val countryNames = countries.map { it.name.common }
                    binding.progressBar.visibility = View.GONE

                    saveCountryNames(requireContext(),countryNames)

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
        }



        spinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener{
            override fun onItemSelected(
                parent: AdapterView<*>?,
                view: View?,
                position: Int,
                id: Long) {
                binding.progressBar.visibility = View.VISIBLE

                if(savedCountryNames!=null){
                    val sortedCountryNames = savedCountryNames!!.sorted()
                    countryFromSpinner = sortedCountryNames[position]
                    selectCountry(countryFromSpinner!!)
                    binding.progressBar.visibility = View.GONE
                }
                else{
                    viewModelCountry.selectedCountryIndex.value = position
                    val selectedCountry = viewModelCountry.countries.value?.get(position)
                    selectedCountryName = selectedCountry?.name?.common


                    /*
                    val countrySpinner = parent!!.getItemAtPosition(position).toString()
                    Log.e("Country" , countrySpinner)
                    viewModelCountry.fetchCountryDetails(countrySpinner)
                    //selectedCountryName?.let { viewModelCountry.fetchCountryDetails(it) }
                     */

                    viewModelCountry.countries.observe(viewLifecycleOwner, Observer {
                        for (country in it){
                            val countryName = country.name.common ?: "Unknown"
                            val capitalName = country.capital?.joinToString() ?: "Unknown"
                            val languages = country.languages?.values?.joinToString(separator = ", ") ?: "Unknown"
                            val populationData = country.population
                            val population = String.format("%,d", populationData) ?: "Unknown"
                            val areaData = country.area
                            val area = (String.format("%,d", areaData?.toInt()) + " km²") ?: "Unknown"
                            val region = country.subregion ?: "Unknown"
                            val currency = ( country.currencies?.values?.first()?.name + " (" + country.currencies?.values?.first()?.symbol +")") ?: "Unknown"
                            val flagUrl = country.flags?.png ?: "Unknown"
                            val mapUrl = country.maps?.googleMaps ?: "Unknown"
                            addCountryFromApi(countryName,capitalName,languages, population,area,region,currency,flagUrl,mapUrl)
                            Log.e("RoomDb", "*****Veriler Kaydedildi.*****")
                        }
                        val selectedCountry = it.get(position)

                        if (selectedCountry != null){
                            //Log.e("SelectedCountry" , selectedCountryName!!)
                            binding.progressBar.visibility = View.GONE
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
                    })
                }





            }

            override fun onNothingSelected(p0: AdapterView<*>?) {
                Toast.makeText(requireActivity(),R.string.selectCountry,Toast.LENGTH_LONG).show()
            }

        }
        /*
        viewModelCountry.countryDetails.observe(viewLifecycleOwner, Observer { selectedCountry ->
            if (selectedCountry != null) {
                val countryName = selectedCountry.name.common
                val capitalName = selectedCountry.capital?.joinToString()
                val languages = selectedCountry.languages?.values?.joinToString(separator = ", ")
                val population = selectedCountry.population
                val populationString = String.format("%,d", population)
                val area = selectedCountry.area
                val areaString = String.format("%,d", area?.toInt()) + " km²"
                val region = selectedCountry.region
                val currency = selectedCountry.currencies?.values?.first()?.name
                val currencySymbol = selectedCountry.currencies?.values?.first()?.symbol
                val currencyResult = "$currency ($currencySymbol)"
                val flagUrl = selectedCountry.flags?.png
                googleMaps = selectedCountry.maps?.googleMaps
                binding.tVCountryName.text = countryName
                binding.tvCapitalCityResult.text = capitalName
                binding.tvLanguageResult.text = languages
                binding.tvPopulationResult.text = populationString
                binding.tvAreaResult.text = areaString
                binding.tvRegionResult.text = region
                binding.tvCurrencyResult.text = currencyResult
                val imageView = binding.iVFlag
                Picasso.get()
                    .load(flagUrl)
                    .into(imageView)
            } else {
                Toast.makeText(requireActivity(), R.string.selectCountry, Toast.LENGTH_LONG).show()
            }
        })*/

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

        if(savedCountryNames!=null){
            if(countryFromSpinner!=null){
                var countryName = countryFromSpinner
                val prompt = "$countryName $forCountry $category $getInfoAbout"
                val action = CountryFragmentDirections.actionCountryFragmentToCountryGeminiResponseFragment(countryName!!,category,prompt)
                navController.navigate(action)
            }else{
                Toast.makeText(requireActivity(), R.string.selectCountry, Toast.LENGTH_SHORT).show()
            }

        }else{
            selectedCountryName?.let { countryName ->
                val prompt = "$countryName $forCountry $category $getInfoAbout"
                val action = CountryFragmentDirections.actionCountryFragmentToCountryGeminiResponseFragment(countryName,category,prompt)
                navController.navigate(action)
            } ?: Toast.makeText(requireActivity(), R.string.selectCountry, Toast.LENGTH_SHORT).show()
        }
    }

    fun saveCountryNames(context: Context, countryNames: List<String>) {
        val sharedPreferences = context.getSharedPreferences("AppPrefs", Context.MODE_PRIVATE)
        val editor = sharedPreferences.edit()
        editor.putStringSet("countryNames", countryNames.toSet())
        editor.apply()
    }

    fun getCountryNames(context: Context): Set<String>? {
        val sharedPreferences = context.getSharedPreferences("AppPrefs", Context.MODE_PRIVATE)
        return sharedPreferences.getStringSet("countryNames", null)
    }

    fun addCountryFromApi(countryName:String, capitalName:String, language:String, population:String, area:String, region:String, currency:String, flagUrl:String, mapUrl:String) {
        val job = CoroutineScope(Dispatchers.Main).launch {
            val newCountry = Countries(0, countryName, capitalName, language, population, area, region, currency, flagUrl,mapUrl)
            cdao.addCountry(newCountry)
        }
    }

    fun selectCountry(countryName:String) {
        val job = CoroutineScope(Dispatchers.Main).launch {
            val country = cdao.selectCountry(countryName)

            val countryName = country.countryName
            val capitalName = country.capitalName
            val languages = country.language
            val population = country.population
            val area = country.area
            val region = country.region
            val currency = country.currency
            val flagUrl = country.flagUrl
            val mapUrl = country.mapUrl

            binding.tVCountryName.text = countryName
            binding.tvCapitalCityResult.text = capitalName
            binding.tvLanguageResult.text = languages
            binding.tvPopulationResult.text = population
            binding.tvAreaResult.text = area
            binding.tvRegionResult.text = region
            binding.tvCurrencyResult.text = currency
            val imageView = binding.iVFlag
            Picasso.get()
                .load(flagUrl)
                .into(imageView);

            Log.e("CountryName", country.countryName)
            Log.e("capitalName", country.capitalName)
            Log.e("language", country.language)
            Log.e("population", country.population)
            Log.e("area", country.area)
            Log.e("region", country.region)
            Log.e("currency", country.currency)
            Log.e("flagUrl", country.flagUrl)
            Log.e("mapUrl", country.mapUrl)

            googleMaps = country.mapUrl
        }
    }

}