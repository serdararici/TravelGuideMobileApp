package com.serdararici.travelguide.View

import android.animation.ObjectAnimator
import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.NavController
import androidx.navigation.Navigation
import com.serdararici.travelguide.R
import com.serdararici.travelguide.databinding.FragmentSplashBinding

class SplashFragment : Fragment() {
    private var _binding: FragmentSplashBinding? = null
    private val binding get() = _binding!!
    private lateinit var navController: NavController

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        _binding = FragmentSplashBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {

        binding.ivSpalshLogo.alpha = 0f
        binding.ivSpalshLogo.animate().setDuration(3000).alpha(1f)
        binding.progressBar.max = 1000
        val currentProgress = 1000
        ObjectAnimator.ofInt(binding.progressBar, "progress", currentProgress)
            .setDuration(3000)
            .start()
        navController= Navigation.findNavController(view)
        Handler(Looper.getMainLooper()).postDelayed({
            navController.navigate(R.id.action_splashFragment_to_signInFragment)

        }, 3000) // 2000 milisaniye (2 saniye) sonra geçiş yapacak

    }

}