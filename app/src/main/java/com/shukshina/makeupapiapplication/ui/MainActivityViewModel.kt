package com.shukshina.makeupapiapplication.ui

import android.content.Context
import android.net.ConnectivityManager
import android.net.Network
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.shukshina.makeupapiapplication.domain.MakeUpApiRepository
import com.shukshina.makeupapiapplication.response.ProductsList
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import javax.inject.Inject

@HiltViewModel
class MainActivityViewModel @Inject constructor(
    @ApplicationContext context: Context,
    private val repository: MakeUpApiRepository
) : ViewModel() {

    private val _productsList = MutableStateFlow<ProductsList?>(null)
    val productsList = _productsList.asStateFlow()

    private val _internetConnectionState = MutableStateFlow(false)
    val internetConnectionState = _internetConnectionState.asStateFlow()

    private val _uiState = MutableStateFlow(ProductDetailUIState())
    val uiState: StateFlow<ProductDetailUIState> = _uiState.asStateFlow()

    init {
        viewModelScope.launch {
            val connectivityManager =
                context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
            val networkCallback = object : ConnectivityManager.NetworkCallback() {

                override fun onAvailable(network: Network) {
                    _internetConnectionState.value = true
                    getProductByBrandAndProductType("maybelline",
                        "lipstick")
                }

                override fun onLost(network: Network) {
                    _internetConnectionState.value = false
                }
            }
            connectivityManager.registerDefaultNetworkCallback(networkCallback)
        }
    }

    fun setUIState(imageLink: String, name: String, description: String) {
        _uiState.update { currentState ->
            currentState.copy(
                imageLink = imageLink,
                productName = name,
                productDescription = description,
            )
        }
    }

    fun getAllProducts() {
        _productsList.value = null
        viewModelScope.launch {
            val call: Call<ProductsList> = repository.getAllProducts()
            call.enqueue(object : Callback<ProductsList> {
                override fun onResponse(call: Call<ProductsList>, response: Response<ProductsList>) {
                    if (response.body() == null) {
                        _productsList.value = ProductsList()
                        return
                    }
                    _productsList.value = response.body()
                }

                override fun onFailure(call: Call<ProductsList>, t: Throwable) {
                    t.printStackTrace()
                }
            })
        }
    }

    fun getProductByBrandAndProductType(brand: String, productType: String) {
        _productsList.value = null
        viewModelScope.launch {
            val call: Call<ProductsList> = repository.getProductByBrandAndProductType(brand, productType)
            call.enqueue(object : Callback<ProductsList> {
                override fun onResponse(call: Call<ProductsList>, response: Response<ProductsList>) {
                    if (response.body() == null) {
                        _productsList.value = ProductsList()
                        return
                    }
                    _productsList.value = response.body()
                }

                override fun onFailure(call: Call<ProductsList>, t: Throwable) {
                    t.printStackTrace()
                }
            })
        }
    }

    fun getProductsByProductType(productType: String) {
        _productsList.value = null
        viewModelScope.launch {
            val call: Call<ProductsList> = repository.getProductsByProductType(productType)
            call.enqueue(object : Callback<ProductsList> {
                override fun onResponse(call: Call<ProductsList>, response: Response<ProductsList>) {
                    if (response.body() == null) {
                        _productsList.value = ProductsList()
                        return
                    }
                    _productsList.value = response.body()
                }

                override fun onFailure(call: Call<ProductsList>, t: Throwable) {
                    t.printStackTrace()
                }
            })
        }
    }
}