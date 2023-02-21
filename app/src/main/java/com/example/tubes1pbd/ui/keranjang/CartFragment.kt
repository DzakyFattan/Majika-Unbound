package com.example.tubes1pbd.ui.keranjang

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
import com.google.mlkit.vision.codescanner.GmsBarcodeScanning

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
            binding.totalPrice.text = cartViewModel.calculateTotalPrice(it).toString()
        }
        val scanner = GmsBarcodeScanning.getClient(requireContext())
        binding.addButton2.setOnClickListener {
            scanner.startScan()
                .addOnSuccessListener {
                    cartViewModel.sendQRDecode(it.rawValue.toString())
                }
                .addOnFailureListener {
                    Toast.makeText(context, "Error: ${it.message}", Toast.LENGTH_SHORT).show()
                }
        }
        cartViewModel.status.observe(viewLifecycleOwner){
            Toast.makeText(context, it.toString(), Toast.LENGTH_SHORT).show()
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}