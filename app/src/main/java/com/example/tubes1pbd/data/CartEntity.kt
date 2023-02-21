package com.example.tubes1pbd.data

import androidx.room.Entity
import androidx.room.ColumnInfo
import androidx.room.PrimaryKey
import com.example.tubes1pbd.models.Cart


@Entity(tableName = "cart", primaryKeys = ["name", "price"])
data class CartEntity constructor(
    val name: String,
    val price: Int,
    val quantity: Int
)
fun  List<CartEntity>.asDomainModel(): List<Cart>{
    return map {
        Cart(
            name = it.name,
            price = it.price,
            quantity = it.quantity
        )
    }
}

