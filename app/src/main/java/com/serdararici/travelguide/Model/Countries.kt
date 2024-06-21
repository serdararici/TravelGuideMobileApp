package com.serdararici.travelguide.Model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import org.jetbrains.annotations.NotNull

@Entity(tableName = "countries")
data class Countries(@PrimaryKey(autoGenerate = true)
                     @ColumnInfo(name = "countryId") @NotNull var countryId:Int,
                     @ColumnInfo(name = "countryName") @NotNull var countryName:String,
                     @ColumnInfo(name = "capitalName") @NotNull var capitalName:String,
                     @ColumnInfo(name = "language") @NotNull var language:String,
                     @ColumnInfo(name = "population") @NotNull var population:String,
                     @ColumnInfo(name = "area") @NotNull var area:String,
                     @ColumnInfo(name = "region") @NotNull var region:String,
                     @ColumnInfo(name = "currency") @NotNull var currency:String,
                     @ColumnInfo(name = "flagUrl") @NotNull var flagUrl:String,
                     @ColumnInfo(name = "mapUrl") @NotNull var mapUrl:String){
}