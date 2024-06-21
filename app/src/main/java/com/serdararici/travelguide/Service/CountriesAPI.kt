package com.serdararici.travelguide.Service

import com.serdararici.travelguide.Model.Country
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface CountriesAPI {

    @GET("v3.1/all")
    fun getCountries(): Call<List<Country>>

    /*@GET("v3.1/all")
    fun getCountryNames(@Query("fields") fields: String = "name"): Call<List<Country>>*/

    @GET("v3.1/all?fields=name")
    fun getCountryNames(): Call<List<Country>>

    @GET("v3.1/name/{countryName}")
    fun getCountryDetails(@Path("countryName") countryName: String): Call<List<Country>>
}