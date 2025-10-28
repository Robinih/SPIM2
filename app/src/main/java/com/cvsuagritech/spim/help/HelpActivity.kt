package com.cvsuagritech.spim.help

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.cvsuagritech.spim.databinding.ActivityHelpBinding

class HelpActivity : AppCompatActivity() {

    private lateinit var binding: ActivityHelpBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityHelpBinding.inflate(layoutInflater)
        setContentView(binding.root)

        supportActionBar?.title = getString(com.cvsuagritech.spim.R.string.help_center_title)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        binding.tvHelpContent.text = getString(com.cvsuagritech.spim.R.string.help_center_content)
    }

    override fun onSupportNavigateUp(): Boolean {
        finish()
        return true
    }
}


