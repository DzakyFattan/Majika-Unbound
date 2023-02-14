package com.example.tubes1pbd.ui.twibbon

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.example.tubes1pbd.R
import com.example.tubes1pbd.databinding.FragmentTwibbonResultdisplayBinding

class ResultDisplayFragment : Fragment(R.layout.fragment_twibbon_resultdisplay) {
    private var _binding: FragmentTwibbonResultdisplayBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        super.onCreate(savedInstanceState)
        _binding = FragmentTwibbonResultdisplayBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.navButton.setOnClickListener {
            // navigate
            parentFragmentManager
                .beginTransaction()
                .replace(R.id.fragment_twibbon_main, LiveFeedFragment())
                .commit()
        }
    }
    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

}