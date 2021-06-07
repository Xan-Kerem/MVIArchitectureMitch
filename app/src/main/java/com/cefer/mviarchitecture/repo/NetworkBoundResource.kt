package com.cefer.mviarchitecture.repo

import androidx.lifecycle.LiveData
import androidx.lifecycle.MediatorLiveData
import com.cefer.mviarchitecture.util.*
import kotlinx.coroutines.Dispatchers.IO
import kotlinx.coroutines.Dispatchers.Main
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

abstract class NetworkBoundResource<ResponseObject , ViewStateType> {
    
    protected val result = MediatorLiveData<DataState<ViewStateType>>()
    
    init {
        result.value = DataState.loading(true)
        println("Trace : result init in networkbound")
        
        GlobalScope.launch(IO) {
            println("Trace : IO in networkbound")
            delay(1000)
            
            withContext(Main) {
                println("Trace : Main in networkbound")
                
                val apiResponse = createCall()
                
                result.addSource(apiResponse) { genericApiResponse ->
                    println("Trace : BeforeRemoveResource ${result.value!!.loading} in networkbound")
                    result.removeSource(apiResponse)
                    println("Trace : removeResource in networkbound")
                    println("Trace : removeResource ${result.value!!.loading} in networkbound")
                    
                    handleNetworkCall(genericApiResponse)
                }
            }
        }
        
    }
    
    private fun handleNetworkCall(response : GenericApiResponse<ResponseObject>) {
        println("Trace : handleNetworkCall in networkbound")
        when (response) {
            is ApiSuccessResponse -> {
                handleApiSuccessResponse(response)
            }
            
            is ApiErrorResponse   -> {
                println("DEBUG: NetworkBoundResource: ${response.errorMessage}")
                onReturnError(response.errorMessage)
            }
            
            is ApiEmptyResponse   -> {
                println("DEBUG: NetworkBoundResource: Request returned NOTHING (HTTP 204)")
                onReturnError("HTTP 204. Returned NOTHING.")
            }
        }
    }
    
    private fun onReturnError(errorMessage : String) {
        result.value = DataState.error(errorMessage)
    }
    
    abstract fun handleApiSuccessResponse(response : ApiSuccessResponse<ResponseObject>)
    
    abstract fun createCall() : LiveData<GenericApiResponse<ResponseObject>>
    
    fun asLiveData() : LiveData<DataState<ViewStateType>> {
        println("Trace : asLiveData in networkbound")
        return result
    }
}