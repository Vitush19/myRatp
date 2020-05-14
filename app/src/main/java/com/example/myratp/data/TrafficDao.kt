package com.example.myratp.data

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import com.example.myratp.model.Traffic

@Dao
interface TrafficDao {
    @Query("select * from alltraffic")
    suspend fun getTraffic(): List<Traffic>

    @Insert
    suspend fun addTraffic(station: Traffic)

    @Delete
    suspend fun deleteTraffic(station: Traffic)

    @Query("delete from alltraffic")
    suspend fun deleteAllTraffic()

    @Query("select * from alltraffic where id_traffic = :id")
    suspend fun getTraffic(id: Int): Traffic
}