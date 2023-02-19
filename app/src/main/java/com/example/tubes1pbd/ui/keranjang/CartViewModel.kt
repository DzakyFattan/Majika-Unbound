package com.example.tubes1pbd.ui.keranjang

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.map
import com.example.tubes1pbd.data.MajikaRoomDatabase
import com.example.tubes1pbd.models.Cart
import com.example.tubes1pbd.repository.MajikaRepository
import com.example.tubes1pbd.ui.menu.MenuViewModel

class CartViewModel(database: MajikaRoomDatabase) : ViewModel() {
    val repository = MajikaRepository(database)

    fun calculateTotalPrice(cartList: List<Cart>) : Long{
        return cartList.map{it.price.toLong() * it.quantity}.reduceOrNull{acc, price -> acc + price} ?: 0L
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
