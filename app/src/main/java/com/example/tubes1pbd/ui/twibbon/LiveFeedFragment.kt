package com.example.tubes1pbd.ui.twibbon

import android.Manifest
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.camera.lifecycle.ProcessCameraProvider
import androidx.core.content.ContentProviderCompat.requireContext
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import com.example.tubes1pbd.R
import com.example.tubes1pbd.databinding.FragmentTwibbonLivefeedBinding

class LiveFeedFragment : Fragment(R.layout.fragment_twibbon_livefeed) {
    private var _binding: FragmentTwibbonLivefeedBinding? = null
    private val binding get() = _binding!!

    companion object {
        private const val TAG = "LiveFeedFragment"
        private const val FILENAME_FORMAT = "yyyy-MM-dd-HH-mm-ss-SSS"
        private const val REQUEST_CODE_PERMISSIONS = 10
        private val REQUIRED_PERMISSIONS = mutableListOf(
            Manifest.permission.CAMERA
        ).toTypedArray()
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        super.onCreateView(inflater, container, savedInstanceState)
        _binding = FragmentTwibbonLivefeedBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        askForPermissions()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun takePhoto() {

    }

    private fun startCamera() {
        val cameraProviderFuture = ProcessCameraProvider.getInstance(requireContext())
        cameraProviderFuture.addListener(
            {
                val cameraProvider = cameraProviderFuture.get()
                val preview = androidx.camera.core.Preview.Builder()
                    .build()
                    .also {
                        it.setSurfaceProvider(binding.liveFeedPreview.surfaceProvider)
                    }
                val cameraSelector = androidx.camera.core.CameraSelector.DEFAULT_FRONT_CAMERA
                try {
                    cameraProvider.unbindAll()
                    cameraProvider.bindToLifecycle(
                        this, cameraSelector, preview
                    )
                } catch (e: Exception) {
                    Log.e(TAG, "Use case binding failed", e)
                }

            }, ContextCompat.getMainExecutor(requireContext()))

    }

    private fun allPermissionGranted() = REQUIRED_PERMISSIONS.all {
        requireContext().checkSelfPermission(it) == PackageManager.PERMISSION_GRANTED
    }

    private fun askForPermissions() {
        val requestPermissionLauncher = registerForActivityResult(
            androidx.activity.result.contract.ActivityResultContracts.RequestMultiplePermissions()
        ) { permissions ->
            if (permissions.all { it.value }) {
                startCamera()
            } else {
                Toast.makeText(
                    requireContext(),
                    "Permissions not granted by the user.",
                    Toast.LENGTH_SHORT
                ).show()
            }
        }
        requestPermissionLauncher.launch(REQUIRED_PERMISSIONS)
    }

}