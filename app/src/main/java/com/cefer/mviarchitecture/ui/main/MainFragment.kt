package com.cefer.mviarchitecture.ui.main

import android.os.Bundle
import android.view.*
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.cefer.mviarchitecture.R
import com.cefer.mviarchitecture.databinding.FragmentMainBinding
import com.cefer.mviarchitecture.ui.main.state.MainStateEvent.GetBlogPostsEvent
import com.cefer.mviarchitecture.ui.main.state.MainStateEvent.GetUserEvent

class MainFragment : Fragment() {
    
    private lateinit var viewModel : MainViewModel
    
    private lateinit var binding : FragmentMainBinding
    
    override fun onCreateView(
            inflater : LayoutInflater , container : ViewGroup? ,
            savedInstanceState : Bundle?
    ) : View {
        // Inflate the layout for this fragment
        binding = FragmentMainBinding.inflate(inflater , container , false)
        return binding.root
    }
    
    override fun onViewCreated(view : View , savedInstanceState : Bundle?) {
        super.onViewCreated(view , savedInstanceState)
        setHasOptionsMenu(true)
        
        viewModel = activity?.run {
            ViewModelProvider(this).get(MainViewModel::class.java)
        } ?: throw Exception("Invalid Activity")
        
        subscribeObservers()
    }
    
    private fun subscribeObservers() {
        
        // handle Data<T>
        viewModel.dataState.observe(viewLifecycleOwner , { dataState ->
            
            println("DEBUG: DataState: $dataState")
            // handle Data<T>
            dataState.data?.let { mainViewState ->
                
                mainViewState.blogPosts?.let {
                    // set BlogPosts data
                    viewModel.setBlogListData(it)
                }
                
                mainViewState.user?.let {
                    // set User data
                    viewModel.setUser(it)
                }
            }
            
            
            // handle Error
            dataState.message?.let {
            
            }
            
            // handle Loading
            dataState.loading.let {
            
            }
        }
        )
        
        viewModel.viewState.observe(viewLifecycleOwner , { viewState ->
            viewState.blogPosts?.let {
                // set BlogPosts to RecyclerView
                println("DEBUG: Setting blog posts to RecyclerView: ${viewState.blogPosts}")
            }
            
            viewState.user?.let {
                // set User data to widgets
                println("DEBUG: Setting User data: ${viewState.user}")
            }
        })
    }
    
    private fun triggerGetUserEvent() {
        viewModel.setStateEvent(GetUserEvent("1"))
    }
    
    private fun triggerGetBlogsEvent() {
        viewModel.setStateEvent(GetBlogPostsEvent)
    }
    
    override fun onCreateOptionsMenu(menu : Menu , inflater : MenuInflater) {
        super.onCreateOptionsMenu(menu , inflater)
        inflater.inflate(R.menu.main_menu , menu)
    }
    
    override fun onOptionsItemSelected(item : MenuItem) : Boolean {
        when (item.itemId) {
            R.id.action_get_blogs -> triggerGetBlogsEvent()
            
            R.id.action_get_user  -> triggerGetUserEvent()
        }
        
        return super.onOptionsItemSelected(item)
    }
}














