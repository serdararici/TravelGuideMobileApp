package com.serdararici.travelguide.View

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
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

        binding.fabExplore.setOnClickListener {
            Navigation.findNavController(it).navigate(R.id.action_exploreFragment_to_exploreCreateFragment)
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

        binding.recyclerViewExplore.layoutManager = LinearLayoutManager(requireContext())
        binding.recyclerViewExplore.setHasFixedSize(true)

        val exploreList = ArrayList<Explore>()
        val e1 = Explore("1","başlık1", "detay1 kkkkkkkkkkkkkkkkkkkkkkkkkkkkkkkkkkkkkkkkkkkkkkkkkkkkkkkkkkkkkkkkkkkkkkkkkkkkkkkkkkkkkkkkkkkkkkkkkkkkkkkkkkkkkkkkkkkkkkkkkkkkkkkkkkkkkkkkkkkkkkkkkkkkkkkkkkkkkkkkkkkkkkkkkkkkkkkkkkkkkkkkkkkkkkkkkkkkkkkkkkkkkkkkkkkkkkkkkkkkkkkkkkkkkkkkkkkkkkkkkkkkkkkkkkkkkkkkkkkkkkkkkkkkkkkkkkkkkkkkkkkkkkkkkkkkkkkkkkkkkkkkkkkkkkkkkkkkkkkkkkkkkkkkkkkkkkkkkkkkkkkkkkkkkkkkkkkkkkkkkkkkkkkkkkkkkkkkkkkkkkkkkkkkkkkkkkkkkkkkkkkkkkkkkkkkkkkkkkkkkkkkkkkkkkkkkkkkkkkkkkkkkkkkkkkkkkkkkkkkkkkkkkkkkkkkkkkkkkkkkkkkkkkkkkkkkkkkkkkkkkkkkkkkkkkkkkkkkkkkkkkkkkkkkkkkkkkkkkkkkkkkkkkkkkkkkkkkkkkkkkkkkkkk", "3.5", "Sakarya-Turkey")
        val e2 = Explore("2","başlık2", "detay2", "4.5", "Istanbul-Turkey")
        val e3 = Explore("3","başlık3", "detay3", "2.0", "Ankara-Turkey")
        val e4 = Explore("4","başlık4", "detay4", "5.0", "İzmir-Turkey")
        exploreList.add(e1)
        exploreList.add(e2)
        exploreList.add(e3)
        exploreList.add(e4)

        val adapter = ExploreAdapter(requireContext(),exploreList)
        binding.recyclerViewExplore.adapter = adapter
    }

    override fun onQueryTextSubmit(query: String): Boolean {
        search(query)
        return true
    }

    override fun onQueryTextChange(newText: String): Boolean {
        search(newText)
        return true
    }

    fun search(searchingWord:String){

    }

    override fun onResume() {
        super.onResume()
    }
}