package com.serdararici.travelguide.View

import android.Manifest
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Color
import android.location.Location
import android.net.Uri
import android.os.Bundle
import android.provider.Settings
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.PopupMenu
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.core.content.PermissionChecker.checkSelfPermission
import com.google.android.gms.common.api.Status
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationAvailability
import com.google.android.gms.location.LocationServices
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.StreetViewPanorama
import com.google.android.gms.maps.StreetViewPanoramaFragment
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.BitmapDescriptorFactory
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.Marker
import com.google.android.gms.maps.model.MarkerOptions
import com.google.android.gms.tasks.Task
import com.google.android.libraries.places.api.Places
import com.google.android.libraries.places.api.model.Place
import com.google.android.libraries.places.api.model.PlaceTypes.RESTAURANT
import com.google.android.libraries.places.api.net.FindCurrentPlaceRequest
import com.google.android.libraries.places.api.net.PlacesClient
import com.google.android.libraries.places.widget.AutocompleteSupportFragment
import com.google.android.libraries.places.widget.listener.PlaceSelectionListener
import com.google.android.material.snackbar.Snackbar
import com.serdararici.travelguide.R
import com.serdararici.travelguide.databinding.FragmentMapBinding
import org.json.JSONArray
import org.json.JSONObject

class MapFragment : Fragment(), OnMapReadyCallback {
    private var _binding: FragmentMapBinding? = null
    private val binding get() = _binding!!
    private var mGoogleMap:GoogleMap? = null
    private lateinit var placesClient: PlacesClient
    private var locationMarker: Marker? = null
    private var selectedLocationMarker: Marker? = null
    private val markerPositions = mutableListOf<LatLng>()
    private val restaurantMarkers = mutableListOf<Marker>()
    private val cafeMarkers = mutableListOf<Marker>()
    private val hotelMarkers = mutableListOf<Marker>()
    private var  locationRequestControl = 0
    private var latitude = 0.0
    private var longitude = 0.0
    private val yellowMarkers = mutableListOf<Marker>()
    private lateinit var flpc:FusedLocationProviderClient
    private lateinit var locationTask: Task<Location>
    private lateinit var autocompleteFragment:AutocompleteSupportFragment
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        _binding = FragmentMapBinding.inflate(inflater, container, false)
        val view = binding.root

        flpc = LocationServices.getFusedLocationProviderClient(requireActivity())

        val mapFragment = childFragmentManager.findFragmentById(R.id.fragmentContainerView) as? SupportMapFragment
        mapFragment?.getMapAsync(this)



        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val popupMenu = PopupMenu(requireContext(),binding.ibMapMenu)
        popupMenu.menuInflater.inflate(R.menu.map_options, popupMenu.menu)
        popupMenu.setOnMenuItemClickListener { menuItem->
            changeMap(menuItem.itemId)
            true
        }

        binding.ibMapMenu.setOnClickListener{
            popupMenu.show()
        }
        binding.ibZoomIn.setOnClickListener {
            mGoogleMap?.animateCamera(CameraUpdateFactory.zoomIn())

        }
        binding.ibZoomOut.setOnClickListener {
            mGoogleMap?.animateCamera(CameraUpdateFactory.zoomOut())
        }
        binding.ibMapLocation.setOnClickListener {
            requestLocationPermission()
        }
        binding.btRestaurants.setOnClickListener {
            loadNearbyRestaurants()
        }
        binding.btnCafes.setOnClickListener {
            loadNearbyCafes()
        }
        binding.btnHotels.setOnClickListener {
            loadNearbyHotels()
        }
        binding.ibStreeView.setOnClickListener {
            if (yellowMarkers.isNotEmpty()) {
                val intent = Intent(requireActivity(), StreetViewActivity::class.java)
                val position = yellowMarkers[0].position
                intent.putExtra("latitude", position.latitude)
                intent.putExtra("longitude", position.longitude)
                startActivity(intent)
            } else {
                Toast.makeText(
                    requireActivity(),
                    R.string.streetViewMessage,
                    Toast.LENGTH_SHORT
                ).show()
            }
        }

        /// Places Api
        Places.initialize(requireContext(), getString(R.string.google_map_api_key))

        //Places Client
        placesClient = Places.createClient(requireActivity())

        //Places AutoComplete
        autocompleteFragment = childFragmentManager.findFragmentById(R.id.autocomplete_fragment) as AutocompleteSupportFragment
        autocompleteFragment.setPlaceFields(listOf(Place.Field.ID, Place.Field.ADDRESS, Place.Field.LAT_LNG, Place.Field.NAME)) //you can get more info about place
        autocompleteFragment.setOnPlaceSelectedListener(object : PlaceSelectionListener{
            override fun onError(p0: Status) {
                Toast.makeText(requireActivity(), "Some Error on Search", Toast.LENGTH_LONG).show()
            }

            override fun onPlaceSelected(place: Place) {
                //val address = place.address
                //val id = place.id
                val name = place.name
                val latLng = place.latLng
                if (latLng != null && name != null) {
                    zoomOnMap(latLng, name)
                } else {
                    Toast.makeText(requireContext(), R.string.selectedPlaceErrorMessage, Toast.LENGTH_LONG).show()
                }
            }

        })
    }

    fun getLocationInfo(){
        locationTask.addOnSuccessListener {
            if (it != null){
                val newLocation = LatLng(it.latitude,it.longitude)
                locationMarker?.remove()
                var title= getString(R.string.yourLocation)
                //mGoogleMap?.clear()
                locationMarker = mGoogleMap?.addMarker(MarkerOptions().position(newLocation).title((title)).icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_BLUE)))
                val cameraUpdate = CameraUpdateFactory.newLatLngZoom(newLocation, 15f)
                mGoogleMap?.animateCamera(cameraUpdate, 1000, null)
                println("lattitude: ${it.latitude}, longitude: ${it.longitude}")
            }else{
                println("lattitude: alınamadı, longitude: alınamadı")
            }
        }
    }
    private fun requestLocationPermission(){
        locationRequestControl = ContextCompat.checkSelfPermission(requireContext(),Manifest.permission.ACCESS_FINE_LOCATION)

        if (locationRequestControl != PackageManager.PERMISSION_GRANTED){//permission is denied
            ActivityCompat.requestPermissions(requireActivity(),
                arrayOf(Manifest.permission.ACCESS_FINE_LOCATION),100)
            if (ActivityCompat.shouldShowRequestPermissionRationale(requireActivity(), Manifest.permission.ACCESS_FINE_LOCATION)) {
                // İzin isteme mantığı - izin neden gerekli?
                Toast.makeText(context, R.string.locationPermissionMessage, Toast.LENGTH_LONG).show()

                // İzin tekrar isteniyor
                ActivityCompat.requestPermissions(requireActivity(), arrayOf(Manifest.permission.ACCESS_FINE_LOCATION), 100)

            } else {
                // İzin kalıcı olarak reddedilmiş, kullanıcıyı ayarlara yönlendirin
                Toast.makeText(context, R.string.locationDeniedMessage, Toast.LENGTH_LONG).show()

                val intent = Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS)
                intent.data = Uri.fromParts("package", requireActivity().packageName, null)
                startActivity(intent)
            }

        }else{
            locationTask = flpc.lastLocation
            getLocationInfo()
        }
    }
    private fun showLocation(){
        locationRequestControl = ContextCompat.checkSelfPermission(requireContext(),Manifest.permission.ACCESS_FINE_LOCATION)

        if (locationRequestControl == PackageManager.PERMISSION_GRANTED) {
            locationTask = flpc.lastLocation
            locationTask.addOnSuccessListener {
                if (it != null) {
                    val location = LatLng(it.latitude, it.longitude)
                    locationMarker?.remove()
                    var title = getString(R.string.yourLocation)
                    //mGoogleMap?.clear()
                    mGoogleMap?.addMarker(MarkerOptions().position(location).title((title)).icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_BLUE)))
                    val cameraUpdate = CameraUpdateFactory.newLatLngZoom(location, 15f)
                    mGoogleMap?.animateCamera(cameraUpdate, 1000, null)
                    println("lattitude: ${it.latitude}, longitude: ${it.longitude}")
                } else {
                    println("lattitude: alınamadı, longitude: alınamadı")
                }
            }
        }
    }
    private fun zoomOnMap(latLng:LatLng, name:String){
        val location = LatLng(latLng.latitude,latLng.longitude)
        //mGoogleMap?.clear()
        selectedLocationMarker?.remove()
        selectedLocationMarker = mGoogleMap?.addMarker(MarkerOptions().position(location).title(name))
        val newLatLngZoom = CameraUpdateFactory.newLatLngZoom(latLng,12f)
        mGoogleMap?.animateCamera(newLatLngZoom)
    }


    @Deprecated("Deprecated in Java")
    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
    super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if(requestCode == 100){
            locationRequestControl = ContextCompat.checkSelfPermission(requireContext(),Manifest.permission.ACCESS_FINE_LOCATION)

            if(grantResults[0] == PackageManager.PERMISSION_GRANTED){
                var locationRequestGranted = getString(R.string.locationRequestGranted)
                Toast.makeText(requireContext(), locationRequestGranted,Toast.LENGTH_LONG).show()
                locationTask = flpc.lastLocation
                getLocationInfo()

            }else{
                //var locationRequestDenied = getString(R.string.locationRequestDenied)
                Toast.makeText(requireContext(), R.string.locationRequestDenied,Toast.LENGTH_LONG).show()
                //Toast.makeText(requireContext(), "reddedildi",Toast.LENGTH_LONG).show()
            }
        }
    }

    private fun changeMap(itemId: Int){
        when(itemId){
            R.id.normal_map -> mGoogleMap?.mapType = GoogleMap.MAP_TYPE_NORMAL
            R.id.hybrid_map -> mGoogleMap?.mapType = GoogleMap.MAP_TYPE_HYBRID
            R.id.satellite_map -> mGoogleMap?.mapType = GoogleMap.MAP_TYPE_SATELLITE
            R.id.terrain_map -> mGoogleMap?.mapType = GoogleMap.MAP_TYPE_TERRAIN
        }
    }

    override fun onMapReady(googleMap: GoogleMap) {
        mGoogleMap = googleMap
        showLocation()

        val savedMarkers = loadMarkers(requireContext())
        for (position in savedMarkers) {
            addCustomMarker(position)
            markerPositions.add(position)
        }

        //to show streetView
        mGoogleMap?.setOnMapClickListener {
            addStreetViewMarker(it)
            latitude = it.latitude
            longitude = it.longitude
        }
        //to show flag icon when user click long on the map
        mGoogleMap?.setOnMapLongClickListener { position->
            addCustomMarker(position)
            markerPositions.add(position)
            Toast.makeText(requireContext(),R.string.mapLongClickMessage,Toast.LENGTH_LONG).show()
        }
        //to delete marker on the map
        mGoogleMap?.setOnMarkerClickListener { marker->
            Snackbar.make(requireView(),R.string.mapMarkerClickMessage,Snackbar.LENGTH_SHORT)
                .setAction(R.string.yes){
                    marker.remove()

                    // Marker'ı listeden çıkar
                    markerPositions.remove(marker.position)

                    // SharedPreferences'ı güncelle
                    saveMarkers(requireContext(), markerPositions)
            }
                .setActionTextColor(Color.GREEN)
            .show()
            false
        }
    }
    override fun onStop() {
        super.onStop()
        // Marker konumlarını sakla
        saveMarkers(requireContext(), markerPositions)
    }
    private fun addMarker(position:LatLng){
        mGoogleMap?.addMarker(MarkerOptions().position(position).title("Marker"))
    }
    private fun addCustomMarker(position: LatLng){
        mGoogleMap?.addMarker(MarkerOptions()
            .position(position)
            .title("Custom Marker")
            .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_GREEN))
        )
    }
    private fun addStreetViewMarker(position: LatLng){
        // Tüm mevcut sarı marker'ları kaldır
        for (marker in yellowMarkers) {
            marker.remove()
        }
        // Listeyi temizle
        yellowMarkers.clear()

        // Yeni sarı marker ekle ve listeye ekle
        val newMarker = mGoogleMap?.addMarker(
            MarkerOptions()
                .position(position)
                .title("Street View Marker")
                .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_YELLOW))
        )

        if (newMarker != null) {
            yellowMarkers.add(newMarker)
        }
    }
    fun saveMarkers(context: Context, markerPositions: List<LatLng>) {
        val jsonArray = JSONArray()
        for (latLng in markerPositions) {
            val jsonObject = JSONObject()
            jsonObject.put("latitude", latLng.latitude)
            jsonObject.put("longitude", latLng.longitude)
            jsonArray.put(jsonObject)
        }

        val sharedPreferences = context.getSharedPreferences("markers", Context.MODE_PRIVATE)
        with(sharedPreferences.edit()) {
            putString("marker_positions", jsonArray.toString())
            apply()
        }
    }
    fun loadMarkers(context: Context): List<LatLng> {
        val sharedPreferences = context.getSharedPreferences("markers", Context.MODE_PRIVATE)
        val jsonString = sharedPreferences.getString("marker_positions", "[]")
        val jsonArray = JSONArray(jsonString)

        val markerPositions = mutableListOf<LatLng>()
        for (i in 0 until jsonArray.length()) {
            val jsonObject = jsonArray.getJSONObject(i)
            val latitude = jsonObject.getDouble("latitude")
            val longitude = jsonObject.getDouble("longitude")
            markerPositions.add(LatLng(latitude, longitude))
        }

        return markerPositions
    }

    //////// Map Nearby ///////////
    private fun loadNearbyRestaurants() {
        val placeFields = listOf(Place.Field.NAME, Place.Field.ADDRESS, Place.Field.LAT_LNG ,Place.Field.TYPES)
        val request = FindCurrentPlaceRequest.newInstance(placeFields)

        val locationClient = LocationServices.getFusedLocationProviderClient(requireActivity())
        locationClient.lastLocation.addOnSuccessListener { location: Location? ->
            if (location != null) {
                val userLocation = LatLng(location.latitude, location.longitude)

                if (ContextCompat.checkSelfPermission(requireContext(), Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
                    val placeResponse = placesClient.findCurrentPlace(request)
                    placeResponse.addOnCompleteListener { task ->
                        if (task.isSuccessful) {
                            val response = task.result
                            restaurantMarkers.clear() // Eski markerları temizleyelim
                            var restaurantFound = false // Restoran bulunup bulunmadığını takip etmek için bayrak
                            for (placeLikelihood in response.placeLikelihoods) {
                                val place = placeLikelihood.place
                                if (place.placeTypes == null) {
                                    Log.w("PlaceCheck", "Place types is null for ${place.name}")
                                }
                                if (place.types != null && place.types.contains(Place.Type.RESTAURANT) ) {
                                    val marker = mGoogleMap?.addMarker(
                                        MarkerOptions()
                                            .position(place.latLng!!)
                                            .title(place.name)
                                    )
                                    Toast.makeText(requireActivity(),R.string.nearRestaurants,Toast.LENGTH_SHORT).show()
                                    restaurantMarkers.add(marker!!) // Marker'ları sakla
                                    restaurantFound = true
                                }
                            }
                            if (!restaurantFound) {
                                Toast.makeText(requireActivity(), R.string.restaurantNotFound, Toast.LENGTH_LONG).show()
                            }
                        } else {
                            val exception = task.exception
                            Log.e("PlaceError", "Place request failed: ${exception?.message}")
                            Toast.makeText(requireActivity(), R.string.restaurantNotFound, Toast.LENGTH_LONG).show()
                        }
                    }
                } else {
                    Toast.makeText(requireActivity(), R.string.locationDeniedMessage,Toast.LENGTH_LONG).show()
                    requestLocationPermission()
                }
            }else{
                Toast.makeText(requireActivity(), R.string.locationDeniedMessage,Toast.LENGTH_LONG).show()
            }
        }
    }
    private fun removeRestaurantMarkers() {
        for (marker in restaurantMarkers) {
            marker.remove() // Marker'ı haritadan kaldır
        }

        restaurantMarkers.clear() // Listeyi temizle
        Toast.makeText(requireActivity(), "Restoran markerları kaldırıldı", Toast.LENGTH_SHORT).show()
    }

    private fun loadNearbyCafes() {
        val placeFields = listOf(Place.Field.NAME, Place.Field.ADDRESS, Place.Field.LAT_LNG ,Place.Field.TYPES)
        val request = FindCurrentPlaceRequest.newInstance(placeFields)

        val locationClient = LocationServices.getFusedLocationProviderClient(requireActivity())
        locationClient.lastLocation.addOnSuccessListener { location: Location? ->
            if (location != null) {
                val userLocation = LatLng(location.latitude, location.longitude)

                if (ContextCompat.checkSelfPermission(requireContext(), Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
                    val placeResponse = placesClient.findCurrentPlace(request)
                    placeResponse.addOnCompleteListener { task ->
                        if (task.isSuccessful) {
                            val response = task.result
                            cafeMarkers.clear() // Eski markerları temizle
                            var cafeFound = false // Restoran bulunup bulunmadığını takip etmek için bayrak
                            for (placeLikelihood in response.placeLikelihoods) {
                                val place = placeLikelihood.place
                                if (place.placeTypes == null) {
                                    Log.w("PlaceCheck", "Place types is null for ${place.name}")
                                }
                                if (place.types != null && place.types.contains(Place.Type.CAFE)) {
                                    val marker = mGoogleMap?.addMarker(
                                        MarkerOptions()
                                            .position(place.latLng!!)
                                            .title(place.name)
                                    )
                                    Toast.makeText(requireActivity(),R.string.nearCafes,Toast.LENGTH_SHORT).show()
                                    restaurantMarkers.add(marker!!) // Marker'ları sakla
                                    cafeFound = true
                                }
                            }
                            if (!cafeFound) {
                                Toast.makeText(requireActivity(), R.string.cafeNotFound, Toast.LENGTH_LONG).show()
                            }
                        } else {
                            val exception = task.exception
                            Log.e("PlaceError", "Place request failed: ${exception?.message}")
                            Toast.makeText(requireActivity(), R.string.cafeNotFound, Toast.LENGTH_LONG).show()
                        }
                    }
                } else {
                    Toast.makeText(requireActivity(), R.string.locationDeniedMessage,Toast.LENGTH_LONG).show()
                    requestLocationPermission()
                }
            }else{
                Toast.makeText(requireActivity(), R.string.locationDeniedMessage,Toast.LENGTH_LONG).show()
            }
        }
    }

    private fun loadNearbyHotels() {
        val placeFields = listOf(Place.Field.NAME, Place.Field.ADDRESS, Place.Field.LAT_LNG ,Place.Field.TYPES)
        val request = FindCurrentPlaceRequest.newInstance(placeFields)

        val locationClient = LocationServices.getFusedLocationProviderClient(requireActivity())
        locationClient.lastLocation.addOnSuccessListener { location: Location? ->
            if (location != null) {
                val userLocation = LatLng(location.latitude, location.longitude)

                if (ContextCompat.checkSelfPermission(requireContext(), Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
                    val placeResponse = placesClient.findCurrentPlace(request)
                    placeResponse.addOnCompleteListener { task ->
                        if (task.isSuccessful) {
                            val response = task.result
                            hotelMarkers.clear() // Eski markerları temizle
                            var hotelFound = false // Restoran bulunup bulunmadığını takip etmek için bayrak
                            for (placeLikelihood in response.placeLikelihoods) {
                                val place = placeLikelihood.place
                                if (place.placeTypes == null) {
                                    Log.w("PlaceCheck", "Place types is null for ${place.name}")
                                }
                                if (place.types != null && place.types.contains(Place.Type.LODGING)) {
                                    val marker = mGoogleMap?.addMarker(
                                        MarkerOptions()
                                            .position(place.latLng!!)
                                            .title(place.name)
                                    )
                                    Toast.makeText(requireActivity(),R.string.nearHotels,Toast.LENGTH_SHORT).show()
                                    restaurantMarkers.add(marker!!) // Marker'ları sakla
                                    hotelFound = true
                                }
                            }
                            if (!hotelFound) {
                                Toast.makeText(requireActivity(), R.string.hotelNotFound, Toast.LENGTH_LONG).show()
                            }
                        } else {
                            val exception = task.exception
                            Log.e("PlaceError", "Place request failed: ${exception?.message}")
                            Toast.makeText(requireActivity(), R.string.hotelNotFound, Toast.LENGTH_LONG).show()
                        }
                    }
                } else {
                    Toast.makeText(requireActivity(), R.string.locationDeniedMessage,Toast.LENGTH_LONG).show()
                    requestLocationPermission()
                }
            }else{
                Toast.makeText(requireActivity(), R.string.locationDeniedMessage,Toast.LENGTH_LONG).show()
            }
        }
    }

}