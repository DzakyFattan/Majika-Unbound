package com.example.tubes1pbd.ui.keranjang

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.tubes1pbd.data.CartDao
import com.example.tubes1pbd.databinding.CartListItemBinding
import com.example.tubes1pbd.models.Cart


class CartAdapter(private val listener: OnButtonClickListener) :
    RecyclerView.Adapter<CartAdapter.CartViewHolder>() {
    private var _cartList = mutableListOf<Cart>()
    var cartList
        get() = _cartList
        set(value){
            _cartList = value
            notifyDataSetChanged()
        }
    class CartViewHolder(val binding: CartListItemBinding) : RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CartViewHolder {
        val binding = CartListItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return CartViewHolder(binding)
    }

    override fun onBindViewHolder(holder: CartViewHolder, position: Int) {
        val cart = _cartList[position]
        holder.binding.name.text = cart.name
        holder.binding.price.text = cart.price.toString()
        holder.binding.quantity.text = cart.quantity.toString()
        holder.binding.increaseButton.setOnClickListener{
            listener.onIncreaseButtonClicked(cart.name, cart.price, cart.quantity)
        }
        holder.binding.decreaseButton.setOnClickListener {
            listener.onDecreaseButtonclicked(cart.name, cart.price, cart.quantity)
        }
    }

    override fun getItemCount() = _cartList.size

    interface OnButtonClickListener{
        fun onIncreaseButtonClicked(name: String, price: Int, quantity: Int)

        fun onDecreaseButtonclicked(name: String, price: Int, quantity: Int)
    }
}