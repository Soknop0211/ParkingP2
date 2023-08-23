package com.daikou.p2parking.data.di.module

import android.app.Application
import android.content.Context
import com.daikou.p2parking.base.Config
import com.daikou.p2parking.data.di.network.MyServiceInterceptor
import com.daikou.p2parking.data.remote.ParkingApi
import com.daikou.p2parking.data.repository.Repository
import com.daikou.p2parking.helper.LogDemoJson
import com.google.gson.FieldNamingPolicy
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import dagger.Module
import dagger.Provides
import okhttp3.*
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.io.File
import java.util.concurrent.TimeUnit
import javax.inject.Singleton

/**
 *created by Tona Song on 29-03-21.
 **/

@Module
object NetworkModule {

    @Provides
    @Singleton
    fun provideContext(app: Application): Context {
        return app.applicationContext
    }

    @Provides
    @Singleton
    fun provideGson(): Gson {
        val builder = GsonBuilder()
            .setFieldNamingPolicy(FieldNamingPolicy.LOWER_CASE_WITH_UNDERSCORES)
        return builder
            .setLenient()
            .create()
    }


    @Provides
    @Singleton
    fun provideCache(app: Application): Cache {
        return Cache(
            File(app.applicationContext.cacheDir, "caifutong_cache"),
            10 * 1024 * 1024
        )
    }

    @Provides
    @Singleton
    fun provideMyServiceInterceptor(context: Context): MyServiceInterceptor {
        return MyServiceInterceptor(context)
    }

    @Provides
    @Singleton
    fun provideRequestHeader(
        cache: Cache,
        myServiceInterceptor: MyServiceInterceptor
    ): OkHttpClient {
        val timeOut:Long = 120
        val httpClient = OkHttpClient.Builder()
        httpClient.cache(cache)
        /*  httpClient.addNetworkInterceptor(cacheInterceptor)*/
        httpClient.addNetworkInterceptor(LogDemoJson.KessLogDataGson())
        httpClient.addInterceptor(myServiceInterceptor)
        httpClient.connectTimeout(timeOut, TimeUnit.SECONDS)
        httpClient.writeTimeout(timeOut, TimeUnit.SECONDS)
        httpClient.readTimeout(timeOut, TimeUnit.SECONDS)
        return httpClient.build()
    }

    @Provides
    @Singleton
    fun provideRetrofitInstance(gson: Gson, okHttpClient: OkHttpClient): Retrofit =
        Retrofit.Builder()
            .baseUrl(Config.BASE_URL)
            .client(okHttpClient)
            .addConverterFactory(GsonConverterFactory.create())
            .addConverterFactory(GsonConverterFactory.create(gson))
            .build()

    @Provides
    @Singleton
    fun provideCaiFuTongApi(retrofit: Retrofit): ParkingApi {
        return retrofit.create(ParkingApi::class.java)
    }

    @Provides
    @Singleton
    fun provideRepository(context: Context, caiFuTongApi: ParkingApi): Repository {
        return Repository(context,caiFuTongApi)
    }

}