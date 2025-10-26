package com.cvsuagritech.spim.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.cvsuagritech.spim.R
import com.cvsuagritech.spim.models.Reward
import com.google.android.material.button.MaterialButton

class RewardAdapter(
    private val onRewardClick: (Reward) -> Unit
) : RecyclerView.Adapter<RewardAdapter.RewardViewHolder>() {

    private var rewards = listOf<Reward>()
    private var userPoints = 0

    fun updateRewards(newRewards: List<Reward>, currentUserPoints: Int) {
        rewards = newRewards
        userPoints = currentUserPoints
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RewardViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_reward, parent, false)
        return RewardViewHolder(view)
    }

    override fun onBindViewHolder(holder: RewardViewHolder, position: Int) {
        holder.bind(rewards[position])
    }

    override fun getItemCount(): Int = rewards.size

    inner class RewardViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val rewardIcon: ImageView = itemView.findViewById(R.id.reward_icon)
        private val rewardName: TextView = itemView.findViewById(R.id.tv_reward_name)
        private val rewardDescription: TextView = itemView.findViewById(R.id.tv_reward_description)
        private val rewardCategory: TextView = itemView.findViewById(R.id.tv_reward_category)
        private val rewardCost: TextView = itemView.findViewById(R.id.tv_reward_cost)
        private val rewardStatus: TextView = itemView.findViewById(R.id.tv_reward_status)
        private val unlockButton: MaterialButton = itemView.findViewById(R.id.btn_unlock_reward)

        fun bind(reward: Reward) {
            rewardName.text = reward.name
            rewardDescription.text = reward.description
            rewardCost.text = reward.pointsCost.toString()
            
            // Set category based on reward type
            rewardCategory.text = getCategoryText(reward.type)
            
            // Set reward icon based on type
            rewardIcon.setImageResource(getRewardIcon(reward.type))
            
            // Update status and button
            val canAfford = userPoints >= reward.pointsCost
            
            if (reward.isUnlocked) {
                rewardStatus.text = "🎉 Unlocked"
                rewardStatus.setTextColor(itemView.context.getColor(R.color.status_success))
                unlockButton.text = "Unlocked"
                unlockButton.isEnabled = false
                unlockButton.backgroundTintList = itemView.context.getColorStateList(R.color.status_success)
                rewardIcon.alpha = 1.0f
            } else if (canAfford) {
                rewardStatus.text = "🔓 Available"
                rewardStatus.setTextColor(itemView.context.getColor(R.color.status_success))
                unlockButton.text = "Unlock"
                unlockButton.isEnabled = true
                unlockButton.backgroundTintList = itemView.context.getColorStateList(R.color.gradient_primary_start)
                rewardIcon.alpha = 1.0f
            } else {
                rewardStatus.text = "💰 Need ${reward.pointsCost - userPoints} more points"
                rewardStatus.setTextColor(itemView.context.getColor(R.color.status_warning))
                unlockButton.text = "Locked"
                unlockButton.isEnabled = false
                unlockButton.backgroundTintList = itemView.context.getColorStateList(R.color.text_modern_secondary)
                rewardIcon.alpha = 0.5f
            }
            
            // Set click listener
            unlockButton.setOnClickListener {
                if (!reward.isUnlocked && canAfford) {
                    onRewardClick(reward)
                }
            }
            
            itemView.setOnClickListener {
                if (!reward.isUnlocked && canAfford) {
                    onRewardClick(reward)
                }
            }
        }
        
        private fun getCategoryText(rewardType: com.cvsuagritech.spim.models.RewardType): String {
            return when (rewardType) {
                com.cvsuagritech.spim.models.RewardType.PEST_IDENTIFICATION_PRO -> "🐛 Pest Control"
                com.cvsuagritech.spim.models.RewardType.ADVANCED_ANALYTICS -> "🌿 Fertilizers"
                com.cvsuagritech.spim.models.RewardType.PREMIUM_SEEDS_CATALOG -> "🌱 Seeds"
                com.cvsuagritech.spim.models.RewardType.SOIL_TESTING_TOOL -> "🌍 Soil Tools"
                com.cvsuagritech.spim.models.RewardType.WEATHER_FORECAST -> "💧 Water Tools"
                com.cvsuagritech.spim.models.RewardType.HARVEST_CALCULATOR -> "📊 Farm Tools"
                com.cvsuagritech.spim.models.RewardType.FARMING_GUIDE -> "📚 Learning"
                com.cvsuagritech.spim.models.RewardType.EXPERT_CONSULTATION -> "👨‍🌾 Premium"
                com.cvsuagritech.spim.models.RewardType.FARMER_NETWORK_ACCESS -> "🤝 Community"
                com.cvsuagritech.spim.models.RewardType.BADGE -> "🏆 Achievements"
                else -> "🎁 Rewards"
            }
        }
        
        private fun getRewardIcon(rewardType: com.cvsuagritech.spim.models.RewardType): Int {
            return when (rewardType) {
                com.cvsuagritech.spim.models.RewardType.PEST_IDENTIFICATION_PRO -> R.drawable.ic_crop_health
                com.cvsuagritech.spim.models.RewardType.ADVANCED_ANALYTICS -> R.drawable.ic_fertilizer
                com.cvsuagritech.spim.models.RewardType.PREMIUM_SEEDS_CATALOG -> R.drawable.ic_fertilizer
                com.cvsuagritech.spim.models.RewardType.SOIL_TESTING_TOOL -> R.drawable.ic_fertilizer
                com.cvsuagritech.spim.models.RewardType.WEATHER_FORECAST -> R.drawable.ic_fertilizer
                com.cvsuagritech.spim.models.RewardType.HARVEST_CALCULATOR -> R.drawable.ic_crop_health
                com.cvsuagritech.spim.models.RewardType.FARMING_GUIDE -> R.drawable.ic_crop_health
                com.cvsuagritech.spim.models.RewardType.EXPERT_CONSULTATION -> R.drawable.ic_crop_health
                com.cvsuagritech.spim.models.RewardType.FARMER_NETWORK_ACCESS -> R.drawable.ic_crop_health
                com.cvsuagritech.spim.models.RewardType.BADGE -> R.drawable.ic_crop_health
                else -> R.drawable.ic_crop_health
            }
        }
    }
}