package com.example.tubes1pbd.ui.twibbon

import android.graphics.BitmapFactory
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.commit
import androidx.lifecycle.ViewModelProvider
import com.example.tubes1pbd.R
import com.example.tubes1pbd.databinding.FragmentTwibbonMainBinding

class TwibbonMainFragment: Fragment(R.layout.fragment_twibbon_main) {

    private var _binding: FragmentTwibbonMainBinding? = null
    private val binding get() = _binding!!
    private val twibbonViewModel: TwibbonViewModel by lazy {
        ViewModelProvider(requireActivity())[TwibbonViewModel::class.java]
    }

    companion object {
        private const val TAG = "TwibbonMainFragment"
    }
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        twibbonViewModel.setBitmapTwibbonHolder(
            BitmapFactory.decodeResource(
                resources,
                R.drawable.twibex
            )
        )
        super.onCreate(savedInstanceState)
        _binding = FragmentTwibbonMainBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        Log.d(TAG, "onViewCreated:  $savedInstanceState")
        // if no fragment is displayed
        parentFragmentManager.commit {
            replace(R.id.fragment_twibbon_main, LiveFeedFragment(), "liveFeedFragment")
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}