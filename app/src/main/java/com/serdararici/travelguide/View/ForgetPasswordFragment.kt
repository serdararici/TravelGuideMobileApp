package com.serdararici.travelguide.View

import android.os.Bundle
import android.util.Patterns
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.viewModels
import androidx.navigation.NavController
import androidx.navigation.Navigation
import com.serdararici.travelguide.R
import com.serdararici.travelguide.ViewModel.AuthViewModel
import com.serdararici.travelguide.databinding.FragmentForgetPasswordBinding

class ForgetPasswordFragment : Fragment() {
    private var _binding: FragmentForgetPasswordBinding?= null
    private val binding get() = _binding!!
    private lateinit var navController: NavController
    private lateinit var viewModelAuth: AuthViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val tempViewModel: AuthViewModel by viewModels()
        this.viewModelAuth = tempViewModel
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        _binding= FragmentForgetPasswordBinding.inflate(inflater,container,false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        val toolbar = (activity as AppCompatActivity).supportActionBar?.setTitle(R.string.app_name)

        navController = Navigation.findNavController(view)

        binding.btnSend.setOnClickListener {
            val email = binding.tvEmail.text.toString()
            if(checkEmail()){
                viewModelAuth.forgetPasswordViewModel(email)
                Toast.makeText(requireContext(), R.string.checkYourEmail, Toast.LENGTH_LONG).show()
                navController.navigate(R.id.action_forgetPasswordFragment_to_signInFragment)
            }

        }

    }

    private fun checkEmail():Boolean {
        val email = binding.tvEmail.text.toString()
        if(email == ""){
            binding.textInputLayout.error = getString(R.string.requiredEmail)
            Toast.makeText(requireContext(), R.string.requiredEmail, Toast.LENGTH_LONG).show()
            return false
        }
        if(!Patterns.EMAIL_ADDRESS.matcher(email).matches()){
            binding.textInputLayout.error = getString(R.string.checkEmailFormat)
            Toast.makeText(requireContext(), R.string.checkEmailFormat, Toast.LENGTH_LONG).show()
            return false
        }
        return true
    }


}