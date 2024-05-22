package com.serdararici.travelguide.View

import android.content.Context
import android.content.Context.MODE_PRIVATE
import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.viewModels
import androidx.navigation.NavController
import androidx.navigation.Navigation
import com.serdararici.travelguide.R
import com.serdararici.travelguide.ViewModel.ExploreViewModel
import com.serdararici.travelguide.ViewModel.ProfileViewModel
import com.serdararici.travelguide.databinding.FragmentProfileDetailsBinding
import com.serdararici.travelguide.databinding.FragmentSettingsBinding
import com.squareup.picasso.Picasso
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

class SettingsFragment : Fragment() {
    private var _binding: FragmentSettingsBinding?=null
    private val binding get() = _binding!!
    private lateinit var navController: NavController
    private lateinit var viewModelProfile: ProfileViewModel

    private val languages = ArrayList<String>()
    private lateinit var languageAdapter: ArrayAdapter<String>
    var selectedLanguage = ""
    //var selectedLanguage = ""
    //var selectedLanguagePosition = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val tempViewModelProfile: ProfileViewModel by viewModels()
        this.viewModelProfile = tempViewModelProfile
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        _binding= FragmentSettingsBinding.inflate(inflater,container,false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        val toolbar = (activity as AppCompatActivity).supportActionBar?.setTitle(R.string.settings)
        navController = Navigation.findNavController(view)


        viewModelProfile.profileListLive.observe(viewLifecycleOwner){
            if(it!= null){
                val profile = it[0]
                binding.tvSettingsUserName.text = profile.userName
                binding.tvSettingsEmail.text = profile.userEmail

                val imageView = binding.ivSettingsProfileImg
                Picasso.get()
                    .load(profile.profileImgUri)
                    .into(imageView)
                binding.progressBarSettings.visibility = View.GONE

            }
        }

        languages.add(getString(R.string.english))
        languages.add(getString(R.string.turkish))
        languages.add(getString(R.string.spanish))
        languages.add(getString(R.string.french))
        languages.add(getString(R.string.german))

        languageAdapter = ArrayAdapter(requireContext(),android.R.layout.simple_list_item_1,android.R.id.text1,languages)
        binding.spinnerSettingsLanguages.adapter = languageAdapter

        binding.spinnerSettingsLanguages.onItemSelectedListener = object  : AdapterView.OnItemSelectedListener{
            override fun onItemSelected(p0: AdapterView<*>?, p1: View?, position: Int, p3: Long) {
                //selectedLanguage = languages[position]
                //selectedLanguagePosition = position

                selectedLanguage = when (position) {
                    0 -> "en" // English
                    1 -> "tr" // Turkish
                    2 -> "es" // Spanish
                    3 -> "fr" // French
                    4 -> "de" // German
                    else -> "en"
                }

            }

            override fun onNothingSelected(p0: AdapterView<*>?) {

            }

        }
        binding.btnSettingsApplyLanguage.setOnClickListener {
            setLocale(selectedLanguage)
            // Seçilen dili SharedPreferences'te kaydedin
            val editor = requireActivity().getSharedPreferences("Settings", Context.MODE_PRIVATE).edit()
            editor.putString("My_Lang", selectedLanguage)
            editor.apply()

            // Ana aktiviteyi yeniden başlatın
            val intent = requireActivity().intent
            requireActivity().finish()
            startActivity(intent)
        }

        binding.btnSettingsEditProfile.setOnClickListener {
            val action = SettingsFragmentDirections.actionSettingsFragmentToProfileFragment()
            navController.navigate(action)
        }

        binding.tvSettingsSignOut.setOnClickListener {
            val action = SettingsFragmentDirections.actionSettingsFragmentToSignOutFragment()
            navController.navigate(action)
        }
        binding.tvSettingsChangePassword.setOnClickListener {
            navController.navigate(R.id.action_settingsFragment_to_changePasswordFragment)
        }
    }

    private fun setLocale(languageCode: String) {
        val locale = Locale(languageCode)
        Locale.setDefault(locale)
        val config = resources.configuration
        config.setLocale(locale)
        resources.updateConfiguration(config, resources.displayMetrics)
    }
}