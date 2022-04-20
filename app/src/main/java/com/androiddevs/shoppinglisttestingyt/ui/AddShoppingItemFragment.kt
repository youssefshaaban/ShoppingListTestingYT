package com.androiddevs.shoppinglisttestingyt.ui

import android.os.Bundle
import android.view.View
import androidx.activity.OnBackPressedCallback
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.androiddevs.shoppinglisttestingyt.R
import com.androiddevs.shoppinglisttestingyt.data.local.ShoppingItem
import com.androiddevs.shoppinglisttestingyt.other.DataState
import com.bumptech.glide.RequestManager
import com.google.android.material.snackbar.Snackbar
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.fragment_add_shopping_item.*
import kotlinx.android.synthetic.main.fragment_shopping.*
import java.lang.Error
import javax.inject.Inject
@AndroidEntryPoint
class AddShoppingItemFragment : Fragment(R.layout.fragment_add_shopping_item) {
    lateinit var viewModel: ShoppingViewModel

    @Inject
    lateinit var glide: RequestManager
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel = ViewModelProvider(requireActivity()).get(ShoppingViewModel::class.java)
        ivShoppingImage.setOnClickListener {
            findNavController().navigate(AddShoppingItemFragmentDirections.actionAddShoppingItemFragmentToImagePickFragment())
        }
        val callback = object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                viewModel.setImageSelect("")
                findNavController().popBackStack()
            }
        }
        requireActivity().onBackPressedDispatcher.addCallback(callback)
        subscribeToObserve()
        btnAddShoppingItem.setOnClickListener {
            viewModel.insertShoppingItem(
                etShoppingItemName.text.toString(),
                etShoppingItemAmount.text.toString(),
                etShoppingItemPrice.text.toString()
            )
        }
    }

    private fun subscribeToObserve() {
        viewModel.image.observe(viewLifecycleOwner) {
            glide.load(it).into(ivShoppingImage)
        }
        viewModel.insertShoppingItemStatus.observe(viewLifecycleOwner, ::handleStatusInsertShooping)
    }

    private fun handleStatusInsertShooping(dataState: DataState<ShoppingItem>?) {
        when (dataState) {
            is DataState.Success -> {
                Snackbar.make(requireActivity().rootLayout, "success add shopping item", 1000)
                    .show()
                findNavController().popBackStack()
            }
            is DataState.Error -> {
                Snackbar.make(requireActivity().rootLayout, dataState.error, 1000).show()
            }
            is DataState.Loading -> {
                /*No operation here*/
            }
            else -> {}
        }
    }


}