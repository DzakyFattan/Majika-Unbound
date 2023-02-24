package com.example.tubes1pbd.ui.menu

import android.content.Context.SENSOR_SERVICE
import android.hardware.Sensor
import android.hardware.SensorEvent
import android.hardware.SensorEventListener
import android.hardware.SensorManager
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.widget.SearchView
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.tubes1pbd.data.MajikaRoomDatabase
import com.example.tubes1pbd.databinding.FragmentMenuBinding

class MenuFragment : Fragment(), SensorEventListener {
    private var _binding: FragmentMenuBinding? = null
    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!
    private lateinit var database : MajikaRoomDatabase
    private val menuViewModel: MenuViewModel by lazy {
        ViewModelProvider(this, MenuViewModel.Factory(database))[MenuViewModel::class.java]
    }
    private lateinit var adapter: MenuAdapter
    private lateinit var sensorManager: SensorManager
    private var tempSensor : Sensor? = null

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentMenuBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        database = MajikaRoomDatabase.getDatabase(activity?.application!!)
        adapter = MenuAdapter(menuViewModel)
        binding.rvMenu.adapter = adapter
        binding.rvMenu.layoutManager = LinearLayoutManager(context)
        sensorManager = activity?.getSystemService(SENSOR_SERVICE) as SensorManager
        tempSensor = sensorManager.getDefaultSensor(Sensor.TYPE_AMBIENT_TEMPERATURE)
        tempSensor ?: run{
            Toast.makeText(context, "Temperature sensor is not available", Toast.LENGTH_LONG).show()
            binding.temperatureView.text = "No Temperature Sensor"
        }
        binding.searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                return true
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                menuViewModel.filter(newText!!)
                Log.d("1", binding.searchView.query.toString())
                return true
            }
        })
        binding.filterAll.setOnClickListener{
            menuViewModel.type = "All"
            menuViewModel.filter(menuViewModel.currQuery)
        }
        binding.filterFood.setOnClickListener{
            menuViewModel.type = "Food"
            menuViewModel.filter(menuViewModel.currQuery)
        }
        binding.filterDrink.setOnClickListener{
            menuViewModel.type = "Drink"
            menuViewModel.filter(menuViewModel.currQuery)
        }
        menuViewModel.rvMenu.observe(viewLifecycleOwner){
            adapter.menuList = it.toMutableList()
            menuViewModel.checkMenuAndCart(it)
        }
        menuViewModel.repository.cartList.observe(viewLifecycleOwner){
            val cartList = it.toMutableList()
            adapter.cartList = cartList
        }
        menuViewModel.getMenu()


    }
    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    override fun onSensorChanged(event: SensorEvent?) {
        binding.temperatureView.text = "${event?.values?.get(0)} °C"
    }

    override fun onResume() {
        super.onResume()
        sensorManager.registerListener(this, tempSensor, SensorManager.SENSOR_DELAY_NORMAL)
    }

    override fun onAccuracyChanged(sensor: Sensor?, accuracy: Int) {
        //Do Nothing
    }

    override fun onPause() {
        super.onPause()
        sensorManager.unregisterListener(this)
    }
}