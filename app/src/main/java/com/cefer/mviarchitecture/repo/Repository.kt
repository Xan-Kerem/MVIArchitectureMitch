package com.cefer.mviarchitecture.repo

import androidx.lifecycle.LiveData
import com.cefer.mviarchitecture.api.RetrofitBuilder
import com.cefer.mviarchitecture.model.BlogPost
import com.cefer.mviarchitecture.ui.main.state.MainViewState
import com.cefer.mviarchitecture.util.ApiSuccessResponse
import com.cefer.mviarchitecture.util.DataState
import com.cefer.mviarchitecture.util.GenericApiResponse

object Repository {
    
    fun getBlogPosts() : LiveData<DataState<MainViewState>> {
        
        return object : NetworkBoundResource<List<BlogPost> , MainViewState>() {
            
            override fun handleApiSuccessResponse(response : ApiSuccessResponse<List<BlogPost>>) {
                result.value =
                        DataState.data(null , MainViewState(blogPosts = response.body , null))
            }
            
            override fun createCall() : LiveData<GenericApiResponse<List<BlogPost>>> {
                
                return RetrofitBuilder.apiService.getBlogPosts()
            }
            
        }.asLiveData()
    }
}