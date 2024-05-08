package com.serdararici.travelguide.View

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.navigation.fragment.navArgs
import com.serdararici.travelguide.R
import com.serdararici.travelguide.ViewModel.CountryGeminiResponseViewModel
import com.serdararici.travelguide.databinding.FragmentCountryGeminiResponseBinding

class CountryGeminiResponseFragment : Fragment() {
    private var _binding: FragmentCountryGeminiResponseBinding? = null
    private val binding get() = _binding!!
    private lateinit var viewModelCountryGeminiResponse: CountryGeminiResponseViewModel
    private val args: CountryGeminiResponseFragmentArgs by navArgs()


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val tempViewModel: CountryGeminiResponseViewModel by viewModels()
        this.viewModelCountryGeminiResponse = tempViewModel
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        _binding = FragmentCountryGeminiResponseBinding.inflate(inflater, container ,false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        val toolbar = (activity as AppCompatActivity).supportActionBar?.setTitle(R.string.countries)
        binding.progressBar2.visibility = View.VISIBLE
        Toast.makeText(requireActivity(),R.string.pleaseWait,Toast.LENGTH_SHORT).show()
        val countryName = args.countryName
        val category = args.category
        val prompt = args.prompt
        val title = "$countryName, $category"

        binding.tvTitle.text = title
        if (category== getString(R.string.overview)){
            binding.ivCountryResponse.setImageResource(R.drawable.baseline_language_24_blue)
        }else if(category== getString(R.string.mustSeePlaces)){
                binding.ivCountryResponse.setImageResource(R.drawable.baseline_local_see_24_blue)
        }else if(category== getString(R.string.foodCulture)){
            binding.ivCountryResponse.setImageResource(R.drawable.baseline_restaurant_menu_24_blue)
        }else if(category== getString(R.string.artandHistory)){
            binding.ivCountryResponse.setImageResource(R.drawable.baseline_castle_24_blue)
        }else if(category== getString(R.string.events)){
            binding.ivCountryResponse.setImageResource(R.drawable.baseline_calendar_month_24_blue)
        }else if(category== getString(R.string.transportation)){
            binding.ivCountryResponse.setImageResource(R.drawable.baseline_directions_transit_24_blue)
        }else if(category== getString(R.string.naturalPlaces)){
            binding.ivCountryResponse.setImageResource(R.drawable.baseline_forest_24_blue)
        }else if(category== getString(R.string.climate)){
            binding.ivCountryResponse.setImageResource(R.drawable.baseline_cloud_24_blue)
        }else if(category== getString(R.string.accommodation)){
            binding.ivCountryResponse.setImageResource(R.drawable.baseline_hotel_24_blue)
        }else if(category== getString(R.string.nightLife)){
            binding.ivCountryResponse.setImageResource(R.drawable.baseline_nightlife_24_blue)
        }else if(category== getString(R.string.healthandSecurity)){
            binding.ivCountryResponse.setImageResource(R.drawable.baseline_security_24_blue)
        }else if(category== getString(R.string.practicalInfo)){
            binding.ivCountryResponse.setImageResource(R.drawable.baseline_info_24_blue)
        }
        else{
            Toast.makeText(requireActivity(),"Bir seçim yapınız.",Toast.LENGTH_SHORT).show()
        }

        viewModelCountryGeminiResponse.generateContent(countryName,category,prompt)
        viewModelCountryGeminiResponse.responseText.observe(viewLifecycleOwner, Observer { responseText ->
            if (responseText != null) {
                binding.progressBar2.visibility = View.GONE
                binding.tvGeminiResponse.text = responseText
                //println(responseText)
            } else {
                // Hata mesajı göster
                Toast.makeText(requireActivity(), R.string.noContentWarning, Toast.LENGTH_SHORT).show()
            }
        })

    }

}