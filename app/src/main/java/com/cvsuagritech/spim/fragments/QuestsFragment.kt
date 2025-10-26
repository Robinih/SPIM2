package com.cvsuagritech.spim.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.cvsuagritech.spim.R
import com.cvsuagritech.spim.adapters.QuestAdapter
import com.cvsuagritech.spim.adapters.RewardAdapter
import com.cvsuagritech.spim.databinding.FragmentQuestsBinding
import com.cvsuagritech.spim.models.Quest
import com.cvsuagritech.spim.models.Reward
import com.cvsuagritech.spim.models.UserProfile
import com.cvsuagritech.spim.utils.GamificationManager

class QuestsFragment : Fragment() {

    private var _binding: FragmentQuestsBinding? = null
    private val binding get() = _binding!!
    
    private lateinit var gamificationManager: GamificationManager
    private lateinit var questAdapter: QuestAdapter
    private lateinit var rewardAdapter: RewardAdapter

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentQuestsBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        gamificationManager = GamificationManager(requireContext())
        
        // Initialize user if not exists
        gamificationManager.initializeUser("user_001", "Farmer")
        
        setupRecyclerViews()
        setupTabListener()
        updateUserProfile()
        loadQuests()
        loadRewards()
    }

    private fun setupRecyclerViews() {
        // Setup quests RecyclerView
        questAdapter = QuestAdapter { quest ->
            if (!quest.isCompleted) {
                gamificationManager.completeQuest(quest.id)
                showQuestCompletionAnimation(quest)
                loadQuests()
                updateUserProfile()
            }
        }
        
        binding.recyclerViewQuests.apply {
            layoutManager = LinearLayoutManager(requireContext())
            adapter = questAdapter
        }

        // Setup rewards RecyclerView
        rewardAdapter = RewardAdapter { reward ->
            val success = gamificationManager.unlockReward(reward.id)
            if (success) {
                showRewardUnlockAnimation(reward)
                loadRewards()
                updateUserProfile()
            } else {
                Toast.makeText(requireContext(), "Not enough points! Need ${reward.pointsCost} points", Toast.LENGTH_SHORT).show()
            }
        }
        
        binding.recyclerViewRewards.apply {
            layoutManager = LinearLayoutManager(requireContext())
            adapter = rewardAdapter
        }
    }

    private fun setupTabListener() {
        binding.tabLayout.addOnTabSelectedListener(object : com.google.android.material.tabs.TabLayout.OnTabSelectedListener {
            override fun onTabSelected(tab: com.google.android.material.tabs.TabLayout.Tab?) {
                when (tab?.position) {
                    0 -> showQuestsSection()
                    1 -> showRewardsSection()
                }
            }

            override fun onTabUnselected(tab: com.google.android.material.tabs.TabLayout.Tab?) {}
            override fun onTabReselected(tab: com.google.android.material.tabs.TabLayout.Tab?) {}
        })
    }

    private fun showQuestsSection() {
        binding.questsSection.visibility = View.VISIBLE
        binding.rewardsSection.visibility = View.GONE
    }

    private fun showRewardsSection() {
        binding.questsSection.visibility = View.GONE
        binding.rewardsSection.visibility = View.VISIBLE
    }

    private fun updateUserProfile() {
        val profile = gamificationManager.getUserProfile()
        profile?.let {
            binding.tvUsername.text = it.username
            binding.tvLevel.text = "Level ${it.level}"
            binding.tvPoints.text = "${it.totalPoints} points"
            binding.tvStreak.text = "${it.streakDays} day streak"
            binding.progressBarLevel.progress = (it.experience % 100)
        }
    }

    private fun loadQuests() {
        val quests = gamificationManager.getDailyQuests()
        questAdapter.updateQuests(quests)
        
        if (quests.isEmpty()) {
            binding.tvNoQuests.visibility = View.VISIBLE
            binding.recyclerViewQuests.visibility = View.GONE
        } else {
            binding.tvNoQuests.visibility = View.GONE
            binding.recyclerViewQuests.visibility = View.VISIBLE
        }
    }

    private fun loadRewards() {
        val rewards = gamificationManager.getRewards()
        val userProfile = gamificationManager.getUserProfile()
        val userPoints = userProfile?.totalPoints ?: 0
        
        rewardAdapter.updateRewards(rewards, userPoints)
        
        if (rewards.isEmpty()) {
            binding.tvNoRewards.visibility = View.VISIBLE
            binding.recyclerViewRewards.visibility = View.GONE
        } else {
            binding.tvNoRewards.visibility = View.GONE
            binding.recyclerViewRewards.visibility = View.VISIBLE
        }
    }

    private fun showQuestCompletionAnimation(quest: Quest) {
        Toast.makeText(requireContext(), "🎉 Quest completed! +${quest.points} points", Toast.LENGTH_LONG).show()
        
        // Add haptic feedback
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
            val vibrator = requireContext().getSystemService(android.content.Context.VIBRATOR_SERVICE) as android.os.Vibrator
            vibrator.vibrate(android.os.VibrationEffect.createOneShot(100, android.os.VibrationEffect.DEFAULT_AMPLITUDE))
        }
    }

    private fun showRewardUnlockAnimation(reward: Reward) {
        Toast.makeText(requireContext(), "🎁 Reward unlocked: ${reward.name}!", Toast.LENGTH_LONG).show()
        
        // Add haptic feedback
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
            val vibrator = requireContext().getSystemService(android.content.Context.VIBRATOR_SERVICE) as android.os.Vibrator
            vibrator.vibrate(android.os.VibrationEffect.createOneShot(200, android.os.VibrationEffect.DEFAULT_AMPLITUDE))
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
