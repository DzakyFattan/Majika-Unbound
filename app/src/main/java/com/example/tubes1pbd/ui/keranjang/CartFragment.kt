package com.example.tubes1pbd.ui.keranjang

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.tubes1pbd.data.MajikaRoomDatabase
import com.example.tubes1pbd.databinding.FragmentCartBinding
import com.example.tubes1pbd.ui.payment.PaymentActivity
import com.google.mlkit.vision.codescanner.GmsBarcodeScanning
import java.text.DecimalFormat

class CartFragment : Fragment() {

    private var _binding: FragmentCartBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!
    private lateinit var database : MajikaRoomDatabase
    private lateinit var adapter: CartAdapter
    private val cartViewModel: CartViewModel by lazy {
        ViewModelProvider(this, CartViewModel.Factory(database))[CartViewModel::class.java]
    }


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentCartBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        database = MajikaRoomDatabase.getDatabase(activity?.application!!)
        adapter = CartAdapter(cartViewModel)
        binding.rvCart.adapter = adapter
        binding.rvCart.layoutManager = LinearLayoutManager(context)
        cartViewModel.repository.cartList.observe(viewLifecycleOwner){
            adapter.cartList = it.toMutableList()
            val price: Long = cartViewModel.calculateTotalPrice(it)
            if (price == 0L){
                binding.totalPrice.text = "Rp. 0"
                binding.addButton2.isEnabled = false
            } else {
                binding.totalPrice.text = "Rp. ${DecimalFormat("#,###").format(price).replace(',', '.')}"
                binding.addButton2.isEnabled = true
            }
        }
        binding.addButton2.setOnClickListener {
            Intent(context, PaymentActivity::class.java)
                .putExtra("price", binding.totalPrice.text)
                .also {
                    startActivity(it)
                }
//            scanner.startScan()
//                .addOnSuccessListener {
//                    cartViewModel.sendQRDecode(it.rawValue.toString())
//                }
//                .addOnFailureListener {
//                    Toast.makeText(context, "Error: ${it.message}", Toast.LENGTH_SHORT).show()
//                }
        }
        cartViewModel.status.observe(viewLifecycleOwner){
            Toast.makeText(context, it.toString(), Toast.LENGTH_SHORT).show()
        }
        cartViewModel.getMenu()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}