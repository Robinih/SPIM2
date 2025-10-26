package com.cvsuagritech.spim.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ProgressBar
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.cvsuagritech.spim.R
import com.cvsuagritech.spim.models.TreatmentRecommendation
import com.cvsuagritech.spim.models.SustainabilityLevel

class RecommendationsAdapter(
    private val recommendations: List<TreatmentRecommendation>,
    private val onItemClick: (TreatmentRecommendation) -> Unit
) : RecyclerView.Adapter<RecommendationsAdapter.RecommendationViewHolder>() {

    class RecommendationViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val tvTitle: TextView = itemView.findViewById(R.id.tv_title)
        val tvDescription: TextView = itemView.findViewById(R.id.tv_description)
        val tvTreatmentType: TextView = itemView.findViewById(R.id.tv_treatment_type)
        val tvSustainabilityBadge: TextView = itemView.findViewById(R.id.tv_sustainability_badge)
        val progressEffectiveness: ProgressBar = itemView.findViewById(R.id.progress_effectiveness)
        val tvEffectivenessPercentage: TextView = itemView.findViewById(R.id.tv_effectiveness_percentage)
        val tvCost: TextView = itemView.findViewById(R.id.tv_cost)
        val tvTimeToApply: TextView = itemView.findViewById(R.id.tv_time_to_apply)
        val btnApplyTreatment: View = itemView.findViewById(R.id.btn_apply_treatment)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecommendationViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_recommendation, parent, false)
        return RecommendationViewHolder(view)
    }

    override fun onBindViewHolder(holder: RecommendationViewHolder, position: Int) {
        val recommendation = recommendations[position]
        
        // Set title and description
        holder.tvTitle.text = recommendation.title
        holder.tvDescription.text = recommendation.description
        
        // Set treatment type
        holder.tvTreatmentType.text = recommendation.getTreatmentTypeDisplayName()
        
        // Set sustainability badge
        holder.tvSustainabilityBadge.text = recommendation.getSustainabilityLevelDisplayName()
        holder.tvSustainabilityBadge.background = getSustainabilityBadgeBackground(holder.itemView.context, recommendation.sustainabilityLevel)
        
        // Set effectiveness
        val effectivenessPercentage = recommendation.getEffectivenessPercentage()
        holder.progressEffectiveness.progress = effectivenessPercentage
        holder.tvEffectivenessPercentage.text = "$effectivenessPercentage%"
        
        // Set cost
        holder.tvCost.text = recommendation.getCostDisplay()
        
        // Set time to apply
        holder.tvTimeToApply.text = recommendation.timeToApply
        
        // Set click listeners
        holder.itemView.setOnClickListener {
            onItemClick(recommendation)
        }
        
        holder.btnApplyTreatment.setOnClickListener {
            onItemClick(recommendation)
        }
    }

    override fun getItemCount(): Int = recommendations.size
    
    private fun getSustainabilityBadgeBackground(context: Context, sustainabilityLevel: SustainabilityLevel): android.graphics.drawable.Drawable {
        val colorRes = when (sustainabilityLevel) {
            SustainabilityLevel.HIGH -> R.color.sustainability_high
            SustainabilityLevel.MEDIUM -> R.color.sustainability_medium
            SustainabilityLevel.LOW -> R.color.sustainability_low
            SustainabilityLevel.CHEMICAL -> R.color.sustainability_chemical
        }
        
        val color = ContextCompat.getColor(context, colorRes)
        val drawable = context.getDrawable(R.drawable.sustainability_badge_background)
        drawable?.setTint(color)
        return drawable ?: context.getDrawable(R.drawable.sustainability_badge_background)!!
    }
}



