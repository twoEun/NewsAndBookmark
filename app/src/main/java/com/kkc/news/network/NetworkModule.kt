package com.kkc.imagesearch.network

import com.google.gson.GsonBuilder
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import okhttp3.Interceptor
import okhttp3.Interceptor.Chain
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class NetworkModule {
    private val HOST_URL = "https://newsapi.org/v2/"
    private val CONNECT_TIME_OUT = 30L
    private val READ_TIME_OUT = 30L
    private val WRITE_TIME_OUT = 30L

    @Provides
    @Singleton
    fun provideOkHttpClient(interceptor: Interceptor): OkHttpClient {
        return OkHttpClient.Builder()
            .connectTimeout(CONNECT_TIME_OUT, TimeUnit.SECONDS)
            .readTimeout(READ_TIME_OUT, TimeUnit.SECONDS)
            .writeTimeout(WRITE_TIME_OUT, TimeUnit.SECONDS)
            .addInterceptor(
                HttpLoggingInterceptor().apply {
                    level = HttpLoggingInterceptor.Level.BODY
                }
            )
            .addInterceptor(interceptor)
            .build()
    }

    @Provides
    @Singleton
    fun provideInterceptor(): Interceptor {
        return Interceptor { chain: Chain ->
            val resultBuilder = chain.request().newBuilder()
                .addHeader("Content-Type", "application/json")
            chain.proceed(resultBuilder.build())
        }
    }

    @Provides
    @Singleton
    fun provideRetrofit(client: OkHttpClient): Retrofit {
        val gson = GsonBuilder().setLenient().setDateFormat("yyyy-MM-dd HH:mm:ss").create()
        return Retrofit.Builder().baseUrl(HOST_URL)
            .client(client)
            .addConverterFactory(GsonConverterFactory.create(gson))
            .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
            .build()
    }
}
