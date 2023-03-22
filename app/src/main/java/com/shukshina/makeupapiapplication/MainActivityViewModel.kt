package com.shukshina.makeupapiapplication

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.shukshina.makeupapiapplication.domain.MakeUpApiRepository
import com.shukshina.makeupapiapplication.response.ProductsList
import com.shukshina.makeupapiapplication.response.ProductsListItem
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import javax.inject.Inject

@HiltViewModel
class MainActivityViewModel @Inject constructor(private val repository: MakeUpApiRepository): ViewModel() {

    private val _allProductsList = MutableLiveData<ProductsList>()
    val allProductsList = _allProductsList
    private val _productsByBrandList = MutableLiveData<ProductsList>()
    val productsByBrandList = _productsByBrandList
    private val _productsByBrandAndProductTypeList = MutableLiveData<ProductsList>()
    val productsByBrandAndProductTypeList = _productsByBrandAndProductTypeList
    private val _productById = MutableLiveData<ProductsListItem>()
    val productById = _productById

    fun getAllProducts() {
        viewModelScope.launch {
            val call: Call<ProductsList> = repository.getAllProducts()
            call.enqueue(object: Callback<ProductsList>{
                override fun onResponse(call: Call<ProductsList>, response: Response<ProductsList>) {
                        _allProductsList.postValue(response.body()!!)
                }

                override fun onFailure(call: Call<ProductsList>, t: Throwable) {
                    t.printStackTrace()
                }
            })
        }
    }

    fun getProductsByBrand(brand: String) {
        viewModelScope.launch {
            val call: Call<ProductsList> = repository.getProductsByBrand(brand)
            call.enqueue(object: Callback<ProductsList>{
                override fun onResponse(call: Call<ProductsList>, response: Response<ProductsList>) {
                    _productsByBrandList.postValue(response.body()!!)
                }

                override fun onFailure(call: Call<ProductsList>, t: Throwable) {
                    t.printStackTrace()
                }
            })
        }
    }

    fun getProductByBrandAndProductType(brand: String, productType: String) {
        viewModelScope.launch {
            val call: Call<ProductsList> = repository.getProductByBrandAndProductType(brand, productType)
            call.enqueue(object: Callback<ProductsList>{
                override fun onResponse(call: Call<ProductsList>, response: Response<ProductsList>) {
                    _productsByBrandAndProductTypeList.postValue(response.body()!!)
                }

                override fun onFailure(call: Call<ProductsList>, t: Throwable) {
                    t.printStackTrace()
                }
            })
        }
    }

    fun getProductById(productId: String) {
        viewModelScope.launch {
            val call: Call<ProductsListItem> = repository.getProductById(productId)
            call.enqueue(object: Callback<ProductsListItem>{
                override fun onResponse(call: Call<ProductsListItem>, response: Response<ProductsListItem>) {
                    _productById.postValue(response.body()!!)
                }

                override fun onFailure(call: Call<ProductsListItem>, t: Throwable) {
                    t.printStackTrace()
                }
            })
        }
    }

}