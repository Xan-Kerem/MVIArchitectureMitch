package com.cefer.mviarchitecture.api

import androidx.lifecycle.LiveData
import com.cefer.mviarchitecture.model.BlogPost
import com.cefer.mviarchitecture.util.GenericApiResponse
import retrofit2.http.GET

interface ApiService {
    
    @GET("placeholder/blogs")
    fun getBlogPosts() : LiveData<GenericApiResponse<List<BlogPost>>>
    
}