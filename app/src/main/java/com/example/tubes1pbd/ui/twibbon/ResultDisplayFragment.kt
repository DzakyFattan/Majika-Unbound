package com.example.tubes1pbd.ui.twibbon

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.example.tubes1pbd.R
import com.example.tubes1pbd.databinding.FragmentTwibbonResultdisplayBinding

class ResultDisplayFragment : Fragment(R.layout.fragment_twibbon_resultdisplay) {
    private var _binding: FragmentTwibbonResultdisplayBinding? = null
    private val binding get() = _binding!!
    private val twibbonViewModel: TwibbonViewModel by lazy {
        ViewModelProvider(requireActivity())[TwibbonViewModel::class.java]
    }
    companion object;
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
        twibbonViewModel.bitmapCombined.observe(viewLifecycleOwner){
            binding.resultView.setImageBitmap(it)
        }
        twibbonViewModel.saveBitmapMessage.observe(viewLifecycleOwner){
            it.getContentIfNotHandled()?.let {msg ->
                Toast.makeText(context, msg, Toast.LENGTH_SHORT).show()
            }
        }
        binding.recaptureButton.setOnClickListener {
            // navigate
            parentFragmentManager
                .beginTransaction()
                .replace(R.id.fragment_twibbon_main, LiveFeedFragment(), "liveFeedFragment")
                .commit()
        }
        binding.saveButton.setOnClickListener {
            // save
            twibbonViewModel.saveCombinedBitmap()
        }
    }
    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

}