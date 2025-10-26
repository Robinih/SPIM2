package com.cvsuagritech.spim.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.cvsuagritech.spim.R
import com.cvsuagritech.spim.adapters.RecommendationsAdapter
import com.cvsuagritech.spim.database.CropHealthDatabaseHelper
import com.cvsuagritech.spim.databinding.FragmentRecommendationsBinding
import com.cvsuagritech.spim.models.TreatmentRecommendation
import com.cvsuagritech.spim.models.TreatmentType

class RecommendationsFragment : Fragment() {

    private var _binding: FragmentRecommendationsBinding? = null
    private val binding get() = _binding!!
    
    private lateinit var databaseHelper: CropHealthDatabaseHelper
    private lateinit var recommendationsAdapter: RecommendationsAdapter
    private var allRecommendations = mutableListOf<TreatmentRecommendation>()
    private var filteredRecommendations = mutableListOf<TreatmentRecommendation>()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentRecommendationsBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        
        databaseHelper = CropHealthDatabaseHelper(requireContext())
        setupRecyclerView()
        setupClickListeners()
        loadRecommendations()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun setupRecyclerView() {
        recommendationsAdapter = RecommendationsAdapter(filteredRecommendations) { recommendation ->
            // Handle item click - show details or apply treatment
            showRecommendationDetails(recommendation)
        }
        
        binding.recyclerRecommendations.layoutManager = LinearLayoutManager(requireContext())
        binding.recyclerRecommendations.adapter = recommendationsAdapter
    }

    private fun setupClickListeners() {
        // Filter chips
        binding.chipAll.setOnClickListener {
            filterRecommendations(null)
        }
        
        binding.chipCultural.setOnClickListener {
            filterRecommendations(TreatmentType.CULTURAL)
        }
        
        binding.chipBiological.setOnClickListener {
            filterRecommendations(TreatmentType.BIOLOGICAL)
        }
        
        binding.chipChemical.setOnClickListener {
            filterRecommendations(TreatmentType.CHEMICAL)
        }
        
        binding.chipPrevention.setOnClickListener {
            filterRecommendations(TreatmentType.PREVENTION)
        }
    }

    private fun loadRecommendations() {
        allRecommendations.clear()
        // Get all recommendations from all crop health records
        val cropHealthRecords = databaseHelper.getAllCropHealthRecords()
        cropHealthRecords.forEach { record ->
            val recommendations = databaseHelper.getTreatmentRecommendationsByRecordId(record.id)
            allRecommendations.addAll(recommendations)
        }
        
        // Sort by sustainability level (high to low)
        allRecommendations.sortByDescending { it.sustainabilityLevel }
        
        filterRecommendations(null)
    }

    private fun filterRecommendations(treatmentType: TreatmentType?) {
        filteredRecommendations.clear()
        
        if (treatmentType == null) {
            filteredRecommendations.addAll(allRecommendations)
        } else {
            filteredRecommendations.addAll(allRecommendations.filter { it.treatmentType == treatmentType })
        }
        
        updateFilterChips(treatmentType)
        updateEmptyState()
        recommendationsAdapter.notifyDataSetChanged()
    }

    private fun updateFilterChips(selectedType: TreatmentType?) {
        binding.chipAll.isChecked = selectedType == null
        binding.chipCultural.isChecked = selectedType == TreatmentType.CULTURAL
        binding.chipBiological.isChecked = selectedType == TreatmentType.BIOLOGICAL
        binding.chipChemical.isChecked = selectedType == TreatmentType.CHEMICAL
        binding.chipPrevention.isChecked = selectedType == TreatmentType.PREVENTION
    }

    private fun updateEmptyState() {
        if (filteredRecommendations.isEmpty()) {
            binding.emptyState.visibility = View.VISIBLE
            binding.recyclerRecommendations.visibility = View.GONE
        } else {
            binding.emptyState.visibility = View.GONE
            binding.recyclerRecommendations.visibility = View.VISIBLE
        }
    }

    private fun showRecommendationDetails(recommendation: TreatmentRecommendation) {
        val message = buildString {
            appendLine("Title: ${recommendation.title}")
            appendLine("Type: ${recommendation.getTreatmentTypeDisplayName()}")
            appendLine("Description: ${recommendation.description}")
            appendLine("Instructions: ${recommendation.instructions}")
            appendLine("Effectiveness: ${recommendation.getEffectivenessPercentage()}%")
            appendLine("Cost: ${recommendation.getCostDisplay()}")
            appendLine("Time to Apply: ${recommendation.timeToApply}")
            appendLine("Sustainability: ${recommendation.getSustainabilityLevelDisplayName()}")
            if (!recommendation.activeIngredients.isNullOrEmpty()) {
                appendLine("Active Ingredients: ${recommendation.activeIngredients}")
            }
            if (!recommendation.safetyNotes.isNullOrEmpty()) {
                appendLine("Safety Notes: ${recommendation.safetyNotes}")
            }
        }

        androidx.appcompat.app.AlertDialog.Builder(requireContext())
            .setTitle("Treatment Recommendation Details")
            .setMessage(message)
            .setPositiveButton("Apply Treatment") { _, _ ->
                applyTreatment(recommendation)
            }
            .setNegativeButton("Close") { dialog, _ ->
                dialog.dismiss()
            }
            .show()
    }

    private fun applyTreatment(recommendation: TreatmentRecommendation) {
        // In a real app, this would mark the treatment as applied
        Toast.makeText(requireContext(), "Treatment '${recommendation.title}' applied successfully!", Toast.LENGTH_SHORT).show()
    }

    override fun onResume() {
        super.onResume()
        // Refresh data when returning to this fragment
        loadRecommendations()
    }
}



