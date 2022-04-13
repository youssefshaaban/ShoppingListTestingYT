package com.androiddevs.shoppinglisttestingyt.data.local

import androidx.lifecycle.LiveData
import androidx.room.*

@Dao
interface ShoppingDoa {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertShoppingItem(shoppingItem: ShoppingItem)

    @Delete
    suspend fun deleteShoppingItem(shoppingItem: ShoppingItem)

    @Query("SELECT * from shopping_item")
    fun observeAllShoppingItems():LiveData<List<ShoppingItem>>


    @Query("SELECT SUM(price*amount) from shopping_item")
    fun observeTotalPrice():LiveData<Float>

}