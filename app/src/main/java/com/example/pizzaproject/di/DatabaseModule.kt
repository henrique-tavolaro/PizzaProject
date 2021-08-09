package com.example.pizzaproject.di

import android.content.Context
import androidx.room.Room
import com.example.pizzaproject.datasource.room.OrderDatabase
import com.example.pizzaproject.datasource.room.OrderDatabase.Companion.DATABASE
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class DatabaseModule {

    @Provides
    @Singleton
    fun provideUserDatabase(
        @ApplicationContext app: Context
    ) = Room.databaseBuilder(
        app,
        OrderDatabase::class.java,
        DATABASE
    ).build()

    @Singleton
    @Provides
    fun provideOrderDao(db: OrderDatabase) = db.getDao()

}