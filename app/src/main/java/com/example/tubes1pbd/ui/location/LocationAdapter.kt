package com.example.tubes1pbd.ui.location

import android.content.Intent
import android.net.Uri
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.content.ContextCompat.startActivity
import androidx.recyclerview.widget.RecyclerView
import com.example.tubes1pbd.databinding.CardviewLocationBinding
import com.example.tubes1pbd.models.Locations


class LocationAdapter : RecyclerView.Adapter<LocationAdapter.Holder>() {

    var locations = mutableListOf<Locations>()
    fun setLocationsList(locations: List<Locations>) {
        this.locations.clear()
        this.locations = locations.toMutableList()
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): Holder {
        val itemBinding =
            CardviewLocationBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return Holder(itemBinding)
    }

    override fun onBindViewHolder(holder: Holder, position: Int) {
        val location = locations[position]
        holder.bind(location)
    }

    override fun getItemCount(): Int {
        return locations.size
    }

    inner class Holder(private val itemBinding: CardviewLocationBinding) :
        RecyclerView.ViewHolder(itemBinding.root) {
        fun bind(location: Locations) {
            itemBinding.apply {
                tvLocationName.text = location.name
                tvLocationAddress.text = location.address
                tvLocationPhone.text = location.phone_number
            }
        }
        init {
            itemBinding.btnMaps.setOnClickListener {
                val gmmIntentUri = Uri.parse("geo:0,0?q=${itemBinding.tvLocationAddress.text}")
                val mapIntent = Intent(Intent.ACTION_VIEW, gmmIntentUri)
                mapIntent.setPackage("com.google.android.apps.maps")
                startActivity(itemBinding.root.context, mapIntent, null)
            }
        }
    }
}