package com.serdararici.travelguide.View

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.viewModels
import androidx.navigation.NavController
import com.serdararici.travelguide.R
import com.serdararici.travelguide.ViewModel.ExploreCreateViewModel
import com.serdararici.travelguide.databinding.FragmentExploreCreateBinding

class ExploreCreateFragment : Fragment() {
    private var _binding: FragmentExploreCreateBinding?=null
    private val binding get() = _binding!!
    private lateinit var navController: NavController
    private lateinit var viewModelExploreCreate: ExploreCreateViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val tempViewModel: ExploreCreateViewModel by viewModels()
        this.viewModelExploreCreate = tempViewModel
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        _binding = FragmentExploreCreateBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        val toolbar = (activity as AppCompatActivity).supportActionBar?.setTitle(R.string.explore)

    }

}