package com.example.tubes1pbd.ui.menu

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.tubes1pbd.databinding.FragmentMenuBinding

class MenuFragment : Fragment() {
    private var _binding: FragmentMenuBinding? = null
    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!
    private val adapter = MenuAdapter()
    private lateinit var menuViewModel: MenuViewModel

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentMenuBinding.inflate(inflater, container, false)
        binding.rvMenu.adapter = adapter
        binding.rvMenu.layoutManager = LinearLayoutManager(context)
        menuViewModel =
            ViewModelProvider(this)[MenuViewModel::class.java]
        menuViewModel.rvMenu.observe(viewLifecycleOwner){
            adapter.menuList = it.toMutableList()
        }
        menuViewModel.getAPIMenu()
        return binding.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}