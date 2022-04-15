package com.androiddevs.shoppinglisttestingyt.repository

import androidx.lifecycle.LiveData
import com.androiddevs.shoppinglisttestingyt.data.local.ShoppingDoa
import com.androiddevs.shoppinglisttestingyt.data.local.ShoppingItem
import com.androiddevs.shoppinglisttestingyt.data.remote.PixBayApi
import com.androiddevs.shoppinglisttestingyt.data.remote.responses.ImageResponse
import com.androiddevs.shoppinglisttestingyt.other.DataState
import java.lang.Exception
import javax.inject.Inject

class DefaultShoppingRepositery @Inject constructor(private val shoppingDoa: ShoppingDoa,private val pixBayApi: PixBayApi):IShoppingRepository{
    override suspend fun insertShoppingItem(shoppingItem: ShoppingItem) {
        shoppingDoa.insertShoppingItem(shoppingItem)
    }

    override suspend fun deleteShoppingItem(shoppingItem: ShoppingItem) {
        shoppingDoa.deleteShoppingItem(shoppingItem)
    }

    override fun observeAllShoppingItems(): LiveData<List<ShoppingItem>> {
       return shoppingDoa.observeAllShoppingItems()
    }

    override fun observeTotalPrice(): LiveData<Float> {
       return shoppingDoa.observeTotalPrice()
    }

    override suspend fun searchImagePixabay(query: String): DataState<ImageResponse> {
        return try {
            val response=pixBayApi.searchForImage(query)
            if (response.isSuccessful){
                val state=response.body()?.let {
                    DataState.Success(response.body()!!)
                } ?: DataState.Error("An unknownError")
                state
            }else{
                DataState.Error("An unknownError occurred")
            }
        }catch (e:Exception){
            DataState.Error("Couldn't featch data check url ")
        }
    }

}