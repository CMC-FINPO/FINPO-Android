package com.finpo.app.di

import android.util.Log
import com.finpo.app.network.ApiServiceWithoutToken
import com.finpo.app.repository.IntroRepository
import com.finpo.app.utils.API.BASE_URL
import com.finpo.app.utils.isJsonArray
import com.finpo.app.utils.isJsonObject
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import org.json.JSONArray
import org.json.JSONObject
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object NetworkModule {
    @Provides
    @Singleton
    fun provideHttpClient(): OkHttpClient {
        return OkHttpClient.Builder()
            .readTimeout(10, TimeUnit.SECONDS)
            .connectTimeout(10, TimeUnit.SECONDS)
            .writeTimeout(15, TimeUnit.SECONDS)
            .addInterceptor(getLoggingInterceptor())
            .build()
    }

    @Provides
    @Singleton
    fun provideConverterFactory(): GsonConverterFactory {
        return GsonConverterFactory.create()
    }

    private fun getLoggingInterceptor(): HttpLoggingInterceptor {
        val loggingInterceptor = HttpLoggingInterceptor { message ->
            when {
                message.isJsonObject() ->
                    Log.d("retrofit2", JSONObject(message).toString(4))
                message.isJsonArray() ->
                    Log.d("retrofit2", JSONArray(message).toString(4))
                else ->
                    Log.d("retrofit2", "CONNECTION INFO -> $message")
            }
        }
        loggingInterceptor.level = HttpLoggingInterceptor.Level.BODY
        return loggingInterceptor
    }

    @Singleton
    @Provides
    fun provideApiService(
        okHttpClient: OkHttpClient,
        gsonConverterFactory: GsonConverterFactory
    ): ApiServiceWithoutToken {
        return Retrofit.Builder()
            .baseUrl(BASE_URL)
            .client(okHttpClient)
            .addConverterFactory(gsonConverterFactory)
            .build()
            .create(ApiServiceWithoutToken::class.java)
    }

    @Singleton
    @Provides
    fun provideIntroRepository(apiServiceWithoutToken: ApiServiceWithoutToken) = IntroRepository(apiServiceWithoutToken)

}