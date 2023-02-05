package com.example.hyundaiandroidproject.api

import com.example.hyundaiandroidproject.api.ApiClient.BASE_URL
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.adapter.rxjava3.RxJava3CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory


fun provideApi(): ApiInterface = Retrofit.Builder()
    .baseUrl(BASE_URL)
    .client(
        provideOkHttpClient()
    )
    .addCallAdapterFactory(RxJava3CallAdapterFactory.createSynchronous())
    .addConverterFactory(GsonConverterFactory.create())
    .build()
    .create(ApiInterface::class.java)

private fun provideOkHttpClient(): OkHttpClient = OkHttpClient.Builder()
    .addInterceptor(provideAuthInterceptor())
    .addInterceptor(provideLoggingInterceptor())
    .build()

private fun provideLoggingInterceptor(): HttpLoggingInterceptor = HttpLoggingInterceptor().apply {
    level = HttpLoggingInterceptor.Level.BASIC
}

private fun provideAuthInterceptor(): Interceptor = Interceptor { chain ->
    with(chain) {
        val newRequest = request().newBuilder()
            .addHeader("X-Naver-Client-Id", "33chRuAiqlSn5hn8tIme")
            .addHeader("X-Naver-Client-Secret", "fyfwt9PCUN")
            .build()
        proceed(newRequest)
    }
}

