package com.example.tubes1pbd.ui.menu

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.tubes1pbd.data.MajikaRoomDatabase
import com.example.tubes1pbd.models.Menu
import com.example.tubes1pbd.models.MenuList
import com.example.tubes1pbd.repository.MajikaRepository
import com.example.tubes1pbd.service.RestApiBuilder.getClient
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class MenuViewModel(database: MajikaRoomDatabase) : ViewModel() {
    private var menuItem = arrayListOf<Menu>()
    private var currQuery = ""
    private lateinit var response : Call<MenuList>
    private val api = getClient()
    val repository = MajikaRepository(database)
    val rvMenu = MutableLiveData<List<Menu>>()

    fun getMenu(){
        response = api.getMenu()
        response.enqueue(object: Callback<MenuList> {
            override fun onResponse(
                call: Call<MenuList>,
                response: Response<MenuList>
            ){
                val menus = response.body()
                val sortedMenus = menus?.data?.sortedBy { it.name }
                if (sortedMenus != null){
                    menuItem.clear()
                    menuItem.addAll(sortedMenus)
                }
                filter(currQuery)
            }
            override fun onFailure(call: Call<MenuList>, t: Throwable) {
                Log.d("menu", t.toString())
            }
        })
    }

    fun filter(query: String){
        currQuery = query
        rvMenu.postValue(menuItem.filter { it.name!!.contains(query, ignoreCase = true) })
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
