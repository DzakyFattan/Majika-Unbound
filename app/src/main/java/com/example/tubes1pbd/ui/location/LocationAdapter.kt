package com.example.tubes1pbd.ui.location

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.tubes1pbd.R
import kotlinx.android.synthetic.main.cardview_location.view.*

class LocationAdapter(private val location_item: ArrayList<Locations>) :
    RecyclerView.Adapter<LocationAdapter.Holder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): Holder {
        val view =
            LayoutInflater.from(parent.context).inflate(R.layout.cardview_location, parent, false)
        return Holder(view)
    }

    override fun onBindViewHolder(holder: Holder, position: Int) {
        holder.itemView.location_name.text = location_item[position].name
    }

    override fun getItemCount(): Int {
        return location_item.size
    }

    inner class Holder(itemView: View) : RecyclerView.ViewHolder(itemView)
}