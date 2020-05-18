package com.example.myratp.data

import androidx.room.*
import com.example.myratp.model.MetroLine
import com.example.myratp.model.Station

@Dao
interface StationsDao {
    @Query("select * from allstations")
    suspend fun getStations(): List<Station>

    @Insert
    suspend fun addStations(station: Station)

    @Delete
    suspend fun deleteStations(station: Station)

    @Query("delete from allstations")
    suspend fun deleteAllStations()

    @Query("select * from allstations where id_ligne = :id_ligne")
    suspend fun getStationsByLine(id_ligne: String): List<Station>

    @Query("select * from allstations where name = :name")
    suspend fun getStationsByName(name: String): List<Station>

    @Query("select * from allstations where favoris = :favoris")
    suspend fun getStationFav(favoris: Boolean):List<Station>

    @Update
    suspend fun updateStations(station: Station)
}