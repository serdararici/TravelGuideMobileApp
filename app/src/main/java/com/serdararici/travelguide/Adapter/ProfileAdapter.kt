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
import com.serdararici.travelguide.View.ExploreFragmentDirections
import com.serdararici.travelguide.View.ProfileFragmentDirections
import com.serdararici.travelguide.ViewModel.ExploreViewModel
import com.serdararici.travelguide.ViewModel.ProfileEditViewModel
import com.serdararici.travelguide.ViewModel.ProfileViewModel
import com.serdararici.travelguide.databinding.ProfileRecyclerRowBinding
import com.squareup.picasso.Picasso

class ProfileAdapter(var context: Context,var exploreList:List<Explore>,var viewModel: ProfileViewModel, var viewModelProfileEdit: ProfileEditViewModel)
    : RecyclerView.Adapter<ProfileAdapter.ProfileViewHolder>(){
    inner class ProfileViewHolder(binding:ProfileRecyclerRowBinding) : RecyclerView.ViewHolder(binding.root){
        var binding:ProfileRecyclerRowBinding
        init {
            this.binding = binding
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ProfileViewHolder {
        val layoutInflater = LayoutInflater.from(context)
        val binding = ProfileRecyclerRowBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ProfileViewHolder(binding)
    }

    override fun getItemCount(): Int {
        return exploreList.size
    }

    override fun onBindViewHolder(holder: ProfileViewHolder, position: Int) {

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
            var action = ProfileFragmentDirections.actionProfileFragmentToExploreDetailsFragment(explore,explore)
            Navigation.findNavController(it).navigate(action)
        }

        holder.binding.ivExploreMore.setOnClickListener{
            val popup = PopupMenu(context, holder.binding.ivExploreMore)
            popup.menuInflater.inflate(R.menu.explore_popup_menu,popup.menu)
            popup.show()
            popup.setOnMenuItemClickListener {item ->
                when(item.itemId){
                    R.id.editExplore -> {
                        val action = ProfileFragmentDirections.actionProfileFragmentToExploreEditFragment(explore)
                        Navigation.findNavController(it).navigate(action)
                        true
                    }
                    R.id.deleteExplore -> {
                        Snackbar.make(it, "${explore.exploreTitle} ${context.getString(R.string.isDelete)}", Snackbar.LENGTH_LONG)
                            .setAction(R.string.yes){
                                viewModel.deleteExploreViewModel(explore.exploreId!!)
                            }.show()
                        true
                    }
                    else -> false
                }
            }
        }
    }
}