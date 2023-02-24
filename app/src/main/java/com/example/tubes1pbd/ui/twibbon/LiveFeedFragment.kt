package com.example.tubes1pbd.ui.twibbon

import android.Manifest
import android.graphics.BitmapFactory
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.camera.core.ImageCapture
import androidx.camera.core.ImageCaptureException
import androidx.camera.core.ImageProxy
import androidx.camera.core.UseCaseGroup
import androidx.camera.core.ViewPort
import androidx.camera.lifecycle.ProcessCameraProvider
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.commit
import androidx.lifecycle.ViewModelProvider
import com.example.tubes1pbd.R
import com.example.tubes1pbd.databinding.FragmentTwibbonLivefeedBinding

class LiveFeedFragment : Fragment(R.layout.fragment_twibbon_livefeed) {
    private var _binding: FragmentTwibbonLivefeedBinding? = null
    private val binding get() = _binding!!
    private var imageCapture: ImageCapture? = null
    private val twibbonViewModel: TwibbonViewModel by lazy {
        ViewModelProvider(requireActivity())[TwibbonViewModel::class.java]
    }
    companion object {
        private const val TAG = "LiveFeedFragment"
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
        twibbonViewModel.setBitmapTwibbonHolder(
            BitmapFactory.decodeResource(
                resources,
                R.drawable.twibex
            )
        )
        twibbonViewModel.bitmapTwibbonHolder.observe(viewLifecycleOwner){
            binding.twibbonOverlay.setBitmap(it)
        }
        askForPermissions()
        binding.captureButton.setOnClickListener {
            takePhoto()
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun takePhoto() {
        imageCapture = imageCapture ?: return
        imageCapture!!.takePicture(
            ContextCompat.getMainExecutor(requireContext()),
            object : ImageCapture.OnImageCapturedCallback() {
                override fun onCaptureSuccess(image: ImageProxy) {
//                    twibbonViewModel.setBitmapCamHolder(imgProxyToBitmap(image).rotate(270F))
                    twibbonViewModel.setBitmapCamHolder(binding.liveFeedPreview.bitmap!!)
                    super.onCaptureSuccess(image)
                    parentFragmentManager.commit {
                        replace(
                            R.id.fragment_twibbon_main,
                            ResultDisplayFragment(), "resultDisplayFragment"
                        )
                    }
                }
                override fun onError(exception: ImageCaptureException) {
                    super.onError(exception)
                    Log.e(TAG, "Photo capture failed: ${exception.message}", exception)
                }
            }
        )
    }

    private fun startCamera() {
        val cameraProviderFuture = ProcessCameraProvider.getInstance(requireContext())
        cameraProviderFuture.addListener(
            {
                val cameraProvider = cameraProviderFuture.get()
                val viewport = binding.liveFeedPreview.viewPort
                val preview = androidx.camera.core.Preview.Builder()
                    .build()
                    .also {
                        it.setSurfaceProvider(binding.liveFeedPreview.surfaceProvider)
                    }
                val cameraSelector = androidx.camera.core.CameraSelector.DEFAULT_FRONT_CAMERA
                imageCapture = ImageCapture.Builder()
                    .build()
                if (viewport == null) {
                    Log.e(TAG, "ViewPort is null")
                    return@addListener
                }
                val ucg: UseCaseGroup = UseCaseGroup.Builder().setViewPort(viewport)
                    .addUseCase(preview)
                    .addUseCase(imageCapture!!)
                    .build()
                try {
                    cameraProvider.unbindAll()
                    cameraProvider.bindToLifecycle(
                        this, cameraSelector, ucg
                    )
                } catch (e: Exception) {
                    Log.e(TAG, "Use case binding failed", e)
                }

            }, ContextCompat.getMainExecutor(requireContext()))

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