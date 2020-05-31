package com.example.myratp.ui.timetable.tramlines

import androidx.appcompat.app.AppCompatActivity
import androidx.room.Room
import com.example.myratp.data.AppDatabase
import com.example.myratp.data.TramLineDao
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import com.facebook.stetho.okhttp3.StethoInterceptor
import java.util.concurrent.TimeUnit

// je rajoute la fonction clientDao à la classe AppcompactACtivity : retourne le DAO

fun AppCompatActivity.tramDao(): TramLineDao {
    val database: AppDatabase =
        Room.databaseBuilder(
            this,
            AppDatabase::class.java,
            "gestion_bus"
        )
            .build()
    return database.getTramLineDao()
}

fun retrofit_tram(): Retrofit {
    val httpLoggingInterceptor = HttpLoggingInterceptor().apply {
        level = HttpLoggingInterceptor.Level.BODY
    }
    val client = OkHttpClient.Builder()
        .addInterceptor(httpLoggingInterceptor)
        .addNetworkInterceptor(StethoInterceptor())
        .readTimeout(20, TimeUnit.SECONDS)
        .writeTimeout(20, TimeUnit.SECONDS)
        .connectTimeout(20, TimeUnit.SECONDS)
        .build()

    return Retrofit.Builder()
        .baseUrl("https://api-ratp.pierre-grimaud.fr/v4/")
        .addConverterFactory(MoshiConverterFactory.create())
        .client(client)
        .build()
}