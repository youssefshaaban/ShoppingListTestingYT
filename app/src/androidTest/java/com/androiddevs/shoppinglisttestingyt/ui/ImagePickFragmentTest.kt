package com.androiddevs.shoppinglisttestingyt.ui

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.navigation.NavController
import androidx.navigation.Navigation
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions.click
import androidx.test.espresso.contrib.RecyclerViewActions
import androidx.test.espresso.matcher.ViewMatchers.withId
import androidx.test.filters.MediumTest
import com.androiddevs.shoppinglisttestingyt.R
import com.androiddevs.shoppinglisttestingyt.adapters.ImageAdapter
import com.androiddevs.shoppinglisttestingyt.getOrAwaitValueAndroidTest
import com.androiddevs.shoppinglisttestingyt.launchFragmentInHiltContainer
import com.androiddevs.shoppinglisttestingyt.repository.FakeShoppingRepositoryAndroidTest
import com.google.common.truth.Truth.assertThat
import dagger.hilt.android.testing.HiltAndroidRule
import dagger.hilt.android.testing.HiltAndroidTest
import kotlinx.coroutines.ExperimentalCoroutinesApi
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.mockito.Mockito.mock
import org.mockito.Mockito.verify

@MediumTest
@HiltAndroidTest
@ExperimentalCoroutinesApi
class ImagePickFragmentTest {

    @get:Rule
    var instantTaskExecutorRule=InstantTaskExecutorRule()
    @get:Rule
    var hiltRule = HiltAndroidRule(this)

    @Before
    fun setup() {
        hiltRule.inject()
    }

    @Test
    fun clickItemImageSelect_popFragmentAndSetSelect() {
        val navController = mock(NavController::class.java)
        val testViewmodel=ShoppingViewModel(FakeShoppingRepositoryAndroidTest())
        val imag_url="Test"
        launchFragmentInHiltContainer<ImagePickFragment> {
            Navigation.setViewNavController(requireView(), navController)
            viewModel=testViewmodel
            imageAdapter.images=listOf(imag_url)
        }

        onView(withId(R.id.rvImages)).perform(RecyclerViewActions.actionOnItemAtPosition<ImageAdapter.ImageViewHolder>(0,
            click()))

        verify(navController).popBackStack()
        assertThat(testViewmodel.image.getOrAwaitValueAndroidTest()).isEqualTo(imag_url)
    }
}