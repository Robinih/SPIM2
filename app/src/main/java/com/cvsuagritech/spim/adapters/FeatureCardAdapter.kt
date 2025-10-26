package com.cvsuagritech.spim.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.cvsuagritech.spim.R

data class FeatureCard(
    val icon: Int,
    val title: Int,
    val description: Int
)

class FeatureCardAdapter(
    private val features: List<FeatureCard>
) : RecyclerView.Adapter<FeatureCardAdapter.FeatureCardViewHolder>() {

    class FeatureCardViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val icon: ImageView = itemView.findViewById(R.id.feature_icon)
        val title: TextView = itemView.findViewById(R.id.feature_title)
        val description: TextView = itemView.findViewById(R.id.feature_description)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FeatureCardViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_feature_card, parent, false)
        return FeatureCardViewHolder(view)
    }

    override fun onBindViewHolder(holder: FeatureCardViewHolder, position: Int) {
        val feature = features[position]
        
        holder.icon.setImageResource(feature.icon)
        holder.title.setText(feature.title)
        holder.description.setText(feature.description)
    }

    override fun getItemCount(): Int = features.size
}
