package com.example.tubes1pbd.ui.keranjang

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.example.tubes1pbd.data.MajikaRoomDatabase
import com.example.tubes1pbd.models.Cart
import com.example.tubes1pbd.models.DecodeResponse
import com.example.tubes1pbd.models.Menu
import com.example.tubes1pbd.models.MenuResponse
import com.example.tubes1pbd.repository.MajikaRepository
import com.example.tubes1pbd.service.RestApiBuilder.getClient
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class CartViewModel(private val database: MajikaRoomDatabase) : ViewModel(), CartAdapter.OnButtonClickListener {
    val repository = MajikaRepository(database)
    val status = MutableLiveData<String>()
    private lateinit var cartList: List<Cart>
    val menuList = MutableLiveData<List<Menu>>()
    private var menus: MenuResponse? = null
    private lateinit var menuResponse: Call<MenuResponse>

    fun getMenu(){
        menuResponse = getClient().getMenu()
        menuResponse.enqueue(object: Callback<MenuResponse> {
            override fun onResponse(
                call: Call<MenuResponse>,
                response: Response<MenuResponse>
            ){
                menus = response.body()
                menuList.postValue(menus?.data)
            }
            override fun onFailure(call: Call<MenuResponse>, t: Throwable) {
                Log.d("menu", t.toString())
            }
        })
    }

    fun checkMenuAndCart(menuList: List<Menu>){
        cartList = repository.cartList.value?.toList()!!
        cartList.forEach{cart ->
            if(menuList.none{it.name == cart.name && it.price == cart.price}){
                viewModelScope.launch (Dispatchers.IO){
                    database.cartDao.deleteCartItem(cart.name, cart.price)
                }
            }
        }
    }

    fun calculateTotalPrice(cartList: List<Cart>) : Long{
        return cartList.map{it.price.toLong() * it.quantity}.reduceOrNull{acc, price -> acc + price} ?: 0L
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
            if (modelClass.isAssignableFrom(CartViewModel::class.java)) {
                @Suppress("UNCHECKED_CAST")
                return CartViewModel(database) as T
            }
            throw IllegalArgumentException("Unable to construct viewmodel")
        }
    }
}
