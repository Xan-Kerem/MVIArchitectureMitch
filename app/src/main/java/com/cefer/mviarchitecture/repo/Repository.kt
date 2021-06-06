package com.cefer.mviarchitecture.repo

import androidx.lifecycle.LiveData
import androidx.lifecycle.Transformations
import com.cefer.mviarchitecture.api.RetrofitBuilder
import com.cefer.mviarchitecture.ui.main.state.MainViewState
import com.cefer.mviarchitecture.util.ApiEmptyResponse
import com.cefer.mviarchitecture.util.ApiErrorResponse
import com.cefer.mviarchitecture.util.ApiSuccessResponse
import com.cefer.mviarchitecture.util.DataState

object Repository {
    
    fun getBlogPosts() : LiveData<DataState<MainViewState>> {
        return Transformations.switchMap(RetrofitBuilder.apiService.getBlogPosts()) { apiResponse ->
            object : LiveData<DataState<MainViewState>>() {
                override fun onActive() {
                    super.onActive()
                    value = when (apiResponse) {
                        is ApiSuccessResponse -> {
                            DataState.data(data = MainViewState(blogPosts = apiResponse.body))
                        }
                        
                        is ApiErrorResponse   -> {
                            DataState.error(message = apiResponse.errorMessage)
                        }
                        
                        is ApiEmptyResponse   -> {
                            DataState.error(message = "HTTP 204! in repo")
                        }
                    }
                }
                
            }
        }
    }
}