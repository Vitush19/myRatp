package com.example.myratp.utils

import com.example.myratp.R
import com.facebook.stetho.okhttp3.StethoInterceptor
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import java.util.concurrent.TimeUnit

fun retrofit(): Retrofit {
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

fun imageTraffic(line: String): Int{
    return when(line){
        "Métro" -> R.drawable.metro_bleu
        "RER" -> R.drawable.rer
        "Tramway" -> R.drawable.tramway
        else -> R.drawable.question
    }
}

fun imageMetro(line:String):Int{
    return when(line){
        "Métro 1"-> R.drawable.metro1
        "1"-> R.drawable.metro1
        "Métro 2"-> R.drawable.metro2
        "2"-> R.drawable.metro2
        "Métro 3"-> R.drawable.metro3
        "3"-> R.drawable.metro3
        "Métro 3b"-> R.drawable.metro3bis
        "3b"-> R.drawable.metro3bis
        "Métro 4"-> R.drawable.metro4
        "4"-> R.drawable.metro4
        "Métro 5"-> R.drawable.metro5
        "5"-> R.drawable.metro5
        "Métro 6"-> R.drawable.metro6
        "6"-> R.drawable.metro6
        "Métro 7"-> R.drawable.metro7
        "7"-> R.drawable.metro7
        "Métro 7b"-> R.drawable.metro7bis
        "7b"-> R.drawable.metro7bis
        "Métro 8"-> R.drawable.metro8
        "8"-> R.drawable.metro8
        "Métro 9"-> R.drawable.metro9
        "9"-> R.drawable.metro9
        "Métro 10"-> R.drawable.metro10
        "10"-> R.drawable.metro10
        "Métro 11"-> R.drawable.metro11
        "11"-> R.drawable.metro11
        "Métro 12"-> R.drawable.metro12
        "12"-> R.drawable.metro12
        "Métro 13"-> R.drawable.metro13
        "13"-> R.drawable.metro13
        "Métro 14"-> R.drawable.metro14
        "14"-> R.drawable.metro14
        "Métro Orv"-> R.drawable.orlyval
        "Orv"-> R.drawable.orlyval
        "Métro Fun"-> R.drawable.funiculaire
        "Métro 3B"-> R.drawable.metro3bis
        "Métro 7B"-> R.drawable.metro7bis
        "Fun"-> R.drawable.funiculaire
        "RER C"-> R.drawable.rer_c
        "C"-> R.drawable.rer_c
        "RER D"-> R.drawable.rer_d
        "D"-> R.drawable.rer_d
        "RER E"-> R.drawable.rer_e
        "E"-> R.drawable.rer_e
        "t4"->R.drawable.t4
        "T4"->R.drawable.t4
        "t11"->R.drawable.t11
        "T11"->R.drawable.t11
        else-> R.drawable.metro_bleu
    }
}