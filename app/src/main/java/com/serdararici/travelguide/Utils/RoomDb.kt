package com.serdararici.travelguide.Utils

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.serdararici.travelguide.Model.Countries
import com.serdararici.travelguide.Service.CountryDao

@Database(entities = [Countries::class], version = 1)
abstract class RoomDb : RoomDatabase() {
    abstract fun getCountriesDao() : CountryDao

    companion object {
        var INSTANCE: RoomDb? = null

        fun databaseAccess(context: Context) : RoomDb? {
            if(INSTANCE == null) {
                synchronized(RoomDb::class) {
                    INSTANCE = Room.databaseBuilder(context.applicationContext,
                        RoomDb::class.java,
                        "travelGuideDb.sqlite").createFromAsset("travelGuideDb.sqlite").build()
                }
            }
            return INSTANCE
        }
    }
}