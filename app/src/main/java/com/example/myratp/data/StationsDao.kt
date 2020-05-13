package com.example.myratp.data

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import com.example.myratp.model.MetroLine
import com.example.myratp.model.Station

@Dao
interface StationsDao {
    @Query("select * from allstations")
    suspend fun getStations() : List<Station>

    @Insert
    suspend fun addStations(station: Station)

    @Delete
    suspend fun deleteStations(station: Station)

    @Query("delete from allstations")
    suspend fun deleteAllStations()

    @Query("select * from allstations where id_station = :id")
    suspend fun getStations(id: Int) : Station

    @Query("select * from allstations where name = :name")
    suspend fun getStationByName(name :String) : Station
}