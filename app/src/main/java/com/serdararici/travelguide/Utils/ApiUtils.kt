package com.serdararici.travelguide.Utils

import com.serdararici.travelguide.Service.CountriesAPI
import com.serdararici.travelguide.Service.RetrofitClient

class ApiUtils {

    companion object{
        val BASE_URL = "https://restcountries.com/"

        fun getCountriesAPI(): CountriesAPI {

            return RetrofitClient.getClient(BASE_URL).create(CountriesAPI::class.java)
        }
    }
}