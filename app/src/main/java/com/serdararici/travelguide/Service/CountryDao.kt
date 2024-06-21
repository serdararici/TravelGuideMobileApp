package com.serdararici.travelguide.Service

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import com.serdararici.travelguide.Model.Countries

@Dao
interface CountryDao {
    @Query("SELECT * FROM countries")
    suspend fun countries() : List<Countries>

    @Insert
    suspend fun addCountry(country:Countries)

    @Query("SELECT * FROM countries ORDER BY RANDOM() LIMIT 1")
    suspend fun randomCountry() : List<Countries>

    @Query("SELECT * FROM countries WHERE countryName like '%' || :countryName || '%'")
    suspend fun findCountry(countryName:String) : List<Countries>

    @Query("SELECT * FROM countries WHERE countryName=:countryName")
    suspend fun selectCountry(countryName:String) : Countries
}