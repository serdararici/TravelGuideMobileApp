package com.serdararici.travelguide.Adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.navigation.Navigation
import androidx.recyclerview.widget.RecyclerView
import com.serdararici.travelguide.Model.Explore
import com.serdararici.travelguide.View.ExploreFragmentDirections
import com.serdararici.travelguide.ViewModel.ExploreViewModel
import com.serdararici.travelguide.databinding.ExploreRecyclerRowBinding
import com.squareup.picasso.Picasso

class ExploreAdapter (var mContext: Context,
                      var exploreList: List<Explore>,
                      var viewModel: ExploreViewModel)
    : RecyclerView.Adapter<ExploreAdapter.ExploreViewHolder>(){
    inner class ExploreViewHolder(binding:ExploreRecyclerRowBinding) : RecyclerView.ViewHolder(binding.root){
        var binding:ExploreRecyclerRowBinding
        init {
            this.binding = binding
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ExploreViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val binding = ExploreRecyclerRowBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ExploreViewHolder(binding)
    }

    override fun getItemCount(): Int {
        return exploreList.size
    }

    override fun onBindViewHolder(holder: ExploreViewHolder, position: Int) {
        val explore = exploreList.get(position)
        holder.binding.tvExploreTitle.text = explore.exploreTitle
        holder.binding.tvExploreTxt.text = explore.exploreDetails
        var explorePlaceName = "${explore.explorePlace} - ${explore.exploreCountry}"
        holder.binding.tvExplorePlaceName.text = explorePlaceName
        holder.binding.ratingBar.rating = explore.exploreRatingNumber!!.toFloat()
        holder.binding.tvExploreUserName.text = explore.userEmail
        viewModel.getProfileFromExploreViewModel(explore.userEmail!!)
        holder.binding.tvExploreUserName.setOnClickListener {
            val action = ExploreFragmentDirections.actionExploreFragmentToProfileDetailsFragment(explore)
            Navigation.findNavController(it).navigate(action)
        }

        val imageView = holder.binding.iVExploreImage
        Picasso.get()
            .load(explore.exploreImageUri)
            .into(imageView)

        holder.binding.exploreCardView.setOnClickListener {
            val action = ExploreFragmentDirections.actionExploreFragmentToExploreDetailsFragment(explore,explore)
            Navigation.findNavController(it).navigate(action)
        }
    }
}