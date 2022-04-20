package com.androiddevs.shoppinglisttestingyt.ui

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.androiddevs.shoppinglisttestingyt.data.local.ShoppingItem
import com.androiddevs.shoppinglisttestingyt.data.remote.responses.ImageResponse
import com.androiddevs.shoppinglisttestingyt.other.DataState
import com.androiddevs.shoppinglisttestingyt.other.MAX_NAME_LENGTH
import com.androiddevs.shoppinglisttestingyt.other.MAX_PRICE_LENGTH
import com.androiddevs.shoppinglisttestingyt.repository.IShoppingRepository
import com.bumptech.glide.load.engine.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
open class ShoppingViewModel @Inject constructor(private val iShoppingRepository: IShoppingRepository) :
    ViewModel() {
    val shoppingItems = iShoppingRepository.observeAllShoppingItems()
    val totalPrice = iShoppingRepository.observeTotalPrice()
    private val _images = MutableLiveData<DataState<ImageResponse>>()
    val images: LiveData<DataState<ImageResponse>> get() = _images
    private val _currentImage = MutableLiveData<String>()
    val image: LiveData<String> get() = _currentImage

    private val _insertShoppingItemStatus = MutableLiveData<DataState<ShoppingItem>>()
    val insertShoppingItemStatus: LiveData<DataState<ShoppingItem>> get() = _insertShoppingItemStatus

    fun setImageSelect(url: String) {
        _currentImage.postValue(url)
    }

    fun deleteShoppingItem(shoppingItem: ShoppingItem) = viewModelScope.launch {
        iShoppingRepository.deleteShoppingItem(shoppingItem)
    }

    fun insertShoppingItemDB(shoppingItem: ShoppingItem) = viewModelScope.launch {
        iShoppingRepository.insertShoppingItem(shoppingItem)
    }

    fun insertShoppingItem(name: String, amountString: String, priceString: String) {
        if (name.isEmpty() || amountString.isEmpty() || priceString.isEmpty()) {
            _insertShoppingItemStatus.value = DataState.Error("The fields must not be empty")
            return
        }
        if (name.length > MAX_NAME_LENGTH) {
            _insertShoppingItemStatus.postValue(
                DataState.Error(
                    "The name of the item" +
                            "must not exceed ${MAX_NAME_LENGTH} characters"
                )
            )
            return
        }
        if (priceString.length > MAX_PRICE_LENGTH) {
            _insertShoppingItemStatus.postValue(
                DataState.Error(
                    "The price of the item" +
                            "must not exceed ${MAX_PRICE_LENGTH} characters"
                )
            )
            return
        }
        val amount = try {
            amountString.toInt()
        } catch (e: Exception) {
            _insertShoppingItemStatus.postValue(DataState.Error("Please enter a valid amount"))
            return
        }
        val shoppingItem =
            ShoppingItem(name, amount, priceString.toFloat(), _currentImage.value ?: "")
        insertShoppingItemDB(shoppingItem)
        setImageSelect("")
        _insertShoppingItemStatus.postValue(DataState.Success(shoppingItem))
    }

    fun searchForImage(query: String) {
        if (query.isBlank()){
            return
        }
        _images.value=DataState.Loading
        viewModelScope.launch {
            val response=iShoppingRepository.searchImagePixabay(query)
            _images.value=response
        }
    }


}