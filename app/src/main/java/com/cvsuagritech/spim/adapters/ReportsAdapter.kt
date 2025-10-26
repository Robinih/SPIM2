package com.cvsuagritech.spim.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.cvsuagritech.spim.R
import com.cvsuagritech.spim.models.LGUReport
import com.cvsuagritech.spim.models.RiskLevel

class ReportsAdapter(
    private val reports: List<LGUReport>,
    private val onItemClick: (LGUReport) -> Unit
) : RecyclerView.Adapter<ReportsAdapter.ReportViewHolder>() {

    class ReportViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val tvBarangayName: TextView = itemView.findViewById(R.id.tv_barangay_name)
        val tvRiskLevelBadge: TextView = itemView.findViewById(R.id.tv_risk_level_badge)
        val tvReportType: TextView = itemView.findViewById(R.id.tv_report_type)
        val tvGeneratedDate: TextView = itemView.findViewById(R.id.tv_generated_date)
        val tvTotalRecords: TextView = itemView.findViewById(R.id.tv_total_records)
        val tvHealthyCrops: TextView = itemView.findViewById(R.id.tv_healthy_crops)
        val tvIssuesCount: TextView = itemView.findViewById(R.id.tv_issues_count)
        val tvHealthPercentage: TextView = itemView.findViewById(R.id.tv_health_percentage)
        val interventionAlert: LinearLayout = itemView.findViewById(R.id.intervention_alert)
        val btnViewDetails: View = itemView.findViewById(R.id.btn_view_details)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ReportViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_report, parent, false)
        return ReportViewHolder(view)
    }

    override fun onBindViewHolder(holder: ReportViewHolder, position: Int) {
        val report = reports[position]
        
        // Set basic info
        holder.tvBarangayName.text = report.barangayName
        holder.tvReportType.text = report.getReportTypeDisplayName()
        holder.tvGeneratedDate.text = report.getFormattedGeneratedDate()
        
        // Set risk level badge
        holder.tvRiskLevelBadge.text = report.getRiskLevelDisplayName()
        holder.tvRiskLevelBadge.background = getRiskBadgeBackground(holder.itemView.context, report.riskLevel)
        
        // Set statistics
        holder.tvTotalRecords.text = report.totalRecords.toString()
        holder.tvHealthyCrops.text = report.healthyCrops.toString()
        val issuesCount = report.nutrientDeficiency + report.pestDamage + report.disease + report.environmentalStress
        holder.tvIssuesCount.text = issuesCount.toString()
        holder.tvHealthPercentage.text = "${report.getHealthPercentage()}%"
        
        // Show/hide intervention alert
        if (report.interventionNeeded) {
            holder.interventionAlert.visibility = View.VISIBLE
        } else {
            holder.interventionAlert.visibility = View.GONE
        }
        
        // Set click listeners
        holder.itemView.setOnClickListener {
            onItemClick(report)
        }
        
        holder.btnViewDetails.setOnClickListener {
            onItemClick(report)
        }
    }

    override fun getItemCount(): Int = reports.size
    
    private fun getRiskBadgeBackground(context: Context, riskLevel: RiskLevel): android.graphics.drawable.Drawable {
        val colorRes = when (riskLevel) {
            RiskLevel.LOW -> R.color.risk_low
            RiskLevel.MEDIUM -> R.color.risk_medium
            RiskLevel.HIGH -> R.color.risk_high
            RiskLevel.CRITICAL -> R.color.risk_critical
        }
        
        val color = ContextCompat.getColor(context, colorRes)
        val drawable = context.getDrawable(R.drawable.risk_badge_background)
        drawable?.setTint(color)
        return drawable ?: context.getDrawable(R.drawable.risk_badge_background)!!
    }
}




