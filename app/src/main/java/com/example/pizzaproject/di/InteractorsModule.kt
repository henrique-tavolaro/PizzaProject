package com.example.pizzaproject.di

import com.example.pizzaproject.datasource.firestore.FirestoreDatasource
import com.example.pizzaproject.datasource.room.OrderDao
import com.example.pizzaproject.domain.interactors.*
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent
import dagger.hilt.android.scopes.ViewModelScoped


@Module
@InstallIn(ViewModelComponent::class)
object InteractorsModule {

    @ViewModelScoped
    @Provides
    fun provideGetProductsList(
        firestore: FirestoreDatasource
    ) : GetProducts {
        return GetProducts(
            firestore = firestore
        )
    }


    @ViewModelScoped
    @Provides
    fun provideAddClient(
        firestore: FirestoreDatasource
    ): AddClient {
        return AddClient(
            firestore = firestore
        )
    }

    @ViewModelScoped
    @Provides
    fun provideSendOrder(
        firestore: FirestoreDatasource
    ): SendOrder {
        return SendOrder(
            firestore = firestore
        )
    }

    @ViewModelScoped
    @Provides
    fun provideAddProductToOrder(
        dao: OrderDao
    ) : AddProductToOrder{
        return AddProductToOrder(
            dao = dao
        )
    }

    @ViewModelScoped
    @Provides
    fun provideGetTotalSum(
        dao: OrderDao
    ) : GetOrderTotal{
        return GetOrderTotal(
            dao = dao
        )
    }

    @ViewModelScoped
    @Provides
    fun provideGetCart(
        dao: OrderDao
    ) : GetCart{
        return GetCart(
            dao = dao
        )
    }

    @ViewModelScoped
    @Provides
    fun provideClearCart(
        dao: OrderDao
    ) : ClearCart{
        return ClearCart(
            dao = dao
        )
    }

   @ViewModelScoped
    @Provides
    fun provideDeleteProductFromOrder(
        dao: OrderDao
    ) : DeleteProductFromOrder{
        return DeleteProductFromOrder(
            dao = dao
        )
    }






}