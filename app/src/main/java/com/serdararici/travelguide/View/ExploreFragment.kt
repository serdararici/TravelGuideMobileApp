package com.serdararici.travelguide.View

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.LinearLayoutCompat
import androidx.appcompat.widget.SearchView
import androidx.core.view.MenuProvider
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.navigation.NavController
import androidx.navigation.Navigation
import androidx.recyclerview.widget.LinearLayoutManager
import com.serdararici.travelguide.Adapter.ExploreAdapter
import com.serdararici.travelguide.Model.Explore
import com.serdararici.travelguide.R
import com.serdararici.travelguide.ViewModel.ExploreViewModel
import com.serdararici.travelguide.databinding.FragmentExploreBinding

class ExploreFragment : Fragment(), SearchView.OnQueryTextListener {
    private var _binding: FragmentExploreBinding?=null
    private val binding get() = _binding!!
    private lateinit var navController: NavController
    private lateinit var viewModelExplore: ExploreViewModel

    private val categories = ArrayList<String>()
    private lateinit var categoryAdapter:ArrayAdapter<String>
    var selectedCategory = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val tempViewModel: ExploreViewModel by viewModels()
        this.viewModelExplore = tempViewModel
    }
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding= FragmentExploreBinding.inflate(inflater,container,false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        val toolbar = (activity as AppCompatActivity).supportActionBar?.setTitle(R.string.explore)

        binding.recyclerViewExplore.layoutManager = LinearLayoutManager(requireContext())
        binding.recyclerViewExplore.setHasFixedSize(true)

        binding.fabExplore.setOnClickListener {
            Navigation.findNavController(it).navigate(R.id.action_exploreFragment_to_exploreCreateFragment)
        }
        categories.add(getString(R.string.all))
        categories.add(getString(R.string.history))
        categories.add(getString(R.string.transportationAndAccommodation))
        categories.add(getString(R.string.food))
        categories.add(getString(R.string.natureAndAdvanture))
        categories.add(getString(R.string.Entertainment))
        categories.add(getString(R.string.ExpreinceAndSuggestion))

        categoryAdapter = ArrayAdapter(requireContext(),android.R.layout.simple_list_item_1,android.R.id.text1,categories)
        binding.spinnerExplore.adapter = categoryAdapter

        binding.spinnerExplore.onItemSelectedListener = object  : AdapterView.OnItemSelectedListener{
            override fun onItemSelected(p0: AdapterView<*>?, p1: View?, position: Int, p3: Long) {
                var category = categories[position]
                selectedCategory = category
                //viewModelExplore.getExploreForCategoryViewModel(category)
                //selectedCategory == getString(R.string.all)
                if(position == 0 || selectedCategory == ""){
                    viewModelExplore.getExploreViewModel()
                    /*viewModelExplore.exploreListLive.observe(viewLifecycleOwner){
                        val adapter = ExploreAdapter(requireContext(),it,viewModelExplore)
                        binding.recyclerViewExplore.adapter = adapter
                        binding.progressBarExplore.visibility = View.GONE
                        println("if calisti")
                    }*/
                }else{
                    viewModelExplore.getExploreForCategoryViewModel(category)
                    /*viewModelExplore.categoryExploreListLive.observe(viewLifecycleOwner) {
                        val adapter = ExploreAdapter(requireContext(), it, viewModelExplore)
                        binding.recyclerViewExplore.adapter = adapter
                        binding.progressBarExplore.visibility = View.GONE
                        println("else calisti")
                    }*/
                }

            }

            override fun onNothingSelected(p0: AdapterView<*>?) {

            }

        }

        viewModelExplore.exploreListLive.observe(viewLifecycleOwner) {
            val adapter = ExploreAdapter(requireContext(), it, viewModelExplore)
            binding.recyclerViewExplore.adapter = adapter
            binding.progressBarExplore.visibility = View.GONE
        }

        requireActivity().addMenuProvider(object : MenuProvider{
            override fun onCreateMenu(menu: Menu, menuInflater: MenuInflater) {
                menuInflater.inflate(R.menu.toolbar_search_menu,menu)

                val item = menu.findItem(R.id.actionSearch)
                val searchView = item.actionView as SearchView
                searchView.setOnQueryTextListener(this@ExploreFragment)
            }

            override fun onMenuItemSelected(menuItem: MenuItem): Boolean {
                return false
            }

        },viewLifecycleOwner,Lifecycle.State.RESUMED)




        /*viewModelExplore.exploreListLive.observe(viewLifecycleOwner){
            val adapter = ExploreAdapter(requireContext(),it,viewModelExplore)
            binding.recyclerViewExplore.adapter = adapter
            binding.progressBarExplore.visibility = View.GONE
        }*/


    }

    override fun onQueryTextSubmit(query: String): Boolean {
        viewModelExplore.searchViewModel(query)
        return true
    }

    override fun onQueryTextChange(newText: String): Boolean {
        viewModelExplore.searchViewModel(newText)
        return true
    }

    /*override fun onResume() {
        super.onResume()
        viewModelExplore.getExploreViewModel()
    }*/
}