package com.example.tubes1pbd.ui.keranjang

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.example.tubes1pbd.data.MajikaRoomDatabase
import com.example.tubes1pbd.models.Cart
import com.example.tubes1pbd.models.DecodeResponse
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
    private var decode: DecodeResponse? = null
    private lateinit var response : Call<DecodeResponse>

    fun sendQRDecode(qrdecode : String){
        response = getClient().sendQRDecode(qrdecode)
        response.enqueue(object: Callback<DecodeResponse> {
            override fun onResponse(
                call: Call<DecodeResponse>,
                response: Response<DecodeResponse>
            ){
                decode = response.body()
                status.postValue(decode!!.status)
            }
            override fun onFailure(call: Call<DecodeResponse>, t: Throwable) {
                Log.d("menu", t.toString())
            }
        })
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
