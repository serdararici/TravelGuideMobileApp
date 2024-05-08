package com.serdararici.travelguide.View

import android.app.DatePickerDialog
import android.content.DialogInterface
import android.content.Intent
import android.os.Bundle
import android.util.Patterns
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.viewModels
import androidx.navigation.NavController
import androidx.navigation.Navigation
import com.serdararici.travelguide.R
import com.serdararici.travelguide.ViewModel.AuthViewModel
import com.serdararici.travelguide.databinding.FragmentSignUpBinding
import java.util.Calendar

class SignUpFragment : Fragment() {
    private var _binding: FragmentSignUpBinding? = null
    private val binding get() = _binding!!
    private lateinit var navController: NavController
    private val viewModel: AuthViewModel by viewModels()
    //private val viewModelProfile: ProfileViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        _binding = FragmentSignUpBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        navController = Navigation.findNavController(view)

        binding.etBirthDate.setOnClickListener {
            val calendar = Calendar.getInstance()
            val year = calendar.get(Calendar.YEAR)
            val month = calendar.get(Calendar.MONTH)
            val day = calendar.get(Calendar.DAY_OF_MONTH)

            val datePicker = DatePickerDialog(
                requireContext(),
                DatePickerDialog.OnDateSetListener { datePicker, y, m, d ->
                    binding.etBirthDate.setText(("$d/${m + 1}/$y"))
                },
                year,
                month,
                day
            )

            datePicker.setTitle(R.string.choose_date)
            val set = getString(R.string.set)
            val cancel = getString(R.string.cancel)
            datePicker.setButton(DialogInterface.BUTTON_POSITIVE, set, datePicker)
            datePicker.setButton(DialogInterface.BUTTON_NEGATIVE, cancel, datePicker)

            datePicker.show()
        }

        binding.btnSignUp.setOnClickListener {
            val userName = binding.etNameSurname.text.toString()
            val email = binding.etEmailAdressSignUp.text.toString()
            val birthDate = binding.etBirthDate.text.toString()
            val password = binding.etPasswordSignUp.text.toString()

            if (checkAll()) {
                viewModel.signUpViewModel(email, password) { success, message ->
                    if(success){
                        //viewModelProfile.addProfileViewModel(userName,email,"",birthDate,0.0,0.0)
                        Toast.makeText(requireContext(), R.string.registrationSuccess, Toast.LENGTH_LONG).show()
                        val intent = Intent(requireActivity(), MainActivity::class.java)
                        startActivity(intent)
                        requireActivity().finish()
                        //navController.navigate(R.id.action_signUpFragment_to_signInFragment)
                    }else{
                        Toast.makeText(requireContext(),  getString(R.string.registrationFailed)+"$message", Toast.LENGTH_LONG ).show()
                    }
                }
            }
        }

        binding.tvSignUpToSignIn.setOnClickListener {
            navController.navigate(R.id.action_signUpFragment_to_signInFragment)
        }
    }

    private fun checkAll():Boolean {
        val userName = binding.etNameSurname.text.toString()
        val email = binding.etEmailAdressSignUp.text.toString()
        val birthDate = binding.etBirthDate.text.toString()
        val password = binding.etPasswordSignUp.text.toString()
        val passwordAgain = binding.etPasswordAgain.text.toString()
        clearErrors()
        if(userName == ""){
            binding.textInputNameSurname.error = getString(R.string.requiredUserName)
            Toast.makeText(requireContext(), R.string.requiredUserName, Toast.LENGTH_LONG).show()
            return false
        }
        if(email == ""){
            binding.textInputEmail.error = getString(R.string.requiredEmail)
            Toast.makeText(requireContext(), R.string.requiredEmail, Toast.LENGTH_LONG).show()
            return false
        }
        if(!Patterns.EMAIL_ADDRESS.matcher(email).matches()){
            binding.textInputEmail.error = getString(R.string.checkEmailFormat)
            Toast.makeText(requireContext(), R.string.checkEmailFormat, Toast.LENGTH_LONG).show()
            return false
        }
        if(birthDate == ""){
            binding.textInputBirthDate.error = getString(R.string.requiredBirtDate)
            Toast.makeText(requireContext(), R.string.requiredBirtDate, Toast.LENGTH_LONG).show()
            return false
        }
        if(password == ""){
            binding.textInputPassword.error = getString(R.string.requiredPassword)
            Toast.makeText(requireContext(), R.string.requiredPassword, Toast.LENGTH_LONG).show()
            return false
        }
        if(password.length <6){
            binding.textInputPassword.error = getString(R.string.passwordLength)
            Toast.makeText(requireContext(), R.string.passwordLength, Toast.LENGTH_LONG).show()
            return false
        }
        if(passwordAgain == ""){
            binding.textInputPasswordAgain.error = getString(R.string.requiredPasswordAgain)
            Toast.makeText(requireContext(), R.string.requiredPasswordAgain, Toast.LENGTH_LONG).show()
            return false
        }
        if(password!=passwordAgain){
            binding.textInputPasswordAgain.error = getString(R.string.passwordCheck)
            Toast.makeText(requireContext(), R.string.passwordCheck, Toast.LENGTH_LONG).show()
            return false
        }
        return true
    }
    private fun clearErrors() {
        binding.textInputNameSurname.error = null
        binding.textInputEmail.error = null
        binding.textInputBirthDate.error = null
        binding.textInputPassword.error = null
        binding.textInputPasswordAgain.error = null
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

}