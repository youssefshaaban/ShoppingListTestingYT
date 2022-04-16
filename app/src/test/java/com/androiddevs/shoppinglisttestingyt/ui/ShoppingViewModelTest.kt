package com.androiddevs.shoppinglisttestingyt.ui

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.androiddevs.shoppinglisttestingyt.MainCorotinuesRule
import com.androiddevs.shoppinglisttestingyt.data.local.ShoppingItem
import com.androiddevs.shoppinglisttestingyt.data.remote.responses.ImageResponse
import com.androiddevs.shoppinglisttestingyt.getOrAwaitValueTest
import com.androiddevs.shoppinglisttestingyt.other.DataState
import com.androiddevs.shoppinglisttestingyt.other.MAX_NAME_LENGTH
import com.androiddevs.shoppinglisttestingyt.other.MAX_PRICE_LENGTH
import com.androiddevs.shoppinglisttestingyt.repository.FakeShoppingRepository
import com.androiddevs.shoppinglisttestingyt.repository.IShoppingRepository
import com.google.common.truth.Truth.assertThat
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.launch
import kotlinx.coroutines.test.TestCoroutineDispatcher
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test
@ExperimentalCoroutinesApi
class ShoppingViewModelTest {
    private lateinit var viewModel: ShoppingViewModel

    @get:Rule
    var instantTaskExecutorRule=InstantTaskExecutorRule()
    @get:Rule
    var mainCorotinuesRule=MainCorotinuesRule()
    @Before
    fun setup() {
        viewModel = ShoppingViewModel(FakeShoppingRepository())
    }


    @Test
    fun `insert shopping item with empty then return error`() {
        viewModel.insertShoppingItem("joe", "", "3.0")
        val value = viewModel.insertShoppingItemStatus.getOrAwaitValueTest()
      //  assertThat(value).isSameInstanceAs(DataState.Error(""))
        assertThat(value).isInstanceOf(DataState.Error::class.java)
    }

    @Test
    fun `insert shopping item with longName then return error`() {
        val string = buildString {
            for (i in 1..MAX_NAME_LENGTH + 1) {
                append(1)
            }
        }
        viewModel.insertShoppingItem(string, "2", "3.0")
        val value = viewModel.insertShoppingItemStatus.getOrAwaitValueTest()
        assertThat(value).isInstanceOf(DataState.Error::class.java)
    }


    @Test
    fun `insert shopping item with long price then return error`() {
        val string = buildString {
            for (i in 1..MAX_PRICE_LENGTH + 1) {
                append(1)
            }
        }
        viewModel.insertShoppingItem("string", "2", string)
        val value = viewModel.insertShoppingItemStatus.getOrAwaitValueTest()
        assertThat(value).isInstanceOf(DataState.Error::class.java)
    }

    @Test
    fun `insert shopping item with too high amount then return error`() {
        viewModel.insertShoppingItem("string", "2222222222222222222222", "3.0")
        val value = viewModel.insertShoppingItemStatus.getOrAwaitValueTest()
        assertThat(value).isInstanceOf(DataState.Error::class.java)
    }

    @Test
    fun `check selection observe with the same value`() {
        viewModel.setImageSelect("Strnig")
        val value = viewModel.image.getOrAwaitValueTest()
        assertThat(value).isEqualTo("Strnig")
    }

    @Test
    fun `insert shopping item with valid input then return error`() {
        viewModel.insertShoppingItem("string", "2", "3.0")
        val value = viewModel.insertShoppingItemStatus.getOrAwaitValueTest()
        assertThat(value).isInstanceOf(DataState.Success::class.java)
    }

}