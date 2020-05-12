package com.example.myratp.data

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import com.example.myratp.model.Schedule

@Dao
interface ScheduleDao {
    @Query("select * from allschedule")
    suspend fun getSchedule() : List<Schedule>

    @Insert
    suspend fun addSchedule(schedule: Schedule)

    @Delete
    suspend fun deleteSchedule(schedule: Schedule)

    @Query("delete from allschedule")
    suspend fun deleteAllSchedule()

    @Query("select * from allschedule where id_schedule = :id")
    suspend fun getSchedule(id: Int) : Schedule
}