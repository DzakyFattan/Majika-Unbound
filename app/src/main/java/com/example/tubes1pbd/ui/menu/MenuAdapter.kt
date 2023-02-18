package com.example.tubes1pbd.ui.menu

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.tubes1pbd.databinding.FoodDrinkListItemBinding
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

    class MenuViewHolder(val binding: FoodDrinkListItemBinding) : RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MenuViewHolder {
        val binding = FoodDrinkListItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return MenuViewHolder(binding)
    }

    override fun onBindViewHolder(holder: MenuViewHolder, position: Int) {
        val menu = _menuList[position]
        holder.binding.name.text = menu.name
        holder.binding.description.text = menu.description
        holder.binding.sold.text = menu.sold
        holder.binding.price.text = menu.price
    }

    override fun getItemCount() = _menuList.size

}