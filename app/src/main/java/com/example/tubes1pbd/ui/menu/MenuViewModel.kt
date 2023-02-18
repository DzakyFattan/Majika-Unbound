package com.example.tubes1pbd.ui.menu

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.tubes1pbd.models.Menu
import com.example.tubes1pbd.models.MenuList
import com.example.tubes1pbd.service.RestApiBuilder.getClient
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class MenuViewModel : ViewModel() {

    private val menuItem = arrayListOf<Menu>();
    private val _rvMenu = MutableLiveData<List<Menu>>()
    val rvMenu: LiveData<List<Menu>>
        get() = _rvMenu
    private val api = getClient()
    private lateinit var response : Call<MenuList>

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
                    menuItem.addAll(sortedMenus)
                    _rvMenu.postValue(menuItem)
                }
            }

            override fun onFailure(call: Call<MenuList>, t: Throwable) {
                Log.d("menu", t.toString())
            }
        })
    }

    fun filter(query: String){
        _rvMenu.postValue(menuItem.filter { it.name!!.contains(query, ignoreCase = true) })
    }

}