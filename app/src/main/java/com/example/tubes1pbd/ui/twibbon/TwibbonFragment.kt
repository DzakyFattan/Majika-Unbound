package com.example.tubes1pbd.ui.twibbon

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.example.tubes1pbd.databinding.FragmentTwibbonBinding

class TwibbonFragment : Fragment() {

    private var _binding: FragmentTwibbonBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val homeViewModel =
            ViewModelProvider(this).get(TwibbonViewModel::class.java)

        _binding = FragmentTwibbonBinding.inflate(inflater, container, false)
        val root: View = binding.root

        val textView: TextView = binding.textTwibbon
        homeViewModel.text.observe(viewLifecycleOwner) {
            textView.text = it
        }
        return root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}