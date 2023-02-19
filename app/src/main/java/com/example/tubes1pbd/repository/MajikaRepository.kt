package com.example.tubes1pbd.repository

import androidx.lifecycle.LiveData
import androidx.lifecycle.Transformations
import com.example.tubes1pbd.data.MajikaRoomDatabase
import com.example.tubes1pbd.data.asDomainModel
import com.example.tubes1pbd.models.Cart

class MajikaRepository(private val database: MajikaRoomDatabase) {
    val cartList: LiveData<List<Cart>> = Transformations.map(database.cartDao.getCartItem()){
        it.asDomainModel()
    }
}