package com.example.tubes1pbd.ui.menu

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.tubes1pbd.databinding.FoodDrinkListItemBinding
import com.example.tubes1pbd.models.Cart
import com.example.tubes1pbd.models.Menu
import java.text.DecimalFormat


class MenuAdapter(private val listener: OnButtonClickListener) :
    RecyclerView.Adapter<MenuAdapter.MenuViewHolder>() {
    private var _menuList = mutableListOf<Menu>()
    private var _cartList = mutableListOf<Cart>()
    var cartList
        get() = _cartList
        set(value){
            _cartList = value
            notifyDataSetChanged()
        }
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
        val cart : Cart? = cartList.find{it.name == menu.name && it.price == menu.price}
        holder.binding.name.text = menu.name
        holder.binding.description.text = menu.description

        holder.binding.sold.text = "Sold: ${DecimalFormat("#,###").format(menu.sold).replace(',', '.')}"
        holder.binding.price.text = "Rp. ${DecimalFormat("#,###").format(menu.price).replace(',', '.')}"
        cart?.let {
            holder.binding.layoutButton.visibility = View.VISIBLE
            holder.binding.addButton.visibility = View.GONE
            holder.binding.quantity.text = DecimalFormat("#,###").format(cart.quantity).replace(',', '.')
        } ?: run {
            holder.binding.layoutButton.visibility = View.GONE
            holder.binding.addButton.visibility = View.VISIBLE
        }
        holder.binding.addButton.setOnClickListener{
            listener.onAddButtonClicked(menu.name!!, menu.price!!)
        }
        holder.binding.increaseButton.setOnClickListener{
            listener.onIncreaseButtonClicked(menu.name!!, menu.price!!, cart!!.quantity)
        }
        holder.binding.decreaseButton.setOnClickListener {
            listener.onDecreaseButtonclicked(menu.name!!, menu.price!!, cart!!.quantity)
        }
    }

    override fun getItemCount() = _menuList.size

    interface OnButtonClickListener{
        fun onAddButtonClicked(name: String, price: Int)
        fun onIncreaseButtonClicked(name: String, price: Int, quantity: Int)

        fun onDecreaseButtonclicked(name: String, price: Int, quantity: Int)
    }
}