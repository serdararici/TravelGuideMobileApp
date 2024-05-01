package com.serdararici.travelguide.ViewModel

import android.util.Log
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Spinner
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.serdararici.travelguide.Model.Country
import com.serdararici.travelguide.Utils.ApiUtils
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class CountryViewModel : ViewModel() {
    private val _countries = MutableLiveData<List<Country>>()
    val countries: LiveData<List<Country>> = _countries
    //private val countries = ArrayList<String>()
    //private lateinit var countryAdapter: ArrayAdapter<String>

    init {
        fetchCountries()
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

    fun fetchCountries1() {
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
    }

    /*fun getCountriesName(spinner: Spinner) {
        val apiService = ApiUtils.getCountriesAPI()
        //val apiService = RetrofitClient.retrofit.create(CountriesAPI::class.java)
        //apiService.getCountries().enqueue(object : Callback<NamesResult>

        apiService.getCountries().enqueue(object : Callback<List<Country>> {
            override fun onResponse(call: Call<List<Country>>, response: Response<List<Country>>) {
                if (response.isSuccessful) {
                    val countries = response.body() ?: emptyList()

                    // Tüm ülke isimlerini bir listeye ekle
                    val countryNames = countries.map { it.name.common }

                    // Bu listeyi spinner'a eklemek için ArrayAdapter kullan
                    val countryAdapter = ArrayAdapter(
                        this@MainActivity,
                        android.R.layout.simple_spinner_item,
                        countryNames
                    )
                    countryAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
                    spinner.adapter = countryAdapter

                    spinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
                        override fun onItemSelected(
                            parent: AdapterView<*>?,
                            view: View?,
                            position: Int,
                            id: Long
                        ) {
                            // Seçilen ülkeyi bul
                            val selectedCountry = countries[position]

                            Log.d("Country", "Name: ${selectedCountry.name.common}")
                            Log.d("Country", "Capital: ${selectedCountry.capital?.joinToString()}")
                            Log.d("Country", "Population: ${selectedCountry.population}")
                            Log.d("Country", "Area: ${selectedCountry.area}")
                            Log.d("Country", "Currency: ${selectedCountry.currencies?.values?.joinToString()}")
                            Log.d("Country", "Google Maps: ${selectedCountry.maps?.googleMaps}")
                        }
                        override fun onNothingSelected(parent: AdapterView<*>?) {
                            // Hiçbir şey seçilmediğinde yapılacak işlem
                        }
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
}