package com.serdararici.travelguide.Adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.navigation.Navigation
import androidx.recyclerview.widget.RecyclerView
import com.serdararici.travelguide.Model.Explore
import com.serdararici.travelguide.View.ProfileDetailsFragmentDirections
import com.serdararici.travelguide.ViewModel.ProfileViewModel
import com.serdararici.travelguide.databinding.HomeExploreRecyclerRowBinding
import com.serdararici.travelguide.databinding.ProfileDetailsRecyclerRowBinding
import com.squareup.picasso.Picasso

class HomeExploreAdapter  (var context: Context, var exploreList:List<Explore>)
    : RecyclerView.Adapter<HomeExploreAdapter.HomeExploreViewHolder>(){
    inner class HomeExploreViewHolder(binding: HomeExploreRecyclerRowBinding) : RecyclerView.ViewHolder(binding.root){
        var binding: HomeExploreRecyclerRowBinding
        init {
            this.binding = binding
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): HomeExploreViewHolder {
        val layoutInflater = LayoutInflater.from(context)
        val binding = HomeExploreRecyclerRowBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return HomeExploreViewHolder(binding)
    }

    override fun getItemCount(): Int {
        return exploreList.size
    }

    override fun onBindViewHolder(holder: HomeExploreViewHolder, position: Int) {
        val explore = exploreList.get(position)
        holder.binding.tvExploreTitle.text = explore.exploreTitle
        holder.binding.tvExploreTxt.text = explore.exploreDetails
        var explorePlaceName = "${explore.explorePlace} - ${explore.exploreCountry}"
        holder.binding.tvExplorePlaceName.text = explorePlaceName
        holder.binding.ratingBar.rating = explore.exploreRatingNumber!!.toFloat()

        val imageView = holder.binding.iVExploreImage
        Picasso.get()
            .load(explore.exploreImageUri)
            .into(imageView)

        holder.binding.exploreCardView.setOnClickListener {
            var action = ProfileDetailsFragmentDirections.actionProfileDetailsFragmentToExploreDetailsFragment(explore,explore)
            Navigation.findNavController(it).navigate(action)
        }
    }
}