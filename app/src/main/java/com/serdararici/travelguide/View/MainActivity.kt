package com.serdararici.travelguide.View

import android.annotation.SuppressLint
import android.content.Context
import android.os.Bundle
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.GravityCompat
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.NavigationUI
import com.serdararici.travelguide.R
import com.serdararici.travelguide.databinding.ActivityMainBinding
import java.util.Locale

class MainActivity : AppCompatActivity() {
    private lateinit var binding : ActivityMainBinding
    //private var mGoogleMap:GoogleMap? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Kaydedilen dili yükleyin
        val sharedPreferences = getSharedPreferences("Settings", Context.MODE_PRIVATE)
        val language = sharedPreferences.getString("My_Lang", "")
        if (language != null) {
            setLocale(language)
        }

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)



        val navHostFragment = supportFragmentManager.findFragmentById(R.id.nav_host_fragment) as NavHostFragment

        NavigationUI.setupWithNavController(binding.bottomNav, navHostFragment.navController)
        NavigationUI.setupWithNavController(binding.navigationView,navHostFragment.navController)

        setSupportActionBar(binding.materialToolbar)
        val toggle = ActionBarDrawerToggle(this, binding.drawer,binding.materialToolbar,
            R.string.nav_open,
            R.string.nav_close
        )
        binding.drawer.addDrawerListener(toggle)
        toggle.syncState()

        val header = binding.navigationView.inflateHeaderView(R.layout.nav_header)

        //val mapFragment = supportFragmentManager.findFragmentById(R.id.mapFragment) as SupportMapFragment
        //mapFragment.getMapAsync(this)
    }

    private fun setLocale(languageCode: String) {
        val locale = Locale(languageCode)
        Locale.setDefault(locale)
        val config = resources.configuration
        config.setLocale(locale)
        resources.updateConfiguration(config, resources.displayMetrics)
    }




    @SuppressLint("MissingSuperCall")
    override fun onBackPressed() {
        if (binding.drawer.isDrawerOpen((GravityCompat.START))) {
            binding.drawer.closeDrawer(GravityCompat.START)
        } else {
            onBackPressedDispatcher.onBackPressed()
        }
    }

    /*override fun onMapReady(googleMap: GoogleMap) {
        mGoogleMap = googleMap
    }*/


}