package com.cefer.mviarchitecture.ui.main

import android.os.Bundle
import android.view.View.GONE
import android.view.View.VISIBLE
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import com.cefer.mviarchitecture.databinding.ActivityMainBinding
import com.cefer.mviarchitecture.util.DataState

class MainActivity : AppCompatActivity() , DataStateListener {
    
    private lateinit var viewModel : MainViewModel
    
    private lateinit var binding : ActivityMainBinding
    
    override fun onCreate(savedInstanceState : Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        
        // ViewModel
        viewModel = ViewModelProvider(this).get(MainViewModel::class.java)
        
        showMainFragment()
        
    }
    
    private fun showMainFragment() {
        supportFragmentManager.beginTransaction()
                .replace(binding.fragmentContainer.id , MainFragment() , "MainFragment").commit()
    }
    
    override fun onDataStateChange(dataState : DataState<*>?) {
        handleDataStateChange(dataState)
    }
    
    private fun handleDataStateChange(dataState : DataState<*>?) {
        dataState?.let { dataStateStarProjection ->
            
            // handle loading
            showProgressBar(dataStateStarProjection.loading)
            println("Trace ${dataStateStarProjection.loading} in MainActivity")
            
            // handle message
            dataStateStarProjection.message?.let { event ->
                event.getContentIfNotHandled()?.let { message -> showToast(message) }
            }
        }
    }
    
    private fun showProgressBar(isVisible : Boolean) {
        if (isVisible) {
            binding.progressBar.visibility = VISIBLE
        }
        else {
            binding.progressBar.visibility = GONE
        }
    }
    
    private fun showToast(message : String) {
        Toast.makeText(this , message , Toast.LENGTH_SHORT).show()
    }
}