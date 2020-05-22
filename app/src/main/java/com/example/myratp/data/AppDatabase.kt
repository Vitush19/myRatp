package com.example.myratp.data

import androidx.room.Database
import androidx.room.RoomDatabase
import com.example.myratp.model.*

@Database(
    entities = [MetroLine::class, BusLine::class, TrainLine::class, Station::class, Traffic::class, Schedule::class],
    version = 1
)

abstract class AppDatabase : RoomDatabase() {
    abstract fun getMetroLineDao(): MetroLineDao
    abstract fun getBusLineDao(): BusLineDao
    abstract fun getTrainLineDao(): TrainLineDao
    abstract fun getStationsDao(): StationsDao
    abstract fun getTrafficDao(): TrafficDao
    abstract fun getScheduleDao(): ScheduleDao

}