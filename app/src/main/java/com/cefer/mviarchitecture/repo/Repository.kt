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
                println("Trace : handleApiSuccessResponse in getBlogPosts")
                result.value =
                        DataState.data(
                                message = null ,
                                MainViewState(blogPosts = response.body , user = null)
                        )
                println("Trace : handleApiSuccessResponse ${result.value!!.loading} in getBlogPosts")
            }
            
            override fun createCall() : LiveData<GenericApiResponse<List<BlogPost>>> {
                println("Trace : createCall in getBlogPosts")
                return RetrofitBuilder.apiService.getBlogPosts()
            }
            
        }.asLiveData()
    }
}