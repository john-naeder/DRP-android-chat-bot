package com.mdev.chatapp.di

import android.app.Application
import androidx.room.Room
import com.mdev.chatapp.data.local.app_entry.LocalUserManagerImpl
import com.mdev.chatapp.data.local.user.AccountDatabase
import com.mdev.chatapp.data.local.user.AccountRepositoryImpl
import com.mdev.chatapp.data.remote.auth.AuthApi
import com.mdev.chatapp.domain.repository.AuthRepository
import com.mdev.chatapp.data.remote.auth.AuthRepositoryImpl
import com.mdev.chatapp.data.remote.home.HomeRepositoryImpl
import com.mdev.chatapp.domain.repository.HomeRepository
import com.mdev.chatapp.domain.repository.AccountRepository
import com.mdev.chatapp.domain.user_entry.LocalUserManager
import com.mdev.chatapp.domain.user_entry.app_entry.AppEntryUserCase
import com.mdev.chatapp.domain.user_entry.app_entry.ReadAppEntry
import com.mdev.chatapp.domain.user_entry.app_entry.SaveAppEntry
import com.mdev.chatapp.util.Constants
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
            .baseUrl(Constants.AUTH_API_BASE_LINK)
            .addConverterFactory(MoshiConverterFactory.create())
            .build()
            .create(AuthApi::class.java)
    }

    @Singleton
    @Provides
    fun provideAuthRepository(api: AuthApi, dataStore: DataStoreHelper, accountRepository: AccountRepository): AuthRepository {
        return AuthRepositoryImpl(api, dataStore, accountRepository)
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
    fun provideUserDatabase(application: Application): AccountDatabase {
        return Room.databaseBuilder(application, AccountDatabase::class.java, AccountDatabase.DATABASE_NAME)
            .fallbackToDestructiveMigration()
            .build()
    }

    @Singleton
    @Provides
    fun provideUserRepository(accountDatabase: AccountDatabase): AccountRepository {
        return AccountRepositoryImpl(accountDatabase.dao)
    }

    @Provides
    @Singleton
    fun provideLocalUserManager(
        application: Application
    ): LocalUserManager  = LocalUserManagerImpl(application)

    @Provides
    @Singleton
    fun provideAppEntryUserCases(localUserManager : LocalUserManager) =  AppEntryUserCase(
        readAppEntry = ReadAppEntry(localUserManager),
        saveAppEntry = SaveAppEntry(localUserManager)
    )
}