package com.example.tubes1pbd.data

import androidx.lifecycle.LiveData
import androidx.room.*
import com.example.tubes1pbd.models.Cart

@Dao
interface CartDao {

    @Query("SELECT * FROM cart")
    fun getCartItem(): LiveData<List<CartEntity>>

    @Query("INSERT INTO cart VALUES (:name, :price, :quantity)")
    fun insertCartItem(name: String?, price: Int?, quantity: Int?)

    @Query("DELETE FROM cart WHERE name = :name AND price = :price")
    fun deleteCartItem(name: String?, price: Int?)

    @Query("UPDATE cart SET quantity = :quantity WHERE name = :name AND price = :price")
    fun updateCartItem(name: String?, price: Int?, quantity: Int?)
}