package com.androiddevs.shoppinglisttestingyt.repository

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.androiddevs.shoppinglisttestingyt.data.local.ShoppingItem
import com.androiddevs.shoppinglisttestingyt.data.remote.responses.ImageResponse
import com.androiddevs.shoppinglisttestingyt.other.DataState

class FakeShoppingRepository:IShoppingRepository {
    private val listShppingItem= mutableListOf<ShoppingItem>()
    val observeShoppingItem=MutableLiveData<List<ShoppingItem>>(listShppingItem)
    private val observeTotalPrice=MutableLiveData<Float>()
    private var shouldReturnErrorNetwork=false
    fun setNetworkError(value:Boolean){
        shouldReturnErrorNetwork=value
    }

    fun refreshShoppingItem(){
        observeShoppingItem.postValue(listShppingItem)
        observeTotalPrice.postValue(getTotalPrice())
    }
    override suspend fun insertShoppingItem(shoppingItem: ShoppingItem) {
        listShppingItem.add(shoppingItem)
        refreshShoppingItem()
    }

    private fun getTotalPrice():Float{
        return listShppingItem.sumOf { it.amount*it.price.toDouble() }.toFloat()
    }

    override suspend fun deleteShoppingItem(shoppingItem: ShoppingItem) {
        listShppingItem.remove(shoppingItem)
        refreshShoppingItem()
    }

    override fun observeAllShoppingItems(): LiveData<List<ShoppingItem>> {
     return observeShoppingItem
    }

    override fun observeTotalPrice(): LiveData<Float> {
        return observeTotalPrice
    }

    override suspend fun searchImagePixabay(query: String): DataState<ImageResponse> {
        return if (shouldReturnErrorNetwork){
            DataState.Error("Error")
        }else{
            DataState.Success(ImageResponse(listOf(),0,0))
        }
    }

}