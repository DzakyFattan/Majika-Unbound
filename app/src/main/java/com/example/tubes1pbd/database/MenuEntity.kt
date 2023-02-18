package com.example.tubes1pbd.database

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.example.tubes1pbd.models.Menu

@Entity(tableName = "item_cart")
data class MenuEntity (
    @PrimaryKey val name: String,
    val description: String?,
    val currency: String?,
    val price: String?,
    val sold: String?,
    val type: String?
)

fun List<MenuEntity>.asDomainModel(): List<Menu>{
    return map {
        Menu(
            name = it.name,
            description = it.description,
            currency = it.currency,
            price = it.price,
            sold = it.sold,
            type = it.type
        )
    }
}
