package com.example.myratp.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "allschedule")
data class Schedule(
    @PrimaryKey(autoGenerate = true) val id_schedule: Int,
    val message: String,
    val destination: String
) {

}