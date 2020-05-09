package com.example.myratp.data

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import com.example.myratp.model.TrainLine

@Dao
interface TrainLineDao {
    @Query("select * from alltrainlines")
    suspend fun getTrainLines() : List<TrainLine>

    @Insert
    suspend fun addTrainLines(busLine: TrainLine)

    @Delete
    suspend fun deleteTrainlLines(busLine: TrainLine)

    @Query("delete from alltrainlines")
    suspend fun deleteAllTrainLines()

    @Query("select * from alltrainlines where id = :id")
    suspend fun getTrainLines(id: Int) : TrainLine
}