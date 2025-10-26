package com.cvsuagritech.spim.fragments

import android.app.Activity
import android.content.ContentValues
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Canvas
import android.graphics.drawable.BitmapDrawable
import android.graphics.drawable.Drawable
import android.graphics.drawable.VectorDrawable
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.result.ActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AlertDialog
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import com.cvsuagritech.spim.R
import com.cvsuagritech.spim.database.CropHealthDatabaseHelper
import com.cvsuagritech.spim.databinding.FragmentHomeBinding
import com.cvsuagritech.spim.models.CropHealthRecord
import com.cvsuagritech.spim.utils.CropHealthSimulator
import java.io.IOException
import kotlin.math.roundToInt
import kotlin.random.Random

class HomeFragment : Fragment() {

    private var _binding: FragmentHomeBinding? = null
    private val binding get() = _binding!!
    
    private lateinit var databaseHelper: CropHealthDatabaseHelper
    private val GALLERY_REQUEST_CODE = 133
    private var isAnalyzing = false

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        
        databaseHelper = CropHealthDatabaseHelper(requireContext())
        setupUI()
        setupClickListeners()
        updateStatus(getString(R.string.status_ready))
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun setupUI() {
        // Initialize UI state
        binding.loadingOverlay.visibility = View.GONE
        binding.confidenceSection.visibility = View.GONE
        binding.resultsContent.visibility = View.GONE
        binding.imageActionsOverlay.visibility = View.GONE
        
        // Set initial text
        binding.tvOutput.text = getString(R.string.placeholder_result)
        binding.confidencePercentage.text = getString(R.string.placeholder_confidence)
        binding.healthStatus.text = "Healthy"
    }

    private fun setupClickListeners() {
        // Camera button - Simulated
        binding.btnCaptureImage.setOnClickListener {
            simulateCaptureImage()
        }

        // Gallery button - Simulated
        binding.btnLoadImage.setOnClickListener {
            simulateLoadImage()
        }

        // Auto-analyze when image is loaded
        binding.ImageView.setOnClickListener {
            if (binding.ImageView.drawable != null && 
                binding.ImageView.drawable !is android.graphics.drawable.ColorDrawable) {
                analyzeCurrentImage()
            }
        }

        // View recommendations button
        binding.btnViewRecommendations.setOnClickListener {
            val pestName = binding.tvOutput.text.toString()
            if (pestName != getString(R.string.placeholder_result)) {
                showRecommendations(pestName)
            }
        }

        // Save to logbook button
        binding.btnSaveToLogbook.setOnClickListener {
            saveToLogbook()
        }

        // Clear image button
        binding.btnClearImage.setOnClickListener {
            clearImage()
        }

        // Analyze button
        binding.btnAnalyze.setOnClickListener {
            analyzeImage()
        }

        // Result text click to search
        binding.tvOutput.setOnClickListener {
            if (binding.tvOutput.text != getString(R.string.placeholder_result)) {
                searchOnline(binding.tvOutput.text.toString())
            }
        }

        // Long press image to save
        binding.ImageView.setOnLongClickListener {
            if (binding.ImageView.drawable != null && 
                binding.ImageView.drawable !is android.graphics.drawable.ColorDrawable) {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
                    showSaveDialog()
                } else {
                    storagePermissionLauncher.launch(android.Manifest.permission.WRITE_EXTERNAL_STORAGE)
                }
            }
            return@setOnLongClickListener true
        }
    }

    private fun openGallery() {
        val intent = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
        intent.type = "image/*"
        val mimeTypes = arrayOf("image/jpeg", "image/png", "image/jpg")
        intent.putExtra(Intent.EXTRA_MIME_TYPES, mimeTypes)
        intent.flags = Intent.FLAG_GRANT_READ_URI_PERMISSION
        onresult.launch(intent)
    }

    private fun clearImage() {
        binding.ImageView.setImageResource(R.drawable.place_holder)
        binding.tvOutput.text = getString(R.string.placeholder_result)
        binding.confidenceSection.visibility = View.GONE
        binding.resultsContent.visibility = View.GONE
        updateStatus(getString(R.string.status_ready))
    }

    private fun analyzeCurrentImage() {
        val drawable = binding.ImageView.drawable
        if (drawable is BitmapDrawable) {
            val bitmap = drawable.bitmap
            if (bitmap != null) {
                outputGenerator(bitmap)
            } else {
                showError(getString(R.string.error_no_image))
            }
        } else {
            showError(getString(R.string.error_no_image))
        }
    }

    private fun searchOnline(query: String) {
        if (query.isNotEmpty() && query != getString(R.string.placeholder_result)) {
            val intent = Intent(
                Intent.ACTION_VIEW,
                Uri.parse("https://www.google.com/search?q=${query} pest identification")
            )
            startActivity(intent)
        }
    }

    private fun updateStatus(status: String) {
        binding.statusIndicator.text = status
    }

    private fun showError(message: String) {
        Toast.makeText(requireContext(), message, Toast.LENGTH_LONG).show()
        updateStatus(getString(R.string.status_error))
    }

    private fun showSuccess(message: String) {
        Toast.makeText(requireContext(), message, Toast.LENGTH_SHORT).show()
        updateStatus(getString(R.string.status_complete))
    }

    // Permission request launchers
    private val requestPermission =
        registerForActivityResult(ActivityResultContracts.RequestPermission()) { granted ->
            if (granted) {
                takePicturePreview.launch(null)
            } else {
                showError(getString(R.string.permission_denied))
            }
        }

    private val requestStoragePermission =
        registerForActivityResult(ActivityResultContracts.RequestPermission()) { granted ->
            if (granted) {
                openGallery()
            } else {
                showError(getString(R.string.permission_denied))
            }
        }

    // Camera launcher
    private val takePicturePreview =
        registerForActivityResult(ActivityResultContracts.TakePicturePreview()) { bitmap ->
            if (bitmap != null) {
                binding.ImageView.setImageBitmap(bitmap)
                showImageActions()
                updateStatus(getString(R.string.status_ready))
            }
        }

    // Gallery launcher
    private val onresult =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            Log.i("TAG", "This is the result: ${result.data} ${result.resultCode}")
            onResultReceived(GALLERY_REQUEST_CODE, result)
        }

    private fun onResultReceived(requestCode: Int, result: ActivityResult?) {
        when (requestCode) {
            GALLERY_REQUEST_CODE -> {
                if (result?.resultCode == Activity.RESULT_OK) {
                    result?.data?.data?.let { uri ->
                        Log.i("TAG", "OnResultReceived: $uri")
                        try {
                            val bitmap = BitmapFactory.decodeStream(requireContext().contentResolver.openInputStream(uri))
                            if (bitmap != null) {
                                binding.ImageView.setImageBitmap(bitmap)
                                showImageActions()
                                updateStatus(getString(R.string.status_ready))
                            } else {
                                showError(getString(R.string.error_image_load_failed))
                            }
                        } catch (e: Exception) {
                            Log.e("TAG", "Error loading image: ${e.message}")
                            showError(getString(R.string.error_image_load_failed))
                        }
                    }
                } else {
                    Log.e("TAG", "onActivityResult: error in selecting image")
                    showError(getString(R.string.error_image_load_failed))
                }
            }
        }
    }

    private fun showImageActions() {
        // Auto-analyze when image is loaded
        analyzeCurrentImage()
    }

    private fun outputGenerator(bitmap: Bitmap) {
        if (isAnalyzing) return
        
        isAnalyzing = true
        binding.loadingOverlay.visibility = View.VISIBLE
        updateStatus(getString(R.string.status_analyzing))
        
        try {
            // Simulate analysis delay
            Thread.sleep(2000)
            
            // Convert bitmap to byte array for storage
            val imageBlob = databaseHelper.bitmapToByteArray(bitmap)
            
            // Simulate crop health analysis
            val cropHealthRecord = CropHealthSimulator.simulateCropHealthAnalysis(imageBlob)
            
            // Save to database
            val recordId = databaseHelper.insertCropHealthRecord(cropHealthRecord)
            
            // Generate treatment recommendations
            val recommendations = CropHealthSimulator.generateTreatmentRecommendations(
                cropHealthRecord.copy(id = recordId)
            )
            
            // Save recommendations to database
            recommendations.forEach { recommendation ->
                databaseHelper.insertTreatmentRecommendation(recommendation)
            }
            
            val confidence = (cropHealthRecord.confidence * 100).roundToInt()
            
            // Update UI on main thread
            requireActivity().runOnUiThread {
                binding.loadingOverlay.visibility = View.GONE
                
                // Set result text
                binding.tvOutput.text = cropHealthRecord.getHealthStatusDisplayName()
                
                // Show confidence
                binding.confidenceSection.visibility = View.VISIBLE
                binding.confidencePercentage.text = "$confidence%"
                binding.confidenceProgress.progress = confidence
                
                // Show result actions
                binding.resultsContent.visibility = View.VISIBLE
                
                updateStatus(getString(R.string.status_complete))
                isAnalyzing = false
                
                Log.i("TAG", "Crop health analysis complete: ${cropHealthRecord.healthStatus} with confidence: $confidence%")
            }

        } catch (e: Exception) {
            Log.e("TAG", "Error during analysis: ${e.message}")
            requireActivity().runOnUiThread {
                binding.loadingOverlay.visibility = View.GONE
                showError(getString(R.string.error_analysis_failed))
                isAnalyzing = false
            }
        }
    }

    // Storage permission launcher for saving images
    private val storagePermissionLauncher =
        registerForActivityResult(ActivityResultContracts.RequestPermission()) { isGranted: Boolean ->
            if (isGranted) {
                showSaveDialog()
            } else {
                showError(getString(R.string.permission_denied))
            }
        }

    // Helper function to show the save confirmation dialog
    private fun showSaveDialog() {
        val drawable = binding.ImageView.drawable as? BitmapDrawable
        val bitmapToSave = drawable?.bitmap

        if (bitmapToSave == null) {
            showError(getString(R.string.error_no_image))
            return
        }

        AlertDialog.Builder(requireContext())
            .setTitle(getString(R.string.dialog_save_title))
            .setMessage(getString(R.string.dialog_save_message))
            .setPositiveButton(getString(R.string.dialog_yes)) { _, _ ->
                downloadImage(bitmapToSave)
            }
            .setNegativeButton(getString(R.string.dialog_no)) { dialog, _ ->
                dialog.dismiss()
            }
            .show()
    }

    // Modern function to save a bitmap to the device's gallery
    private fun downloadImage(bitmap: Bitmap) {
        val fileName = "SPIM_Pest_Image_${System.currentTimeMillis()}.png"
        val resolver = requireContext().contentResolver

        val imageCollection = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            MediaStore.Images.Media.getContentUri(MediaStore.VOLUME_EXTERNAL_PRIMARY)
        } else {
            MediaStore.Images.Media.EXTERNAL_CONTENT_URI
        }

        val contentValues = ContentValues().apply {
            put(MediaStore.Images.Media.DISPLAY_NAME, fileName)
            put(MediaStore.Images.Media.MIME_TYPE, "image/png")
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
                put(MediaStore.Images.Media.IS_PENDING, 1)
            }
        }

        val imageUri = resolver.insert(imageCollection, contentValues)

        if (imageUri == null) {
            showError(getString(R.string.error_save_failed))
            Log.e("HomeFragment", "Failed to create new MediaStore record.")
            return
        }

        try {
            resolver.openOutputStream(imageUri)?.use { outputStream ->
                if (!bitmap.compress(Bitmap.CompressFormat.PNG, 100, outputStream)) {
                    throw IOException("Couldn't save bitmap.")
                }
            }

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
                contentValues.clear()
                contentValues.put(MediaStore.Images.Media.IS_PENDING, 0)
                resolver.update(imageUri, contentValues, null, null)
            }

            showSuccess(getString(R.string.error_save_success))
        } catch (e: IOException) {
            // If something went wrong, delete the pending entry.
            resolver.delete(imageUri, null, null)
            showError(getString(R.string.error_save_failed))
            Log.e("HomeFragment", "Failed to save bitmap.", e)
        }
    }
    
    // Simulation methods for demo purposes
    private fun simulateLoadImage() {
        // Show loading state
        updateStatus("Loading demo crop image...")
        
        // Simulate loading delay
        binding.root.postDelayed({
            val demoImages = listOf(
                R.drawable.demo_crop_healthy,
                R.drawable.demo_crop_pest_damage,
                R.drawable.demo_crop_nutrient_deficiency,
                R.drawable.demo_crop_disease
            )
            
            val randomImage = demoImages[Random.nextInt(demoImages.size)]
            val drawable = ContextCompat.getDrawable(requireContext(), randomImage)
            
            if (drawable != null) {
                val bitmap = drawableToBitmap(drawable)
                binding.ImageView.setImageBitmap(bitmap)
                updateStatus(getString(R.string.status_ready))
                
                // Auto-analyze the loaded image
                analyzeCurrentImage()
                
                Toast.makeText(requireContext(), "Demo crop image loaded successfully!", Toast.LENGTH_SHORT).show()
            }
        }, 1000)
    }
    
    private fun simulateCaptureImage() {
        // Show loading state
        updateStatus("Capturing demo crop image...")
        
        // Simulate camera capture delay
        binding.root.postDelayed({
            val demoImages = listOf(
                R.drawable.demo_crop_healthy,
                R.drawable.demo_crop_pest_damage,
                R.drawable.demo_crop_nutrient_deficiency,
                R.drawable.demo_crop_disease
            )
            
            val randomImage = demoImages[Random.nextInt(demoImages.size)]
            val drawable = ContextCompat.getDrawable(requireContext(), randomImage)
            
            if (drawable != null) {
                val bitmap = drawableToBitmap(drawable)
                binding.ImageView.setImageBitmap(bitmap)
                updateStatus(getString(R.string.status_ready))
                
                // Auto-analyze the captured image
                analyzeCurrentImage()
                
                Toast.makeText(requireContext(), "Demo crop image captured successfully!", Toast.LENGTH_SHORT).show()
            }
        }, 1500)
    }
    
    private fun drawableToBitmap(drawable: Drawable): Bitmap {
        if (drawable is BitmapDrawable) {
            return drawable.bitmap
        }
        
        val bitmap = Bitmap.createBitmap(
            drawable.intrinsicWidth,
            drawable.intrinsicHeight,
            Bitmap.Config.ARGB_8888
        )
        val canvas = Canvas(bitmap)
        drawable.setBounds(0, 0, canvas.width, canvas.height)
        drawable.draw(canvas)
        return bitmap
    }

    private fun showRecommendations(pestName: String) {
        // Navigate to recommendations fragment
        Toast.makeText(requireContext(), "Opening recommendations for $pestName", Toast.LENGTH_SHORT).show()
    }

    private fun saveToLogbook() {
        // Save current analysis to logbook
        Toast.makeText(requireContext(), "Saved to logbook", Toast.LENGTH_SHORT).show()
    }

    private fun analyzeImage() {
        if (binding.ImageView.drawable != null && 
            binding.ImageView.drawable !is android.graphics.drawable.ColorDrawable) {
            analyzeCurrentImage()
        } else {
            Toast.makeText(requireContext(), "Please select an image first", Toast.LENGTH_SHORT).show()
        }
    }
}
