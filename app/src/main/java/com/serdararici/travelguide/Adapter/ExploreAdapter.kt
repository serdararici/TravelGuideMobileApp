package com.serdararici.travelguide.Adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.navigation.Navigation
import androidx.recyclerview.widget.RecyclerView
import com.serdararici.travelguide.Model.Explore
import com.serdararici.travelguide.View.ExploreFragmentDirections
import com.serdararici.travelguide.databinding.ExploreRecyclerRowBinding

class ExploreAdapter (var mContext: Context,
                      var exploreList: List<Explore>)
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
        holder.binding.tvExplorePlaceName.text = explore.explorePlaceName

        holder.binding.exploreCardView.setOnClickListener {
            val action = ExploreFragmentDirections.actionExploreFragmentToExploreDetailsFragment(explore)
            Navigation.findNavController(it).navigate(action)
        }
    }
}