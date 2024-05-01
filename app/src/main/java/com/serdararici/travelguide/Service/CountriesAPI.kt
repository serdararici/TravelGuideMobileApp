package com.serdararici.travelguide.Service

import com.serdararici.travelguide.Model.Country
import retrofit2.Call
import retrofit2.http.GET

interface CountriesAPI {

    @GET("v3.1/all")
    fun getCountries(): Call<List<Country>>
}