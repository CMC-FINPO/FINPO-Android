package com.finpo.app.di

import android.util.Log
import com.finpo.app.network.*
import com.finpo.app.repository.IntroRepository
import com.finpo.app.utils.API.BASE_URL
import com.finpo.app.utils.RETROFIT_TAG
import com.finpo.app.utils.isJsonArray
import com.finpo.app.utils.isJsonObject
import com.skydoves.sandwich.adapters.ApiResponseCallAdapterFactory
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ActivityComponent
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
    @NormalOkHttpClient
    fun provideHttpClient(
        loggingInterceptor: HttpLoggingInterceptor
    ): OkHttpClient {
        return OkHttpClient.Builder()
            .readTimeout(10, TimeUnit.SECONDS)
            .connectTimeout(10, TimeUnit.SECONDS)
            .writeTimeout(15, TimeUnit.SECONDS)
            .addInterceptor(loggingInterceptor)
            .build()
    }

    @Provides
    @Singleton
    fun provideConverterFactory(): GsonConverterFactory {
        return GsonConverterFactory.create()
    }

    @Provides
    @Singleton
    fun provideLoggingInterceptor(): HttpLoggingInterceptor {
        val loggingInterceptor = HttpLoggingInterceptor { message ->
            when {
                message.isJsonObject() ->
                    Log.d(RETROFIT_TAG, JSONObject(message).toString(4))
                message.isJsonArray() ->
                    Log.d(RETROFIT_TAG, JSONArray(message).toString(4))
                else ->
                    Log.d(RETROFIT_TAG, "CONNECTION INFO -> $message")
            }
        }
        loggingInterceptor.level = HttpLoggingInterceptor.Level.BODY
        return loggingInterceptor
    }

    @Singleton
    @Provides
    fun provideApiServiceWithoutToken(
        @NormalOkHttpClient okHttpClient: OkHttpClient,
        gsonConverterFactory: GsonConverterFactory
    ): ApiServiceWithoutToken {
        return Retrofit.Builder()
            .baseUrl(BASE_URL)
            .client(okHttpClient)
            .addCallAdapterFactory(ApiResponseCallAdapterFactory.create())
            .addConverterFactory(gsonConverterFactory)
            .build()
            .create(ApiServiceWithoutToken::class.java)
    }

    @Singleton
    @Provides
    fun provideGoogleLoginApi(
        @NormalOkHttpClient okHttpClient: OkHttpClient,
        gsonConverterFactory: GsonConverterFactory
    ): GoogleLoginApi {
        return Retrofit.Builder()
            .baseUrl("https://www.googleapis.com/")
            .client(okHttpClient)
            .addCallAdapterFactory(ApiResponseCallAdapterFactory.create())
            .addConverterFactory(gsonConverterFactory)
            .build()
            .create(GoogleLoginApi::class.java)
    }
}