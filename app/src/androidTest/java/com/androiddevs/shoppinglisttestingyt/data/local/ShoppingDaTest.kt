package com.androiddevs.shoppinglisttestingyt.data.local

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.test.filters.SmallTest
import com.androiddevs.shoppinglisttestingyt.getOrAwaitValueAndroidTest
import com.google.common.truth.Truth.assertThat
import dagger.hilt.android.testing.HiltAndroidRule
import dagger.hilt.android.testing.HiltAndroidTest
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runBlockingTest
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import javax.inject.Inject
import javax.inject.Named

@ExperimentalCoroutinesApi
@SmallTest
@HiltAndroidTest
class ShoppingDaTest {

    @Inject
    @Named("test_db")
    lateinit var shoppingItemDatabase: ShoppingItemDatabase

    @get:Rule
    var hiltRule = HiltAndroidRule(this)

    @get:Rule
    var instantTaskExecutorRule = InstantTaskExecutorRule()

    private lateinit var shoppingDoa: ShoppingDoa

    @Before
    fun setup() {
        hiltRule.inject()
        shoppingDoa = shoppingItemDatabase.shoppingDao()
    }

    @After
    fun tearDown() {
        shoppingItemDatabase.close()
    }

    @Test
    fun insertShopping() {
        runBlockingTest {
            val shoppingItem = ShoppingItem("mmm", 3, 34.5f, "url", id = 1)
            shoppingDoa.insertShoppingItem(shoppingItem)
            val allShoppingItem = shoppingDoa.observeAllShoppingItems().getOrAwaitValueAndroidTest()
            assertThat(allShoppingItem).contains(shoppingItem)
        }
    }

    @Test
    fun deleteShoppingItem() {
        runBlockingTest {
            val shoppingItem = ShoppingItem("mmm", 3, 34.5f, "url", id = 1)
            shoppingDoa.insertShoppingItem(shoppingItem)
            shoppingDoa.deleteShoppingItem(shoppingItem)
            val allShoppingItem = shoppingDoa.observeAllShoppingItems().getOrAwaitValueAndroidTest()
            assertThat(allShoppingItem).doesNotContain(shoppingItem)
        }
    }

    @Test
    fun observeTotalPrice() {
        runBlockingTest {
            val shoppingItem = ShoppingItem("mmm", 3, 34.5f, "url", id = 1)
            val shoppingItem1 = ShoppingItem("mmm", 2, 34.5f, "url", id = 2)
            val shoppingItem2 = ShoppingItem("mmm", 1, 34.5f, "url", id = 3)
            shoppingDoa.insertShoppingItem(shoppingItem)
            shoppingDoa.insertShoppingItem(shoppingItem1)
            shoppingDoa.insertShoppingItem(shoppingItem2)

            val totalPrice = shoppingDoa.observeTotalPrice().getOrAwaitValueAndroidTest()
            assertThat(totalPrice).isEqualTo(3 * 34.5f + 2 * 34.5f + 34.5f)
        }
    }
}