package com.serdararici.travelguide.Adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.appcompat.widget.PopupMenu
import androidx.navigation.Navigation
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.snackbar.Snackbar
import com.serdararici.travelguide.Model.Explore
import com.serdararici.travelguide.R
import com.serdararici.travelguide.View.ProfileDetailsFragmentDirections
import com.serdararici.travelguide.View.ProfileFragmentDirections
import com.serdararici.travelguide.ViewModel.ProfileViewModel
import com.serdararici.travelguide.databinding.ProfileDetailsRecyclerRowBinding
import com.serdararici.travelguide.databinding.ProfileRecyclerRowBinding
import com.squareup.picasso.Picasso

class ProfileDetailsAdapter (var context: Context, var exploreList:List<Explore>, var viewModel: ProfileViewModel)
    : RecyclerView.Adapter<ProfileDetailsAdapter.ProfileDetailsViewHolder>(){
    inner class ProfileDetailsViewHolder(binding: ProfileDetailsRecyclerRowBinding) : RecyclerView.ViewHolder(binding.root){
        var binding: ProfileDetailsRecyclerRowBinding
        init {
            this.binding = binding
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ProfileDetailsViewHolder {
        val layoutInflater = LayoutInflater.from(context)
        val binding = ProfileDetailsRecyclerRowBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ProfileDetailsViewHolder(binding)
    }

    override fun getItemCount(): Int {
        return exploreList.size
    }

    override fun onBindViewHolder(holder: ProfileDetailsViewHolder, position: Int) {
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