package com.example.myratp.ui.timetable.metrolines

import androidx.appcompat.app.AppCompatActivity
import androidx.room.Room
import com.example.myratp.R
import com.example.myratp.data.AppDatabase
import com.example.myratp.data.MetroLineDao
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import com.facebook.stetho.okhttp3.StethoInterceptor
import java.util.concurrent.TimeUnit


