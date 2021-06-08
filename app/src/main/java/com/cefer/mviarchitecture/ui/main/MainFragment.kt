package com.cefer.mviarchitecture.ui.main

import android.content.Context
import android.os.Bundle
import android.view.*
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.bumptech.glide.Glide
import com.cefer.mviarchitecture.R
import com.cefer.mviarchitecture.databinding.FragmentMainBinding
import com.cefer.mviarchitecture.model.BlogPost
import com.cefer.mviarchitecture.model.User
import com.cefer.mviarchitecture.ui.main.state.MainStateEvent.GetBlogPostsEvent
import com.cefer.mviarchitecture.ui.main.state.MainStateEvent.GetUserEvent
import com.cefer.mviarchitecture.ui.main.state.MainViewState
import com.cefer.mviarchitecture.util.TopSpacingItemDecoration

class MainFragment : Fragment() , BlogListAdapter.Interaction {
    
    override fun onItemSelected(position : Int , item : BlogPost) {
        println("DEBUG: CLICKED $position")
        println("DEBUG: CLICKED $item")
    }
    
    private lateinit var viewModel : MainViewModel
    
    private lateinit var binding : FragmentMainBinding
    
    private lateinit var dataStateHandler : DataStateListener
    
    private lateinit var blogListAdapter : BlogListAdapter
    
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
        
        initRecyclerView()
    }
    
    private fun setUserProperties(user : User){
        binding.apply {
            email.text = user.email
            username.text = user.username
            view?.let {
                Glide.with(it.context)
                        .load(user.image)
                        .into(image)
            }
        }
    }
    
    private fun initRecyclerView() {
        binding.recyclerView.apply {
            layoutManager = LinearLayoutManager(activity)
            val topSpacingItemDecoration = TopSpacingItemDecoration(30)
            addItemDecoration(topSpacingItemDecoration)
            blogListAdapter = BlogListAdapter(this@MainFragment)
            adapter = blogListAdapter
        }
    }
    
    private fun subscribeObservers() {
        
        // handle Data<T>
        viewModel.dataState.observe(viewLifecycleOwner , { dataState ->
            
            println("DEBUG: DataState: $dataState")
            
            // handle loading and message
            dataStateHandler.onDataStateChange(dataState)
            
            // handle Data<T>
            dataState.data?.let { event ->
                event.getContentIfNotHandled()?.let { mainViewState ->
                    mainViewState.blogPosts?.let {
                        // set BlogPosts data
                        viewModel.setBlogListData(it)
                    }
                    mainViewState.user?.let {
                        // set User data
                        viewModel.setUser(it)
                    }
                }
                
                
            }
            
        }
        )
        
        viewModel.viewState.observe(viewLifecycleOwner , object : Observer<MainViewState> {
            
            override fun onChanged(viewState : MainViewState?) {
                viewState?.blogPosts?.let {
                    // set BlogPosts to RecyclerView
                    println("DEBUG: Setting blog posts to RecyclerView: ${viewState.blogPosts}")
                    blogListAdapter.submitList(it)
                }
                
                
                viewState?.user?.let {
                    // set User data to widgets
                    println("DEBUG: Setting User data: ${viewState.user}")
                    setUserProperties(it)
                }
                
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
    
    override fun onAttach(context : Context) {
        super.onAttach(context)
        
        try {
            
            dataStateHandler = context as DataStateListener
            
        } catch (e : ClassCastException) {
            println("DEBUG: $context must implement DataStateListener")
        }
    }
    
    
}














