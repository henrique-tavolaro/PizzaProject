package com.example.pizzaproject.di

import com.example.pizzaproject.datasource.firestore.FirestoreDatasource
import com.example.pizzaproject.domain.interactors.GetProducts
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

}