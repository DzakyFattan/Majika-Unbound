package com.example.tubes1pbd.ui.menu

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.example.tubes1pbd.data.MajikaRoomDatabase
import com.example.tubes1pbd.models.Menu
import com.example.tubes1pbd.models.MenuResponse
import com.example.tubes1pbd.repository.MajikaRepository
import com.example.tubes1pbd.service.RestApiBuilder.getClient
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class MenuViewModel(private val database: MajikaRoomDatabase) : ViewModel(), MenuAdapter.OnButtonClickListener {
    private var menuItem = arrayListOf<Menu>()
    private var currQuery = ""
    private lateinit var response : Call<MenuResponse>
    val repository = MajikaRepository(database)
    val rvMenu = MutableLiveData<List<Menu>>()

    fun getMenu(){
        response = getClient().getMenu()
        response.enqueue(object: Callback<MenuResponse> {
            override fun onResponse(
                call: Call<MenuResponse>,
                response: Response<MenuResponse>
            ){
                val menus = response.body()
                val sortedMenus = menus?.data?.sortedBy { it.name }
                if (sortedMenus != null){
                    menuItem.clear()
                    menuItem.addAll(sortedMenus)
                }
                filter(currQuery)
            }
            override fun onFailure(call: Call<MenuResponse>, t: Throwable) {
                Log.d("menu", t.toString())
            }
        })
    }

    fun filter(query: String){
        currQuery = query
        rvMenu.postValue(menuItem.filter { it.name!!.contains(query, ignoreCase = true) })
    }

    override fun onAddButtonClicked(name: String, price: Int) {
        viewModelScope.launch(Dispatchers.IO) {
            database.cartDao.insertCartItem(name, price, 1)
        }
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
            if (modelClass.isAssignableFrom(MenuViewModel::class.java)) {
                @Suppress("UNCHECKED_CAST")
                return MenuViewModel(database) as T
            }
            throw IllegalArgumentException("Unable to construct viewmodel")
        }
    }



}
