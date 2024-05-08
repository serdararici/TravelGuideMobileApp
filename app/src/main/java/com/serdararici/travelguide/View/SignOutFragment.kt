package com.serdararici.travelguide.View

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.viewModels
import androidx.navigation.NavController
import com.serdararici.travelguide.R
import com.serdararici.travelguide.ViewModel.AuthViewModel
import com.serdararici.travelguide.databinding.FragmentSignOutBinding

class SignOutFragment : Fragment() {
    private var _binding: FragmentSignOutBinding?=null
    private val binding get() = _binding!!
    private lateinit var navController: NavController
    private val viewModelAuth: AuthViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        _binding= FragmentSignOutBinding.inflate(inflater,container,false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        val toolbar = (activity as AppCompatActivity).supportActionBar?.setTitle(R.string.signOut)


        val user = viewModelAuth.currentUserViewModel()?.email.toString()

        binding.progressBarSignOut.visibility = View.VISIBLE
        viewModelAuth.signOutViewModel(){ success ->
            if (success) {

                Handler(Looper.getMainLooper()).postDelayed({
                    val intent = Intent(requireContext(), AuthActivity::class.java)
                    startActivity(intent)
                    requireActivity().finish()
                    Toast.makeText(requireContext(), "$user " + getString( R.string.signedOut), Toast.LENGTH_LONG).show()
                }, 1000) // 1000 milisaniye (1 saniye) sonra geçiş yapacak

            }
        }
    }

}