package io.github.lokarzz.speedtest.dagger.module

import android.content.Context
import com.google.gson.GsonBuilder
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import io.github.lokarzz.speedtest.BuildConfig
import io.github.lokarzz.speedtest.constants.AppConstants
import io.github.lokarzz.speedtest.repository.digitalocean.local.room.database.DigitalOceanDatabase
import io.github.lokarzz.speedtest.repository.digitalocean.remote.retrofit.IDigitalOceanService
import io.github.lokarzz.speedtest.repository.digitalocean.remote.retrofit.interceptor.BaseUrlInterceptor
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
    fun provideIDigitalOceanService(
        retrofit: Retrofit,
    ): IDigitalOceanService {
        return retrofit.create(IDigitalOceanService::class.java)
    }

    @Singleton
    @Provides
    fun provideRetrofitDigitalOceanService(
        gsonConverterFactory: GsonConverterFactory,
        okHttpClient: OkHttpClient
    ): Retrofit {
        return Retrofit.Builder()
            .baseUrl(BuildConfig.DIGITAL_OCEAN_BASE_URL)
            .client(okHttpClient)
            .addConverterFactory(gsonConverterFactory)
            .build()
    }

    @Singleton
    @Provides
    fun provideOkHttpClient(
        httpLoggingInterceptor: HttpLoggingInterceptor,
        baseUrlInterceptor: BaseUrlInterceptor,
    ): OkHttpClient {
        val builder = OkHttpClient().newBuilder()
        builder.addInterceptor(baseUrlInterceptor)
        if (BuildConfig.DEBUG) {
            builder.addInterceptor(httpLoggingInterceptor)
        }
        return builder.build()
    }

    @Singleton
    @Provides
    fun provideHttpLoggingInterceptor(): HttpLoggingInterceptor {
        val httpLoggingInterceptor = HttpLoggingInterceptor()
        httpLoggingInterceptor.setLevel(HttpLoggingInterceptor.Level.BODY)
        return httpLoggingInterceptor
    }


    @Singleton
    @Provides
    fun provideBaseUrlInterceptor(): BaseUrlInterceptor {
        return BaseUrlInterceptor()
    }


    @Singleton
    @Provides
    fun provideGsonConverterFactory(): GsonConverterFactory {
        val gson = GsonBuilder()
            .setDateFormat(AppConstants.Date.DATE_ISO)
            .setLenient()
            .create()
        return GsonConverterFactory.create(gson)
    }

    @Singleton
    @Provides
    fun provideAppDatabase(@ApplicationContext context: Context): DigitalOceanDatabase {
        return DigitalOceanDatabase.initRoom(context)
    }


}