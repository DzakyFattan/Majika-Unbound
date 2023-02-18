package com.example.tubes1pbd.database

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Query

@Dao
interface MenuDao {
    @Query("SELECT * FROM item_cart")
    fun getMenu(): LiveData<List<MenuEntity>>
}