package com.mdev.chatapp.di

import android.app.Application
import androidx.room.Room
import com.mdev.chatapp.data.local.LocalDatabase
import com.mdev.chatapp.data.local.user.UserRepositoryImpl
import com.mdev.chatapp.data.local.app_entry.LocalUserManagerImpl
import com.mdev.chatapp.data.local.conversation.ConversationRepositoryImpl
import com.mdev.chatapp.data.remote.auth.AuthApi
import com.mdev.chatapp.data.remote.auth.AuthRepositoryImpl
import com.mdev.chatapp.data.remote.chat.ChatApi
import com.mdev.chatapp.domain.repository.local.UserRepository
import com.mdev.chatapp.domain.repository.remote.AuthRepository
import com.mdev.chatapp.domain.user_entry.LocalUserManager
import com.mdev.chatapp.domain.user_entry.app_entry.AppEntryUserCase
import com.mdev.chatapp.domain.user_entry.app_entry.ReadAppEntry
import com.mdev.chatapp.domain.user_entry.app_entry.SaveAppEntry
import com.mdev.chatapp.domain.repository.remote.ChatRepository
import com.mdev.chatapp.data.remote.chat.ChatRepositoryImpl
import com.mdev.chatapp.domain.repository.local.ConversationRepository
import com.mdev.chatapp.ui.history.HistoryRepository
import com.mdev.chatapp.ui.history.HistoryRepositoryImpl
import com.mdev.chatapp.util.Constants
import com.mdev.chatapp.util.DataStoreHelper
import com.mdev.chatapp.util.RetryInterceptor
import com.mdev.chatapp.util.StringProvider
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import java.util.concurrent.TimeUnit
import javax.inject.Singleton


@Module
@InstallIn(SingletonComponent::class)
object AuthModule {

    @Singleton
    @Provides
    fun provideAuthApi(): AuthApi {
        val retryInterceptor = RetryInterceptor(3)

        val okHttpClient = OkHttpClient.Builder()
            .addInterceptor(retryInterceptor)
            .connectTimeout(10, TimeUnit.SECONDS)
            .readTimeout(20, TimeUnit.SECONDS)
            .writeTimeout(20, TimeUnit.SECONDS)
            .build()

        return Retrofit.Builder()
            .baseUrl(Constants.AUTH_API_BASE_LINK)
            .client(okHttpClient)
            .addConverterFactory(MoshiConverterFactory.create())
            .build()
            .create(AuthApi::class.java)
    }

    @Singleton
    @Provides
    fun provideChatApi(): ChatApi {
        val okHttpClient = OkHttpClient.Builder()
            .connectTimeout(10, TimeUnit.SECONDS)
            .readTimeout(60, TimeUnit.SECONDS)
            .writeTimeout(60, TimeUnit.SECONDS)
            .build()
        return Retrofit.Builder()
            .baseUrl(Constants.CHAT_API_BASE_LINK)
            .client(okHttpClient)
            .addConverterFactory(MoshiConverterFactory.create())
            .build()
            .create(ChatApi::class.java)
    }

    @Singleton
    @Provides
    fun provideAuthRepository(api: AuthApi, dataStore: DataStoreHelper, userRepository: UserRepository): AuthRepository {
        return AuthRepositoryImpl(api, dataStore, userRepository)
    }

    @Singleton
    @Provides
    fun provideConversationRepository(localDatabase: LocalDatabase) : ConversationRepository {
        return ConversationRepositoryImpl(localDatabase.conversationDao)
    }

    @Singleton
    @Provides
    fun provideUserRepository(localDatabase: LocalDatabase): UserRepository {
        return UserRepositoryImpl(localDatabase.userDao)
    }

    @Singleton
    @Provides
    fun provideChatRepository(api: ChatApi): ChatRepository {
        return ChatRepositoryImpl(api)
    }

    @Singleton
    @Provides
    fun provideHistoryRepository(api: ChatApi): HistoryRepository {
        return HistoryRepositoryImpl(api)
    }

    @Singleton
    @Provides
    fun provideLocalDatabase(application: Application): LocalDatabase {
        return Room.databaseBuilder(application, LocalDatabase::class.java, LocalDatabase.DATABASE_NAME)
            .fallbackToDestructiveMigration()
            .build()
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


    @Singleton
    @Provides
    fun provideDataStoreHelper(application: Application): DataStoreHelper {
        return DataStoreHelper(application)
    }

    @Singleton
    @Provides
    fun provideStringProvider(application: Application): StringProvider {
        return StringProvider(application)
    }

}