package com.androiddevs.shoppinglisttestingyt.ui

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.androiddevs.shoppinglisttestingyt.data.local.ShoppingItem
import com.androiddevs.shoppinglisttestingyt.data.remote.responses.ImageResponse
import com.androiddevs.shoppinglisttestingyt.other.DataState
import com.androiddevs.shoppinglisttestingyt.repository.IShoppingRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch

@HiltViewModel
class ShoppingViewModel(private val iShoppingRepository: IShoppingRepository):ViewModel() {
    val shoppingItems=iShoppingRepository.observeAllShoppingItems()
    val totalPrice=iShoppingRepository.observeTotalPrice()
    private val _images=MutableLiveData<DataState<ImageResponse>>()
    val images:LiveData<DataState<ImageResponse>> get() =_images
    private val _currentImage=MutableLiveData<String>()
    val image:LiveData<String> get() =_currentImage

    private val _insertShoppingItemStatus=MutableLiveData<DataState<ShoppingItem>>()
    val insertShoppingItemStatus:LiveData<DataState<ShoppingItem>> get() =_insertShoppingItemStatus

    fun setImageSelect(url:String){
        _currentImage.postValue(url)
    }

    fun deleteShoppingItem(shoppingItem: ShoppingItem)=viewModelScope.launch {
        iShoppingRepository.deleteShoppingItem(shoppingItem)
    }

    fun insertShoppingItemDB(shoppingItem: ShoppingItem)=viewModelScope.launch {
        iShoppingRepository.insertShoppingItem(shoppingItem)
    }

    fun insertShoppingItem(name:String,amount:String,priceString:String){

    }

    fun searchForImage(query:String)=viewModelScope.launch {
        iShoppingRepository.searchImagePixabay(query)
    }


}