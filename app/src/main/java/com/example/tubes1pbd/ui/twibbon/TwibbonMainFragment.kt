package com.example.tubes1pbd.ui.twibbon

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.commit
import com.example.tubes1pbd.R
import com.example.tubes1pbd.databinding.FragmentTwibbonMainBinding

class TwibbonMainFragment: Fragment(R.layout.fragment_twibbon_main) {

    private var _binding: FragmentTwibbonMainBinding? = null
    private val binding get() = _binding!!

    companion object {
        private const val TAG = "TwibbonMainFragment"
    }
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        super.onCreate(savedInstanceState)
        _binding = FragmentTwibbonMainBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        Log.d(TAG, "onViewCreated:  $savedInstanceState")
        // if no fragment is displayed
        if (savedInstanceState == null) {
            parentFragmentManager.commit {
                setReorderingAllowed(true)
                replace(R.id.fragment_twibbon_main, LiveFeedFragment())
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}