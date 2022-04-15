package com.androiddevs.shoppinglisttestingyt.other

sealed class DataState<out T> {
    data class Success<T>(val data: T) : DataState<T>()
    data class Error(val error: String) : DataState<Nothing>()
    //data class Validation<T>(val enumValidate:T) : DataState<Nothing>()
    object Loading: DataState<Nothing>()
}

