package com.cvsuagritech.spim

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.cvsuagritech.spim.databinding.ActivityMainNavBinding
import com.cvsuagritech.spim.fragments.LogbookFragment
import com.cvsuagritech.spim.fragments.HomeFragment
import com.cvsuagritech.spim.fragments.RecommendationsFragment
import com.cvsuagritech.spim.fragments.SettingsFragment
import com.cvsuagritech.spim.utils.LanguageManager
import com.cvsuagritech.spim.utils.ThemeManager
import com.cvsuagritech.spim.utils.DemoDataGenerator

class MainNavActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainNavBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        
        // Apply saved language preference
        val savedLanguage = LanguageManager.getCurrentLanguage(this)
        LanguageManager.setLanguage(this, savedLanguage)
        
        // Apply saved theme preference
        ThemeManager.initializeTheme(this)
        
        binding = ActivityMainNavBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setupBottomNavigation()
        
        // Initialize demo data
        DemoDataGenerator.initializeDemoDataIfNeeded(this)
        
        // Set default fragment
        if (savedInstanceState == null) {
            supportFragmentManager.beginTransaction()
                .replace(R.id.nav_host_fragment, HomeFragment())
                .commit()
        }
    }

    private fun setupBottomNavigation() {
        binding.bottomNavigation.setOnItemSelectedListener { item ->
            when (item.itemId) {
                R.id.nav_home -> {
                    replaceFragment(HomeFragment())
                    true
                }
                R.id.nav_logbook -> {
                    replaceFragment(LogbookFragment())
                    true
                }
                R.id.nav_recommendations -> {
                    replaceFragment(RecommendationsFragment())
                    true
                }
                R.id.nav_settings -> {
                    replaceFragment(SettingsFragment())
                    true
                }
                else -> false
            }
        }
    }

    private fun replaceFragment(fragment: Fragment) {
        supportFragmentManager.beginTransaction()
            .replace(R.id.nav_host_fragment, fragment)
            .commit()
    }
}
