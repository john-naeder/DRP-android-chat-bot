package com.mdev.chatapp.di

import android.app.Application
import androidx.room.Database
import androidx.room.Room
import com.mdev.chatapp.data.local.user.UserSignedInDatabase
import com.mdev.chatapp.data.local.user.UserSignedInRepositoryImpl
import com.mdev.chatapp.data.remote.auth.AuthApi
import com.mdev.chatapp.domain.repository.AuthRepository
import com.mdev.chatapp.data.remote.auth.AuthRepositoryImpl
import com.mdev.chatapp.data.remote.home.HomeRepositoryImpl
import com.mdev.chatapp.domain.repository.HomeRepository
import com.mdev.chatapp.domain.repository.UserSignedInRepository
import com.mdev.chatapp.util.DataStoreHelper
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import javax.inject.Singleton


@Module
@InstallIn(SingletonComponent::class)
object AuthModule {

    @Singleton
    @Provides
    fun provideAuthApi(): AuthApi {
        return Retrofit.Builder()
            .baseUrl("https://drpteam.vercel.app/v1/api/")
            .addConverterFactory(MoshiConverterFactory.create())
            .build()
            .create(AuthApi::class.java)
    }

    @Singleton
    @Provides
    fun provideAuthRepository(api: AuthApi, dataStore: DataStoreHelper): AuthRepository {
        return AuthRepositoryImpl(api, dataStore)
    }

    @Singleton
    @Provides
    fun provideHomeRepository(dataStore: DataStoreHelper): HomeRepository {
        return HomeRepositoryImpl(dataStore)
    }

    @Singleton
    @Provides
    fun provideDataStoreHelper(application: Application): DataStoreHelper {
        return DataStoreHelper(application)
    }

    @Singleton
    @Provides
    fun provideUserDatabase(application: Application): UserSignedInDatabase {
        return Room.databaseBuilder(application, UserSignedInDatabase::class.java, UserSignedInDatabase.DATABASE_NAME)
            .fallbackToDestructiveMigration()
            .build()
    }

    @Singleton
    @Provides
    fun provideUserRepository(userSignedInDatabase: UserSignedInDatabase): UserSignedInRepository {
        return UserSignedInRepositoryImpl(userSignedInDatabase.dao)
    }
}