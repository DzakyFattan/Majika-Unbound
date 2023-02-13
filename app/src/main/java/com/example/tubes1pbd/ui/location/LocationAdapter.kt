package com.example.tubes1pbd.ui.location

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.tubes1pbd.databinding.CardviewLocationBinding


class LocationAdapter(private val location_item: ArrayList<Locations>) :
    RecyclerView.Adapter<LocationAdapter.Holder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): Holder {
        val itemBinding =
            CardviewLocationBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return Holder(itemBinding)
    }

    override fun onBindViewHolder(holder: Holder, position: Int) {
        val location = location_item[position]
        holder.bind(location)
    }

    override fun getItemCount(): Int {
        return location_item.size
    }

    inner class Holder(private val itemBinding: CardviewLocationBinding) :
        RecyclerView.ViewHolder(itemBinding.root) {
        fun bind(location: Locations) {
            itemBinding.apply {
                tvLocationName.text = location.name
            }
        }
    }
}