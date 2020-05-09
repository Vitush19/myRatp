package com.example.myratp.data

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import com.example.myratp.model.BusLine

@Dao
interface BusLineDao {
    @Query("select * from allbuslines")
    suspend fun getBusLines() : List<BusLine>

    @Insert
    suspend fun addBusLines(busLine: BusLine)

    @Delete
    suspend fun deleteBuslLines(busLine: BusLine)

    @Query("delete from allbuslines")
    suspend fun deleteAllBusLines()

    @Query("select * from allbuslines where id = :id")
    suspend fun getBusLines(id: Int) : BusLine
}