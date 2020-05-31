package com.example.myratp.data

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverter
import androidx.room.TypeConverters
import com.example.myratp.model.*

@Database(
    entities = [MetroLine::class, BusLine::class, TrainLine::class, TramLine::class, Noctilien::class, Station::class, Traffic::class, Schedule::class],
    version = 1
)
@TypeConverters(testConverter::class)
abstract class AppDatabase : RoomDatabase() {
    abstract fun getMetroLineDao(): MetroLineDao
    abstract fun getBusLineDao(): BusLineDao
    abstract fun getTrainLineDao(): TrainLineDao
    abstract fun getStationsDao(): StationsDao
    abstract fun getTrafficDao(): TrafficDao
    abstract fun getScheduleDao(): ScheduleDao
    abstract fun getTramLineDao(): TramLineDao
    abstract fun getNoctilienDao(): NoctilienDao
}

class testConverter {
    @TypeConverter
    fun fromType(value: Type) = value.toString()
    @TypeConverter
    fun toType(value: String) = Type.valueOf(value)
}