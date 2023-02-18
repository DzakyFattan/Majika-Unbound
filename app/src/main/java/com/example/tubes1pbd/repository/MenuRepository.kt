package com.example.tubes1pbd.repository

import androidx.lifecycle.LiveData
import androidx.lifecycle.Transformations
import com.example.tubes1pbd.database.AppDatabase
import com.example.tubes1pbd.database.asDomainModel
import com.example.tubes1pbd.models.Menu

class MenuRepository (private val database: AppDatabase){
    val menu: LiveData<List<Menu>> = Transformations.map(database.menuDao().getMenu()){
        it.asDomainModel()
    }
}