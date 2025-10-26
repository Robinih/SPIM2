package com.cvsuagritech.spim.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.cvsuagritech.spim.R
import com.cvsuagritech.spim.adapters.LogbookAdapter
import com.cvsuagritech.spim.database.CropHealthDatabaseHelper
import com.cvsuagritech.spim.databinding.FragmentLogbookBinding
import com.cvsuagritech.spim.models.CropHealthRecord

class LogbookFragment : Fragment() {

    private var _binding: FragmentLogbookBinding? = null
    private val binding get() = _binding!!
    
    private lateinit var databaseHelper: CropHealthDatabaseHelper
    private lateinit var logbookAdapter: LogbookAdapter
    private var cropHealthRecords = mutableListOf<CropHealthRecord>()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentLogbookBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        
        databaseHelper = CropHealthDatabaseHelper(requireContext())
        setupRecyclerView()
        setupClickListeners()
        loadLogbook()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun setupRecyclerView() {
        logbookAdapter = LogbookAdapter(cropHealthRecords) { cropHealthRecord ->
            // Handle item click - show details or navigate to detail screen
            showRecordDetails(cropHealthRecord)
        }
        
        binding.recyclerLogbook.layoutManager = LinearLayoutManager(requireContext())
        binding.recyclerLogbook.adapter = logbookAdapter
    }

    private fun setupClickListeners() {
        binding.fabClearLogbook.setOnClickListener {
            showClearLogbookDialog()
        }
        
        // Filter chip listeners
        binding.chipFilterAll.setOnClickListener {
            filterRecords("all")
        }
        
        binding.chipFilterHealthy.setOnClickListener {
            filterRecords("healthy")
        }
        
        binding.chipFilterIssues.setOnClickListener {
            filterRecords("issues")
        }
    }

    private fun loadLogbook() {
        cropHealthRecords.clear()
        cropHealthRecords.addAll(databaseHelper.getAllCropHealthRecords())
        
        logbookAdapter.notifyDataSetChanged()
        updateStatistics()
        
        if (cropHealthRecords.isEmpty()) {
            binding.emptyState.visibility = View.VISIBLE
            binding.recyclerLogbook.visibility = View.GONE
        } else {
            binding.emptyState.visibility = View.GONE
            binding.recyclerLogbook.visibility = View.VISIBLE
        }
    }
    
    private fun updateStatistics() {
        val totalRecords = cropHealthRecords.size
        val healthyCount = cropHealthRecords.count { it.healthStatus == com.cvsuagritech.spim.models.CropHealthStatus.HEALTHY }
        val issuesCount = totalRecords - healthyCount
        
        binding.tvTotalRecords.text = totalRecords.toString()
        binding.tvHealthyCount.text = healthyCount.toString()
        binding.tvIssuesCount.text = issuesCount.toString()
    }
    
    private fun filterRecords(filterType: String) {
        val allRecords = databaseHelper.getAllCropHealthRecords()
        val filteredRecords = when (filterType) {
            "healthy" -> allRecords.filter { it.healthStatus == com.cvsuagritech.spim.models.CropHealthStatus.HEALTHY }
            "issues" -> allRecords.filter { it.healthStatus != com.cvsuagritech.spim.models.CropHealthStatus.HEALTHY }
            else -> allRecords
        }
        
        cropHealthRecords.clear()
        cropHealthRecords.addAll(filteredRecords)
        logbookAdapter.notifyDataSetChanged()
        
        // Update chip states
        binding.chipFilterAll.isChecked = filterType == "all"
        binding.chipFilterHealthy.isChecked = filterType == "healthy"
        binding.chipFilterIssues.isChecked = filterType == "issues"
        
        // Show/hide empty state
        if (cropHealthRecords.isEmpty()) {
            binding.emptyState.visibility = View.VISIBLE
            binding.recyclerLogbook.visibility = View.GONE
        } else {
            binding.emptyState.visibility = View.GONE
            binding.recyclerLogbook.visibility = View.VISIBLE
        }
    }

    private fun showRecordDetails(cropHealthRecord: CropHealthRecord) {
        val message = buildString {
            appendLine("Crop: ${cropHealthRecord.cropType}")
            appendLine("Health Status: ${cropHealthRecord.getHealthStatusDisplayName()}")
            appendLine("Growth Stage: ${cropHealthRecord.getGrowthStageDisplayName()}")
            appendLine("Confidence: ${cropHealthRecord.getConfidencePercentage()}%")
            appendLine("Date: ${cropHealthRecord.getFormattedDate()}")
            appendLine("Time: ${cropHealthRecord.getFormattedTime()}")
            if (!cropHealthRecord.location.isNullOrEmpty()) {
                appendLine("Location: ${cropHealthRecord.location}")
            }
            if (!cropHealthRecord.notes.isNullOrEmpty()) {
                appendLine("Notes: ${cropHealthRecord.notes}")
            }
            appendLine("Sustainability: ${cropHealthRecord.getSustainabilityLevel()}")
        }

        AlertDialog.Builder(requireContext())
            .setTitle("Crop Health Record Details")
            .setMessage(message)
            .setPositiveButton("OK") { dialog, _ ->
                dialog.dismiss()
            }
            .setNegativeButton("Delete") { _, _ ->
                showDeleteRecordDialog(cropHealthRecord)
            }
            .show()
    }

    private fun showDeleteRecordDialog(cropHealthRecord: CropHealthRecord) {
        AlertDialog.Builder(requireContext())
            .setTitle("Delete Record")
            .setMessage("Are you sure you want to delete this crop health record?")
            .setPositiveButton("Delete") { _, _ ->
                if (databaseHelper.deleteCropHealthRecord(cropHealthRecord.id)) {
                    loadLogbook()
                    Toast.makeText(requireContext(), "Record deleted", Toast.LENGTH_SHORT).show()
                } else {
                    Toast.makeText(requireContext(), "Failed to delete record", Toast.LENGTH_SHORT).show()
                }
            }
            .setNegativeButton("Cancel") { dialog, _ ->
                dialog.dismiss()
            }
            .show()
    }

    private fun showClearLogbookDialog() {
        AlertDialog.Builder(requireContext())
            .setTitle("Clear All Logbook")
            .setMessage("Are you sure you want to delete all crop health records? This action cannot be undone.")
            .setPositiveButton("Clear All") { _, _ ->
                val deletedCount = databaseHelper.deleteAllCropHealthRecords()
                if (deletedCount > 0) {
                    loadLogbook()
                    Toast.makeText(requireContext(), "Cleared $deletedCount records", Toast.LENGTH_SHORT).show()
                } else {
                    Toast.makeText(requireContext(), "No records to clear", Toast.LENGTH_SHORT).show()
                }
            }
            .setNegativeButton("Cancel") { dialog, _ ->
                dialog.dismiss()
            }
            .show()
    }

    override fun onResume() {
        super.onResume()
        // Refresh data when returning to this fragment
        loadLogbook()
    }
}
