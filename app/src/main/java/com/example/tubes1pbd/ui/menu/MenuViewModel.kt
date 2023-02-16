package com.example.tubes1pbd.ui.menu

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.tubes1pbd.models.Menu
import com.example.tubes1pbd.models.MenuList
import com.example.tubes1pbd.service.RestAPIBuilder.getClient
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class MenuViewModel : ViewModel() {

    val menuItem = ArrayList<Menu>();
    val rvMenu = MutableLiveData<ArrayList<Menu>>();

    fun getAPIMenu(){
        val api = getClient()
        val response = api.getMenu()
        response.enqueue(object: Callback<MenuList> {
            override fun onResponse(
                call: Call<MenuList>,
                response: Response<MenuList>
            ){
                val menus = response.body()
                val sortedMenus = menus?.data?.sortedBy { it.name }
                Log.d("menu", sortedMenus.toString())
                if (sortedMenus != null){
                    menuItem.addAll(sortedMenus)
                    rvMenu.postValue(menuItem)
                }
            }

            override fun onFailure(call: Call<MenuList>, t: Throwable) {
                Log.d("menu", t.toString())
            }
        })
    }
}