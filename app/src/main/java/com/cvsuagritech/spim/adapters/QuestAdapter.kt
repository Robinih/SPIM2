package com.cvsuagritech.spim.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.ProgressBar
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.cvsuagritech.spim.R
import com.cvsuagritech.spim.models.Quest
import com.google.android.material.button.MaterialButton

class QuestAdapter(
    private val onQuestClick: (Quest) -> Unit
) : RecyclerView.Adapter<QuestAdapter.QuestViewHolder>() {

    private var quests = listOf<Quest>()

    fun updateQuests(newQuests: List<Quest>) {
        quests = newQuests
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): QuestViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_quest, parent, false)
        return QuestViewHolder(view)
    }

    override fun onBindViewHolder(holder: QuestViewHolder, position: Int) {
        holder.bind(quests[position])
    }

    override fun getItemCount(): Int = quests.size

    inner class QuestViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val questIcon: ImageView = itemView.findViewById(R.id.quest_icon)
        private val questTitle: TextView = itemView.findViewById(R.id.tv_quest_title)
        private val questDescription: TextView = itemView.findViewById(R.id.tv_quest_description)
        private val questPoints: TextView = itemView.findViewById(R.id.tv_quest_points)
        private val progressText: TextView = itemView.findViewById(R.id.tv_progress_text)
        private val progressBar: ProgressBar = itemView.findViewById(R.id.progress_bar_quest)
        private val questStatus: TextView = itemView.findViewById(R.id.tv_quest_status)
        private val completeButton: MaterialButton = itemView.findViewById(R.id.btn_complete_quest)

        fun bind(quest: Quest) {
            questTitle.text = quest.title
            questDescription.text = quest.description
            questPoints.text = "+${quest.points}"
            
            // Update progress
            val progress = if (quest.targetCount > 0) {
                (quest.currentCount * 100) / quest.targetCount
            } else {
                if (quest.isCompleted) 100 else 0
            }
            
            progressBar.progress = progress
            progressText.text = "${quest.currentCount}/${quest.targetCount}"
            
            // Update status and button
            if (quest.isCompleted) {
                questStatus.text = "✅ Completed"
                questStatus.setTextColor(itemView.context.getColor(R.color.status_success))
                completeButton.text = "Completed"
                completeButton.isEnabled = false
                completeButton.backgroundTintList = itemView.context.getColorStateList(R.color.text_modern_secondary)
                questIcon.alpha = 0.6f
            } else {
                questStatus.text = "🎯 In Progress"
                questStatus.setTextColor(itemView.context.getColor(R.color.gradient_accent_start))
                completeButton.text = "Complete"
                completeButton.isEnabled = true
                completeButton.backgroundTintList = itemView.context.getColorStateList(R.color.gradient_accent_start)
                questIcon.alpha = 1.0f
            }
            
            // Set quest icon based on type
            questIcon.setImageResource(getQuestIcon(quest.type))
            
            // Set click listeners
            completeButton.setOnClickListener {
                if (!quest.isCompleted) {
                    onQuestClick(quest)
                }
            }
            
            itemView.setOnClickListener {
                if (!quest.isCompleted) {
                    onQuestClick(quest)
                }
            }
        }
        
        private fun getQuestIcon(questType: com.cvsuagritech.spim.models.QuestType): Int {
            return when (questType) {
                com.cvsuagritech.spim.models.QuestType.LOGIN_DAILY -> R.drawable.ic_crop_health
                com.cvsuagritech.spim.models.QuestType.ANALYZE_RICE_CROP -> R.drawable.ic_crop_health
                com.cvsuagritech.spim.models.QuestType.CHECK_SOIL_CONDITIONS -> R.drawable.ic_fertilizer
                com.cvsuagritech.spim.models.QuestType.MONITOR_WATER_LEVEL -> R.drawable.ic_fertilizer
                com.cvsuagritech.spim.models.QuestType.APPLY_FERTILIZER -> R.drawable.ic_fertilizer
                com.cvsuagritech.spim.models.QuestType.CONTROL_PESTS -> R.drawable.ic_crop_health
                com.cvsuagritech.spim.models.QuestType.VIEW_FARM_MAP -> R.drawable.ic_crop_health
                com.cvsuagritech.spim.models.QuestType.COMPLETE_TREATMENT_RECOMMENDATION -> R.drawable.ic_crop_health
                com.cvsuagritech.spim.models.QuestType.UPDATE_FARM_LOG -> R.drawable.ic_crop_health
                com.cvsuagritech.spim.models.QuestType.READ_FARMING_TIPS -> R.drawable.ic_crop_health
                com.cvsuagritech.spim.models.QuestType.SHARE_EXPERIENCE -> R.drawable.ic_crop_health
                else -> R.drawable.ic_crop_health
            }
        }
    }
}