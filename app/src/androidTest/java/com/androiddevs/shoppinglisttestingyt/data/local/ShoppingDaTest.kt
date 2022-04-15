package com.androiddevs.shoppinglisttestingyt.data.local

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.filters.SmallTest
import com.androiddevs.shoppinglisttestingyt.getOrAwaitValue
import com.google.common.truth.Truth.assertThat
import kotlinx.coroutines.test.runBlockingTest
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
@SmallTest
class ShoppingDaTest {
    private lateinit var shoppingItemDatabase: ShoppingItemDatabase

    private lateinit var shoppingDoa: ShoppingDoa
    @get:Rule
    var instantTaskExecutorRule = InstantTaskExecutorRule()
    @Before
    fun setup() {
        shoppingItemDatabase = Room.inMemoryDatabaseBuilder(
            ApplicationProvider.getApplicationContext(),
            ShoppingItemDatabase::class.java
        ).allowMainThreadQueries().build()
        shoppingDoa=shoppingItemDatabase.shoppingDao()
    }

    @After
    fun tearDown(){
        shoppingItemDatabase.close()
    }

    @Test
    fun insertShopping(){
        runBlockingTest {
            val shoppingItem=ShoppingItem("mmm",3,34.5f,"url", id = 1)
            shoppingDoa.insertShoppingItem(shoppingItem)
            val allShoppingItem=shoppingDoa.observeAllShoppingItems().getOrAwaitValue()
            assertThat(allShoppingItem).contains(shoppingItem)
        }
    }

    @Test
    fun deleteShoppingItem(){
        runBlockingTest {
            val shoppingItem=ShoppingItem("mmm",3,34.5f,"url", id = 1)
            shoppingDoa.insertShoppingItem(shoppingItem)
            shoppingDoa.deleteShoppingItem(shoppingItem)
            val allShoppingItem=shoppingDoa.observeAllShoppingItems().getOrAwaitValue()
            assertThat(allShoppingItem).doesNotContain(shoppingItem)
        }
    }

    @Test
    fun observeTotalPrice(){
        runBlockingTest {
            val shoppingItem=ShoppingItem("mmm",3,34.5f,"url", id = 1)
            val shoppingItem1=ShoppingItem("mmm",2,34.5f,"url", id = 2)
            val shoppingItem2=ShoppingItem("mmm",1,34.5f,"url", id = 3)
            shoppingDoa.insertShoppingItem(shoppingItem)
            shoppingDoa.insertShoppingItem(shoppingItem1)
            shoppingDoa.insertShoppingItem(shoppingItem2)

            val totalPrice=shoppingDoa.observeTotalPrice().getOrAwaitValue()
            assertThat(totalPrice).isEqualTo(3*34.5f + 2*34.5f +34.5f)
        }
    }
}