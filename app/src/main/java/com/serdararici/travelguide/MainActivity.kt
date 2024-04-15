package com.serdararici.travelguide

import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.GravityCompat
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.NavigationUI
import com.serdararici.travelguide.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {
    private lateinit var binding : ActivityMainBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val navHostFragment = supportFragmentManager.findFragmentById(R.id.nav_host_fragment) as NavHostFragment

        NavigationUI.setupWithNavController(binding.bottomNav, navHostFragment.navController)
        NavigationUI.setupWithNavController(binding.navigationView,navHostFragment.navController)

        setSupportActionBar(binding.materialToolbar)
        val toggle = ActionBarDrawerToggle(this, binding.drawer,binding.materialToolbar,R.string.nav_open, R.string.nav_close)
        binding.drawer.addDrawerListener(toggle)
        toggle.syncState()

        val header = binding.navigationView.inflateHeaderView(R.layout.nav_header)
    }




    override fun onBackPressed() {
        if (binding.drawer.isDrawerOpen((GravityCompat.START))) {
            binding.drawer.closeDrawer(GravityCompat.START)
        } else {
            onBackPressedDispatcher.onBackPressed()
        }
    }


}