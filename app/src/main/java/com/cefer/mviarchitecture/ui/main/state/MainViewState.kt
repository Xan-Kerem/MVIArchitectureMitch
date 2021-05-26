package com.cefer.mviarchitecture.ui.main.state

import com.cefer.mviarchitecture.model.BlogPost
import com.cefer.mviarchitecture.model.User

data class MainViewState(

    var blogPosts: List<BlogPost>? = null,
    var user: User? = null

)