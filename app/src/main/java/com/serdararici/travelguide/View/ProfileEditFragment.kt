package com.serdararici.travelguide.View

import android.app.DatePickerDialog
import android.content.DialogInterface
import android.net.Uri
import android.os.Bundle
import android.util.Patterns
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContract
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.viewModels
import androidx.navigation.NavController
import androidx.navigation.Navigation
import androidx.navigation.fragment.navArgs
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.serdararici.travelguide.R
import com.serdararici.travelguide.ViewModel.ProfileEditViewModel
import com.serdararici.travelguide.databinding.FragmentProfileEditBinding
import com.squareup.picasso.Picasso
import java.util.Calendar

class ProfileEditFragment : Fragment() {
    private var _binding: FragmentProfileEditBinding?=null
    private val binding get() = _binding!!
    private lateinit var navController: NavController
    private lateinit var viewModelProfileEdit: ProfileEditViewModel

    private var uri: Uri?=null
    private var profileImageUri:String?=null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val tempViewModel: ProfileEditViewModel by viewModels()
        this.viewModelProfileEdit = tempViewModel

        val bottomNavigationView =
            requireActivity()!!.findViewById<BottomNavigationView>(R.id.bottomNav)
        bottomNavigationView.visibility = View.GONE
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        _binding = FragmentProfileEditBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        val toolbar = (activity as AppCompatActivity).supportActionBar?.setTitle(R.string.profile)

        navController = Navigation.findNavController(view)

        val bundle:ProfileEditFragmentArgs by navArgs()
        val profile = bundle.profileEdit

        binding.etProfileNameSurname.setText(profile.userName)
        binding.etProfileBirthDate.setText(profile.birthDate)
        binding.etProfileBio.setText(profile.userBio)

        val imageView = binding.ivProfileEditImg
        Picasso.get()
            .load(profile.profileImgUri)
            .into(imageView);

        val pickImage = registerForActivityResult(ActivityResultContracts.GetContent()){
            binding.ivProfileEditImg.setImageURI(it)
            if(it != null){
                uri = it
            }
        }
        binding.btnProfileEdtImg.setOnClickListener {
            pickImage.launch("image/*")
        }

        binding.btnProfilEditSave.setOnClickListener {
            var userName= binding.etProfileNameSurname.text.toString()
            var birthDate= binding.etProfileBirthDate.text.toString()
            var userBio = binding.etProfileBio.text.toString()
            //var profileUri=
            //var profileId= profile.profileId
            //var userEmail=profile.userEmail
            var profileId= profile.profileId
            var userEmail=profile.userEmail
            var userNumberOfPosts = profile.userNumberOfPosts
            //var profileImgUri = profile.profileImgUri         // bu değiştirlcek fotoğraf kısmnıda
            var profileCreatedDate = profile.profileCreatedDate

            /*uri?.let {
                viewModelProfileEdit.saveProfileImgViewModel(it, profileId!!)
            }*/
                if (checkAll()){

                    uri?.let {
                        viewModelProfileEdit.saveProfileImgViewModel(it, profileId!!)
                        viewModelProfileEdit.profileImgUrl.removeObservers(viewLifecycleOwner) // Önceki gözlemcileri kaldır
                        viewModelProfileEdit.profileImgUrl.observe(viewLifecycleOwner) { url ->
                            viewModelProfileEdit.updateProfileViewModel(profileId!!,userName,userEmail!!,birthDate,userBio,userNumberOfPosts!!,url.toString(),profileCreatedDate!!)
                            navController.navigate(R.id.action_profileEditFragment_to_profileFragment)
                            Toast.makeText(requireActivity(),R.string.profileUpdated,Toast.LENGTH_LONG).show()
                        }
                    } ?: run {
                        // Resim seçilmediyse mevcut resmi kullan
                        val existingImageUrl = profile.profileImgUri ?: ""
                        viewModelProfileEdit.updateProfileViewModel(profileId!!,userName,userEmail!!,birthDate,userBio,userNumberOfPosts!!,existingImageUrl,profileCreatedDate!!)
                        navController.navigate(R.id.action_profileEditFragment_to_profileFragment)
                        Toast.makeText(requireActivity(),R.string.profileUpdated,Toast.LENGTH_LONG).show()
                    }
                    /*if(url!=null){
                        profileImageUri = url.toString()
                        viewModelProfileEdit.updateProfileViewModel(profileId!!,userName,userEmail!!,birthDate,userBio,userNumberOfPosts!!,profileImageUri!!,profileCreatedDate!!)
                        navController.navigate(R.id.action_profileEditFragment_to_profileFragment)
                    }else{
                        profileImageUri = profile.profileImgUri
                        viewModelProfileEdit.updateProfileViewModel(profileId!!,userName,userEmail!!,birthDate,userBio,userNumberOfPosts!!,profileImageUri!!,profileCreatedDate!!)
                        navController.navigate(R.id.action_profileEditFragment_to_profileFragment)
                    }*/
                }

        }


        binding.etProfileBirthDate.setOnClickListener {
            val calendar = Calendar.getInstance()
            val year = calendar.get(Calendar.YEAR)
            val month = calendar.get(Calendar.MONTH)
            val day = calendar.get(Calendar.DAY_OF_MONTH)

            val datePicker = DatePickerDialog(
                requireContext(),
                DatePickerDialog.OnDateSetListener { datePicker, y, m, d ->
                    binding.etProfileBirthDate.setText(("$d/${m + 1}/$y"))
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
    }

    private fun checkAll():Boolean {
        val userName = binding.etProfileNameSurname.text.toString()
        val birthDate = binding.etProfileBirthDate.text.toString()
        clearErrors()
        if (userName == "") {
            binding.textInputProfileNameSurname.error = getString(R.string.requiredUserName)
            Toast.makeText(requireContext(), R.string.requiredUserName, Toast.LENGTH_LONG).show()
            return false
        }
        if (birthDate == "") {
            binding.textInputBirthDate.error = getString(R.string.requiredBirtDate)
            Toast.makeText(requireContext(), R.string.requiredBirtDate, Toast.LENGTH_LONG).show()
            return false
        }
        return true
    }

        private fun clearErrors() {
            binding.textInputProfileNameSurname.error = null
            binding.textInputBirthDate.error = null
        }

    override fun onDestroyView() {
        super.onDestroyView()
        val bottomNavigationView =
            requireActivity().findViewById<BottomNavigationView>(R.id.bottomNav)
        bottomNavigationView.visibility = View.VISIBLE
        _binding = null
    }

}