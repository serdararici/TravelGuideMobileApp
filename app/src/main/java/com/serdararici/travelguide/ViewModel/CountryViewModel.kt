package com.serdararici.travelguide.ViewModel

import android.util.Log
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Spinner
import android.widget.Toast
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.ai.client.generativeai.GenerativeModel
import com.serdararici.travelguide.BuildConfig
import com.serdararici.travelguide.Model.Country
import com.serdararici.travelguide.Utils.ApiUtils
import kotlinx.coroutines.launch
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class CountryViewModel : ViewModel() {
    private val _countries = MutableLiveData<List<Country>>()
    val countries: LiveData<List<Country>> = _countries

    private val _countryDetails = MutableLiveData<Country>()
    val countryDetails: LiveData<Country> = _countryDetails

    //private val countries = ArrayList<String>()
    //private lateinit var countryAdapter: ArrayAdapter<String>
    private val _responseText = MutableLiveData<String>()
    val selectedCountryIndex = MutableLiveData<Int>(0)
    private val apiKey = BuildConfig.API_KEY_GEMINI
    val responseText: LiveData<String> get() = _responseText

    init {
        fetchCountries()
        //fetchCountryNames()
    }

    private fun fetchCountries() {
        val apiService = ApiUtils.getCountriesAPI()
        apiService.getCountries().enqueue(object : Callback<List<Country>> {
            override fun onResponse(call: Call<List<Country>>, response: Response<List<Country>>) {
                if (response.isSuccessful) {
                    val unsortedCountries = response.body() ?: emptyList()

                    // Listeyi alfabetik sırayla düzenleyin
                    val sortedCountries = unsortedCountries.sortedBy { it.name.common }

                    _countries.value = sortedCountries
                } else {
                    // Hata yönetimi
                    Log.e("Error", "Response code: ${response.code()}")
                }
            }

            override fun onFailure(call: Call<List<Country>>, t: Throwable) {
                // Hata yönetimi
                Log.e("Error", "API call failed: ${t.message}")
            }
        })
    }

    /*fun fetchCountries1() {
        val apiService = ApiUtils.getCountriesAPI()

        apiService.getCountries().enqueue(object : Callback<List<Country>> {
            override fun onResponse(call: Call<List<Country>>, response: Response<List<Country>>) {
                if (response.isSuccessful) {
                    val countries = response.body() ?: emptyList()

                    for (country in countries) {
                        val capital = country.capital?.getOrNull(0) ?: "Unknown"
                        val languages = country.languages?.values?.joinToString(separator = ", ") // Dil isimlerini birleştirir
                        Log.d("*******", "*********************************************")
                        Log.d("Country", "Name: ${country.name.common}, Capital: $capital")
                        Log.d("Country", "Population: ${country.population}, Subregion: ${country.subregion}")
                        Log.d("Country", "Language: ${languages}")
                        Log.d("Country", "Currency: ${country.currencies?.values?.first()?.name}, Symbol: ${country.currencies?.values?.first()?.symbol}")
                        Log.d("Country", "Area: ${country.area}, Flag PNG: ${country.flags?.png}, Google Maps: ${country.maps?.googleMaps}")
                        //Log.d("Country", "Name: ${country.name.common}, Capital: ${country.capital}")
                    }
                } else {
                    Log.e("Error", "Response code: ${response.code()}")
                }
            }

            override fun onFailure(call: Call<List<Country>>, t: Throwable) {
                Log.e("Error", "API call failed: ${t.message}")
            }
        })
    }*/

    fun generateContent(cityName: String) {
        viewModelScope.launch {
            val generativeModel = GenerativeModel(
                modelName = "gemini-pro",
                apiKey = apiKey
            )
            val prompt = "$cityName'da gezilecek yerler nelerdir?"
            val response = generativeModel.generateContent(prompt)
            _responseText.value = response.text
        }
    }


    //////
    private fun fetchCountryNames() {
        val apiService = ApiUtils.getCountriesAPI()
        apiService.getCountryNames().enqueue(object : Callback<List<Country>> {
            override fun onResponse(call: Call<List<Country>>, response: Response<List<Country>>) {
                if (response.isSuccessful) {
                    val unsortedCountries = response.body() ?: emptyList()

                    // Listeyi alfabetik sırayla düzenleyin
                    val sortedCountries = unsortedCountries.sortedBy { it.name.common }

                    _countries.value = sortedCountries
                } else {
                    // Hata yönetimi
                    Log.e("Error", "Response code: ${response.code()}")
                }
            }

            override fun onFailure(call: Call<List<Country>>, t: Throwable) {
                // Hata yönetimi
                Log.e("Error", "API call failed: ${t.message}")
            }
        })
    }

    fun fetchCountryDetails(countryName: String) {
        val apiService = ApiUtils.getCountriesAPI()
        apiService.getCountryDetails(countryName).enqueue(object : Callback<List<Country>> {
            override fun onResponse(call: Call<List<Country>>, response: Response<List<Country>>) {
                if (response.isSuccessful) {
                    val countries = response.body() ?: emptyList()
                    if (countries.isNotEmpty()) {
                        _countryDetails.value = countries[0]
                    }
                } else {
                    // Hata yönetimi
                    Log.e("Error", "Response code: ${response.code()}")
                }
            }

            override fun onFailure(call: Call<List<Country>>, t: Throwable) {
                // Hata yönetimi
                Log.e("Error", "API call failed: ${t.message}")
            }
        })
    }
}