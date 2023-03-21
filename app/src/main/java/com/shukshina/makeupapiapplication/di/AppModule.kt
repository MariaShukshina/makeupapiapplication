package com.shukshina.makeupapiapplication.di

import com.shukshina.makeupapiapplication.data.MakeUpApiRepositoryImpl
import com.shukshina.makeupapiapplication.domain.MakeUpApiRepository
import com.shukshina.makeupapiapplication.retrofit.MakeUpApi
import com.shukshina.makeupapiapplication.utils.Constants.BASE_URL
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    @Singleton
    @Provides
    fun provideMakeUpApiRepository(
        api: MakeUpApi
    ): MakeUpApiRepository = MakeUpApiRepositoryImpl(api)

    @Singleton
    @Provides
    fun provideMakeUpApi(): MakeUpApi {
        val loggingInterceptor = HttpLoggingInterceptor() // to see our requests and received responses
        loggingInterceptor.setLevel(HttpLoggingInterceptor.Level.BODY)

        val client = OkHttpClient.Builder()
            .addInterceptor(loggingInterceptor)
            .build()

        return Retrofit.Builder()
            .addConverterFactory(GsonConverterFactory.create())
            .baseUrl(BASE_URL)
            .client(client)
            .build()
            .create(MakeUpApi::class.java)
    }
}