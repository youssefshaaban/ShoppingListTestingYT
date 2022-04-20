package com.androiddevs.shoppinglisttestingyt.di

import android.content.Context
import androidx.room.Room
import com.androiddevs.shoppinglisttestingyt.R
import com.androiddevs.shoppinglisttestingyt.data.local.ShoppingDoa
import com.androiddevs.shoppinglisttestingyt.data.local.ShoppingItemDatabase
import com.androiddevs.shoppinglisttestingyt.data.remote.PixBayApi
import com.androiddevs.shoppinglisttestingyt.other.BASE_URL
import com.androiddevs.shoppinglisttestingyt.other.DATABASE_NAME
import com.androiddevs.shoppinglisttestingyt.repository.DefaultShoppingRepositery
import com.androiddevs.shoppinglisttestingyt.repository.IShoppingRepository
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class AppModule {

    @Provides
    @Singleton
    fun provideShoppingDatabase(@ApplicationContext context: Context): ShoppingItemDatabase =
        Room.databaseBuilder(context, ShoppingItemDatabase::class.java, DATABASE_NAME).build()

    @Provides
    @Singleton
    fun provideDefaultShoppingRepositary(
        shoppingDoa: ShoppingDoa,
        api: PixBayApi
    ): IShoppingRepository =
        DefaultShoppingRepositery(shoppingDoa, pixBayApi = api)

    @Singleton
    @Provides
    fun provideGlideInstance(
        @ApplicationContext context: Context
    ) = Glide.with(context).setDefaultRequestOptions(
        RequestOptions()
            .placeholder(R.drawable.ic_image)
            .error(R.drawable.ic_image)
    )

    @Provides
    @Singleton
    fun provideShoppingDao(shoppingItemDatabase: ShoppingItemDatabase): ShoppingDoa =
        shoppingItemDatabase.shoppingDao()

    @Provides
    @Singleton
    fun providePixabayApi(): PixBayApi = Retrofit.Builder()
        .addConverterFactory(GsonConverterFactory.create())
        .baseUrl(BASE_URL)
        .build().create(PixBayApi::class.java)


}