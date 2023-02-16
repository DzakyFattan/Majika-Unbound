package com.example.tubes1pbd.ui.menu

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.tubes1pbd.databinding.CardviewFoodBinding
import com.example.tubes1pbd.models.Menu


class MenuAdapter() :
    RecyclerView.Adapter<MenuAdapter.MenuViewHolder>() {
    private var _menuList = mutableListOf<Menu>()

    var menuList
        get() = _menuList
        set(value){
            _menuList = value
            notifyDataSetChanged()
        }
    /**
     * Provide a reference to the type of views that you are using
     * (custom ViewHolder)
     */

    inner class MenuViewHolder(val binding: CardviewFoodBinding) : RecyclerView.ViewHolder(binding.root)

    // Create new views (invoked by the layout manager)
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MenuViewHolder {
        // Create a new view, which defines the UI of the list item
        val binding = CardviewFoodBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return MenuViewHolder(binding)
    }

    // Replace the contents of a view (invoked by the layout manager)
    override fun onBindViewHolder(viewHolder: MenuViewHolder, position: Int) {
        val menu = _menuList[position]
        viewHolder.binding.name.text = menu.name
        viewHolder.binding.description.text = menu.description
        viewHolder.binding.sold.text = menu.sold
        viewHolder.binding.price.text = menu.price
    }

    // Return the size of your dataset (invoked by the layout manager)
    override fun getItemCount() = _menuList.size

}