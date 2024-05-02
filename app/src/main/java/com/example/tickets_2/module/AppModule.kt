package com.example.tickets_2.module

import com.example.tickets_2.api.kvitki.KvitkiRestClient
import com.example.tickets_2.service.NotificationService
import com.example.tickets_2.storage.NotificationSharedPreferences
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
class AppModule {

    @Provides
    fun kvitkiRestClient(): KvitkiRestClient {
        return KvitkiRestClient()
    }

    @Provides
    fun notificationService(): NotificationService {
        return NotificationService()
    }

    @Provides
    fun notificationSharedPreferences(): NotificationSharedPreferences {
        return NotificationSharedPreferences()
    }

}