package com.example.myratp.data

import androidx.room.Database
import androidx.room.RoomDatabase
import com.example.myratp.model.BusLine
import com.example.myratp.model.MetroLine

@Database(entities = [MetroLine::class, BusLine::class], version = 1)

abstract class AppDatabase : RoomDatabase(){
    abstract fun getMetroLineDao(): MetroLineDao
    abstract fun getBusLineDao(): BusLineDao
}