package com.serdararici.travelguide.Model

data class Country(
    val name: Name,
    val tld: List<String>?,
    val cca2: String?,
    val ccn3: String?,
    val cca3: String?,
    val cioc: String?,
    val independent: Boolean?,
    val status: String?,
    val unMember: Boolean?,
    val currencies: Map<String, Currency>?,
    val idd: IDD?,
    val capital: List<String>?,
    val altSpellings: List<String>?,
    val region: String?,
    val subregion: String?,
    val languages: Map<String, String>?,
    val translations: Map<String, Translation>?,
    val latlng: List<Double>?,
    val landlocked: Boolean?,
    val borders: List<String>?,
    val area: Double?,
    val demonyms: Map<String, Demonym>?,
    val flag: String?,
    val maps: Maps?,
    val population: Long?,
    val gini: Map<Int, Double>?,
    val fifa: String?,
    val car: Car?,
    val timezones: List<String>?,
    val continents: List<String>?,
    val flags: Flags?,
    val coatOfArms: CoatOfArms?,
    val startOfWeek: String?,
    val capitalInfo: CapitalInfo?,
    val postalCode: PostalCode?
)

data class Name(
    val common: String,
    val official: String,
    val nativeName: Map<String, NativeName>?
)

data class Currency(
    val name: String,
    val symbol: String
)

data class IDD(
    val root: String,
    val suffixes: List<String>
)

data class Translation(
    val official: String,
    val common: String
)

data class Demonym(
    val f: String,
    val m: String
)

data class Maps(
    val googleMaps: String,
    val openStreetMaps: String
)

data class Car(
    val signs: List<String>,
    val side: String
)

data class Flags(
    val png: String,
    val svg: String,
    val alt: String
)

data class CoatOfArms(
    val png: String,
    val svg: String
)

data class CapitalInfo(
    val latlng: List<Double>
)

data class PostalCode(
    val format: String,
    val regex: String
)

data class NativeName(
    val official: String,
    val common: String
)