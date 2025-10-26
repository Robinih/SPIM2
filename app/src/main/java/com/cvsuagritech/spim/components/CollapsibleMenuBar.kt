package com.cvsuagritech.spim.components

import android.animation.ObjectAnimator
import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.View
import android.widget.LinearLayout
import androidx.fragment.app.FragmentManager
import com.cvsuagritech.spim.R
import com.cvsuagritech.spim.databinding.CollapsibleMenuBarBinding
import com.cvsuagritech.spim.fragments.MapboxMapFragment
import com.cvsuagritech.spim.fragments.CommunityFragment

class CollapsibleMenuBar @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : LinearLayout(context, attrs, defStyleAttr) {

    private val binding: CollapsibleMenuBarBinding
    private var isExpanded = false
    private var fragmentManager: FragmentManager? = null

    init {
        binding = CollapsibleMenuBarBinding.inflate(LayoutInflater.from(context), this, true)
        setupClickListeners()
    }

    fun setFragmentManager(fragmentManager: FragmentManager) {
        this.fragmentManager = fragmentManager
    }

    private fun setupClickListeners() {
        binding.menuHeader.setOnClickListener {
            toggleExpansion()
        }

        binding.btnOpenMap.setOnClickListener {
            openMapFragment()
        }

        binding.btnOpenCommunity.setOnClickListener {
            openCommunityFragment()
        }
    }

    private fun toggleExpansion() {
        isExpanded = !isExpanded
        
        if (isExpanded) {
            expandMenu()
        } else {
            collapseMenu()
        }
    }

    private fun expandMenu() {
        binding.menuContent.visibility = View.VISIBLE
        binding.expandCollapseIcon.rotation = 180f
        
        // Animate the expansion
        val animator = ObjectAnimator.ofFloat(binding.menuContent, "alpha", 0f, 1f)
        animator.duration = 300
        animator.start()
    }

    private fun collapseMenu() {
        binding.expandCollapseIcon.rotation = 0f
        
        // Animate the collapse
        val animator = ObjectAnimator.ofFloat(binding.menuContent, "alpha", 1f, 0f)
        animator.duration = 300
        animator.start()
        
        // Hide after animation
        binding.menuContent.postDelayed({
            binding.menuContent.visibility = View.GONE
        }, 300)
    }

    private fun openMapFragment() {
        fragmentManager?.let { fm ->
            val mapFragment = MapboxMapFragment()
            fm.beginTransaction()
                .replace(R.id.nav_host_fragment, mapFragment)
                .addToBackStack(null)
                .commit()
        }
    }

    private fun openCommunityFragment() {
        fragmentManager?.let { fm ->
            val communityFragment = CommunityFragment()
            fm.beginTransaction()
                .replace(R.id.nav_host_fragment, communityFragment)
                .addToBackStack(null)
                .commit()
        }
    }
}
