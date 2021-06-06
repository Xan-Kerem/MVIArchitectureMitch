package com.cefer.mviarchitecture.ui.main

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Transformations
import androidx.lifecycle.ViewModel
import com.cefer.mviarchitecture.model.BlogPost
import com.cefer.mviarchitecture.model.User
import com.cefer.mviarchitecture.repo.Repository
import com.cefer.mviarchitecture.ui.main.state.MainStateEvent
import com.cefer.mviarchitecture.ui.main.state.MainStateEvent.*
import com.cefer.mviarchitecture.ui.main.state.MainViewState
import com.cefer.mviarchitecture.util.AbsentLiveData
import com.cefer.mviarchitecture.util.DataState


class MainViewModel : ViewModel() {

    private val _stateEvent: MutableLiveData<MainStateEvent> = MutableLiveData()
    private val _viewState: MutableLiveData<MainViewState> = MutableLiveData()

    val viewState: LiveData<MainViewState>
        get() = _viewState


    val dataState: LiveData<DataState<MainViewState>> = Transformations
        .switchMap(_stateEvent) { stateEvent ->
            stateEvent?.let {
                handleStateEvent(stateEvent)
            }
        }

    private fun handleStateEvent(stateEvent: MainStateEvent): LiveData<DataState<MainViewState>> {
        println("DEBUG: New StateEvent detected: $stateEvent")
        when (stateEvent) {

            is GetBlogPostsEvent -> {
                return Repository.getBlogPosts()
            }

            is GetUserEvent -> {
                return object : LiveData<DataState<MainViewState>>() {
                    override fun onActive() {
                        super.onActive()
                        val user = User(
                            email = "Cefer@gmail.com",
                            username = "Cefer",
                            image = "https://cdn.open-api.xyz/open-api-static/static-random-images/logo_1080_1080.png"
                        )
                        value = DataState.data(data = MainViewState(
                            user = user
                        ))
                    }
                }
            }

            is None -> {
                return AbsentLiveData.create()
            }
        }
    }

    fun setBlogListData(blogPosts: List<BlogPost>) {
        val update = getCurrentViewStateOrNew()
        update.blogPosts = blogPosts
        _viewState.value = update
    }

    fun setUser(user: User) {
        val update = getCurrentViewStateOrNew()
        update.user = user
        _viewState.value = update
    }

    private fun getCurrentViewStateOrNew(): MainViewState {
        return viewState.value ?: MainViewState()
    }

    fun setStateEvent(event: MainStateEvent) {
        _stateEvent.value = event
    }
}













