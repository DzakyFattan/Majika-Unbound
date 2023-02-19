package com.example.tubes1pbd.ui.menu

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.constraintlayout.widget.ConstraintSet.VISIBLE
import androidx.recyclerview.widget.RecyclerView
import com.example.tubes1pbd.data.CartDao
import com.example.tubes1pbd.data.MajikaRoomDatabase
import com.example.tubes1pbd.databinding.FoodDrinkListItemBinding
import com.example.tubes1pbd.models.Cart
import com.example.tubes1pbd.models.Menu


class MenuAdapter(private val cartDao: CartDao) :
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
        val cart : Cart? = cartList.find{it.name == menu.name}
        holder.binding.name.text = menu.name
        holder.binding.description.text = menu.description
        holder.binding.sold.text = "Sold: ${menu.sold.toString()}"
        holder.binding.price.text = menu.price.toString()
        cart?.let {
            holder.binding.layoutButton.visibility = View.VISIBLE
            holder.binding.addButton.visibility = View.GONE
            holder.binding.quantity.text = cart.quantity.toString()
        } ?: run {
            holder.binding.layoutButton.visibility = View.GONE
            holder.binding.addButton.visibility = View.VISIBLE
        }
            holder.binding.addButton.setOnClickListener{
                cartDao.insertCartItem(menu.name, menu.price, 1)
            }
            holder.binding.decreaseButton.setOnClickListener{
                cart?.let {
                    if (cart.quantity == 1){
                        cartDao.deleteCartItem(menu.name)
                    }
                    else{
                        cartDao.updateCartItem(menu.name, cart.quantity - 1)
                    }
                }
            }
            holder.binding.increaseButton.setOnClickListener{
                cart?.let {
                    cartDao.updateCartItem(menu.name, cart.quantity + 1)
                }
            }
    }

    override fun getItemCount() = _menuList.size

}