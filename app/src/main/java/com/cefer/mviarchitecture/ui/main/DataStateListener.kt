package com.cefer.mviarchitecture.ui.main

import com.cefer.mviarchitecture.util.DataState


interface DataStateListener {
    
    fun onDataStateChange(dataState : DataState<*>?)
}