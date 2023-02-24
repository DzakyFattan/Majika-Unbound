package com.example.tubes1pbd.ui.payment

import android.Manifest
import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.util.Log
import android.util.Size
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import androidx.camera.core.CameraSelector
import androidx.camera.core.ImageAnalysis
import androidx.camera.core.ImageCapture
import androidx.camera.core.UseCaseGroup
import androidx.camera.lifecycle.ProcessCameraProvider
import androidx.core.content.ContextCompat
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.tubes1pbd.MainActivity
import com.example.tubes1pbd.R
import com.example.tubes1pbd.data.MajikaRoomDatabase
import com.example.tubes1pbd.databinding.ActivityPaymentBinding
import com.example.tubes1pbd.service.QRAnalyzer
import com.example.tubes1pbd.ui.keranjang.CartViewModel
import com.example.tubes1pbd.ui.twibbon.LiveFeedFragment
import com.example.tubes1pbd.ui.twibbon.TwibbonViewModel
import kotlinx.coroutines.delay

class PaymentActivity: AppCompatActivity(R.layout.activity_payment) {
    private var _binding: ActivityPaymentBinding? = null
    private val binding get() = _binding!!
    private lateinit var database: MajikaRoomDatabase
    private val viewModel: PaymentViewModel by lazy {
        ViewModelProvider(this, PaymentViewModel.Factory(database))[PaymentViewModel::class.java]
    }

    companion object {
        private const val TAG = "PaymentActivity"
        private val REQUIRED_PERMISSIONS = mutableListOf(
            Manifest.permission.CAMERA
        ).toTypedArray()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
        super.onCreate(savedInstanceState)
        _binding = ActivityPaymentBinding.inflate(layoutInflater)
        setContentView(binding.root)
        askForPermissions()
        database = MajikaRoomDatabase.getDatabase(this)
        // livedataobserver
        viewModel.isPaymentSuccessful.observe(this){
            if (it) {
                Intent(this, MainActivity::class.java).also {backintent ->
                    startActivity(backintent)
                }
            }
        }
        binding.paymentTotalDue.text = "Total : ${intent.getStringExtra("price")}"
        viewModel.paymentStatus.observe(this){
            if (it == "Pembayaran Berhasil") {
                binding.paymentStatusPreview.setTextColor(Color.parseColor("#228C22"))
            } else if (it == "Pembayaran Gagal"){
                binding.paymentStatusPreview.setTextColor(Color.parseColor("#FF0000"))
            } else {
                binding.paymentStatusPreview.setTextColor(Color.parseColor("#000000"))
            }
            binding.paymentStatusPreview.text = it
        }
        viewModel.paymentMessage.observe(this){
            binding.paymentResultComment.text = it
        }
    }
    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }
    private fun askForPermissions() {
        val requestPermissionLauncher = registerForActivityResult(
            androidx.activity.result.contract.ActivityResultContracts.RequestMultiplePermissions()
        ) { permissions ->
            if (permissions.all { it.value }) {
                startCamera()
            } else {
                Toast.makeText(
                    this,
                    "Permissions not granted by the user.",
                    Toast.LENGTH_SHORT
                ).show()
            }
        }
        requestPermissionLauncher.launch(REQUIRED_PERMISSIONS)
    }
    private fun startCamera() {
        val cameraProviderFuture = ProcessCameraProvider.getInstance(this)
        cameraProviderFuture.addListener(
            {
                val cameraProvider = cameraProviderFuture.get()
                val viewport = binding.liveQRPreview.viewPort
                val preview = androidx.camera.core.Preview.Builder()
                    .build()
                    .also {
                        it.setSurfaceProvider(binding.liveQRPreview.surfaceProvider)
                    }
                val cameraSelector = CameraSelector.DEFAULT_BACK_CAMERA
                val barcodeQRAnalyzer = ImageAnalysis.Builder().setTargetResolution(
                    Size(binding.liveQRPreview.width, binding.liveQRPreview.height)
                ).setBackpressureStrategy(ImageAnalysis.STRATEGY_KEEP_ONLY_LATEST).build().
                        also {
                            it.setAnalyzer(
                                ContextCompat.getMainExecutor(this),
                                QRAnalyzer {qrString ->
                                   viewModel.postDebounced(qrString)
                                }
                            )
                        }
                if (viewport == null) {
                    Log.e( "PaymentAct", "ViewPort is null")
                    return@addListener
                }
                val ucg: UseCaseGroup = UseCaseGroup.Builder().setViewPort(viewport)
                    .addUseCase(preview)
                    .addUseCase(barcodeQRAnalyzer)
                    .build()
                try {
                    cameraProvider.unbindAll()
                    cameraProvider.bindToLifecycle(
                        this, cameraSelector, ucg
                    )
                } catch (e: Exception) {
                    Log.e(TAG, "Use case binding failed", e)
                }
            }
        , ContextCompat.getMainExecutor(this))
    }

}