package com.cvsuagritech.spim.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.cvsuagritech.spim.R
import com.cvsuagritech.spim.adapters.PostAdapter
import com.cvsuagritech.spim.databinding.FragmentCommunityBinding
import com.cvsuagritech.spim.models.CommunityPost
import com.cvsuagritech.spim.models.PostCategory
import com.cvsuagritech.spim.utils.CommunityDataGenerator
import java.util.*

class CommunityFragment : Fragment() {

    private var _binding: FragmentCommunityBinding? = null
    private val binding get() = _binding!!

    private lateinit var postAdapter: PostAdapter
    private var allPosts = listOf<CommunityPost>()
    private var currentFilter = PostCategory.GENERAL

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentCommunityBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupRecyclerView()
        setupClickListeners()
        setupFilterChips()
        loadDummyPosts()
    }

    private fun setupRecyclerView() {
        postAdapter = PostAdapter { post ->
            // Handle post click - could navigate to detailed view
            Toast.makeText(requireContext(), "Opening post: ${post.title}", Toast.LENGTH_SHORT).show()
        }

        binding.recyclerViewPosts.apply {
            layoutManager = LinearLayoutManager(requireContext())
            adapter = postAdapter
        }
    }

    private fun setupClickListeners() {
        // Handle back button in toolbar
        binding.toolbar.setNavigationOnClickListener {
            requireActivity().onBackPressed()
        }

        // Create post FAB
        binding.fabCreatePost.setOnClickListener {
            Toast.makeText(requireContext(), "Create new post", Toast.LENGTH_SHORT).show()
        }
    }

    private fun setupFilterChips() {
        binding.filterChips.setOnCheckedStateChangeListener { group, checkedIds ->
            when (checkedIds.firstOrNull()) {
                R.id.chip_all_posts -> {
                    currentFilter = PostCategory.GENERAL
                    filterPosts()
                }
                R.id.chip_rice_farming -> {
                    currentFilter = PostCategory.RICE_FARMING
                    filterPosts()
                }
                R.id.chip_questions -> {
                    currentFilter = PostCategory.QUESTION
                    filterPosts()
                }
                R.id.chip_tips -> {
                    currentFilter = PostCategory.TIP
                    filterPosts()
                }
                R.id.chip_success -> {
                    currentFilter = PostCategory.SUCCESS_STORY
                    filterPosts()
                }
            }
        }
    }

    private fun loadDummyPosts() {
        allPosts = CommunityDataGenerator.generateDummyPosts()
        filterPosts()
    }

    private fun filterPosts() {
        val filteredPosts = if (currentFilter == PostCategory.GENERAL) {
            allPosts
        } else {
            allPosts.filter { it.category == currentFilter }
        }

        postAdapter.updatePosts(filteredPosts)
        
        // Show/hide empty state
        binding.tvNoPosts.visibility = if (filteredPosts.isEmpty()) View.VISIBLE else View.GONE
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
