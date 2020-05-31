package com.example.myratp.data

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import com.example.myratp.model.TramLine

@Dao
interface TramLineDao {
    @Query("select * from alltramlines")
    suspend fun getTramLines(): List<TramLine>

    @Insert
    suspend fun addTramLines(tramLine: TramLine)

    @Delete
    suspend fun deleteTramlLines(tramLine: TramLine)

    @Query("delete from alltramlines")
    suspend fun deleteAllTramLines()

    @Query("select * from alltramlines where id = :id")
    suspend fun getTramLines(id: Int): TramLine
}