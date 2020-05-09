package com.example.myratp.data

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import com.example.myratp.model.MetroLine

@Dao
interface MetroLineDao {
    @Query("select * from allmetrolines")
    suspend fun getMetroLines() : List<MetroLine>

    @Insert
    suspend fun addMetroLines(metroLine: MetroLine)

    @Delete
    suspend fun deleteMetrolLines(metroLine: MetroLine)

    @Query("delete from allmetrolines")
    suspend fun deleteAllMetroLines()

    @Query("select * from allmetrolines where id = :id")
    suspend fun getMetroLines(id: Int) : MetroLine
}