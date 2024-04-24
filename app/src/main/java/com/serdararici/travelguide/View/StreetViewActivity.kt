package com.serdararici.travelguide.View

import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.google.android.gms.maps.SupportStreetViewPanoramaFragment
import com.google.android.gms.maps.model.LatLng
import com.serdararici.travelguide.R
import com.serdararici.travelguide.databinding.ActivityMainBinding
import com.serdararici.travelguide.databinding.ActivityStreetViewBinding

class StreetViewActivity : AppCompatActivity() {
    private lateinit var binding : ActivityStreetViewBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityStreetViewBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val latitude = intent.getDoubleExtra("latitude",0.0)
        val longitude = intent.getDoubleExtra("longitude",0.0)

        val streetViewPanoramaFragment =
            supportFragmentManager.findFragmentById(R.id.fragment_streetView) as SupportStreetViewPanoramaFragment?

        val timesSquare = LatLng(40.7580, -73.9855)
        streetViewPanoramaFragment?.getStreetViewPanoramaAsync { streetViewPanorama->
            streetViewPanorama.setPosition(LatLng(latitude, longitude))
        }

    }


}