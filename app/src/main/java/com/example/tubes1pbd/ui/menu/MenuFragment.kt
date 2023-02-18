package com.example.tubes1pbd.ui.menu

import android.content.Context.SENSOR_SERVICE
import android.hardware.Sensor
import android.hardware.SensorEvent
import android.hardware.SensorEventListener
import android.hardware.SensorManager
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.widget.SearchView
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.tubes1pbd.databinding.FragmentMenuBinding

class MenuFragment : Fragment(), SensorEventListener {
    private var _binding: FragmentMenuBinding? = null
    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!
    private val menuViewModel: MenuViewModel by lazy {
        ViewModelProvider(requireActivity())[MenuViewModel::class.java]
    }
    private val adapter = MenuAdapter()
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
        binding.rvMenu.adapter = adapter
        binding.rvMenu.layoutManager = LinearLayoutManager(context)
        sensorManager = activity?.getSystemService(SENSOR_SERVICE) as SensorManager
        tempSensor = sensorManager.getDefaultSensor(Sensor.TYPE_AMBIENT_TEMPERATURE)
        tempSensor ?: run{
            Toast.makeText(context, "Temperature sensor is not available", Toast.LENGTH_LONG).show()
            binding.temperature?.text = "No Temperature Sensor"
        }
        binding.search?.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                return true
            }
            override fun onQueryTextChange(newText: String?): Boolean {
                menuViewModel.filter(newText!!)
                return true
            }
        })
        menuViewModel.rvMenu.observe(viewLifecycleOwner){
            adapter.menuList = it.toMutableList()
        }
        menuViewModel.getMenu()
    }
    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    override fun onSensorChanged(event: SensorEvent?) {
        binding.temperature?.text = "${event?.values?.get(0)} °C"
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