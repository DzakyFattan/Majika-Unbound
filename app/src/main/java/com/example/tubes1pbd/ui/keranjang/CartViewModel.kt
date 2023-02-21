package com.example.tubes1pbd.ui.keranjang

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.example.tubes1pbd.data.MajikaRoomDatabase
import com.example.tubes1pbd.models.Cart
import com.example.tubes1pbd.repository.MajikaRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class CartViewModel(private val database: MajikaRoomDatabase) : ViewModel(), CartAdapter.OnButtonClickListener {
    val repository = MajikaRepository(database)

    fun calculateTotalPrice(cartList: List<Cart>) : Long{
        return cartList.map{it.price.toLong() * it.quantity}.reduceOrNull{acc, price -> acc + price} ?: 0L
    }

    override fun onIncreaseButtonClicked(name: String, price: Int, quantity: Int) {
        viewModelScope.launch(Dispatchers.IO) {
            database.cartDao.updateCartItem(name, price, quantity + 1)
        }
    }

    override fun onDecreaseButtonclicked(name: String, price: Int, quantity: Int) {
        viewModelScope.launch(Dispatchers.IO) {
            if (quantity == 1) database.cartDao.deleteCartItem(name, price) else database.cartDao.updateCartItem(name, price, quantity - 1)
        }
    }

    class Factory(private val database: MajikaRoomDatabase) : ViewModelProvider.Factory {
        override fun <T : ViewModel> create(modelClass: Class<T>): T {
            if (modelClass.isAssignableFrom(CartViewModel::class.java)) {
                @Suppress("UNCHECKED_CAST")
                return CartViewModel(database) as T
            }
            throw IllegalArgumentException("Unable to construct viewmodel")
        }
    }
}
