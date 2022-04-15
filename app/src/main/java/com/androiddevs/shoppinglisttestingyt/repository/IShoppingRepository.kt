package com.androiddevs.shoppinglisttestingyt.repository

import androidx.lifecycle.LiveData
import com.androiddevs.shoppinglisttestingyt.data.local.ShoppingItem
import com.androiddevs.shoppinglisttestingyt.data.remote.responses.ImageResponse
import com.androiddevs.shoppinglisttestingyt.other.DataState

interface IShoppingRepository {
    suspend fun insertShoppingItem(shoppingItem: ShoppingItem)
    suspend fun deleteShoppingItem(shoppingItem: ShoppingItem)
    fun observeAllShoppingItems():LiveData<List<ShoppingItem>>
    fun observeTotalPrice():LiveData<Float>
    suspend fun searchImagePixabay(query:String):DataState<ImageResponse>
}