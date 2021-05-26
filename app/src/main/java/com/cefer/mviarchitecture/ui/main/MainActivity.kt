package com.cefer.mviarchitecture.ui.main

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import com.cefer.mviarchitecture.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {

    private lateinit var viewModel: MainViewModel

    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // ViewModel
        viewModel = ViewModelProvider(this).get(MainViewModel::class.java)

        showMainFragment()

    }

    private fun showMainFragment() {
        supportFragmentManager.beginTransaction()
            .replace(binding.fragmentContainer.id, MainFragment(), "MainFragment").commit()
    }
}