package com.example.myratp.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "alltraffic")
data class Traffic(@PrimaryKey(autoGenerate = true) val id_traffic: Int,
                   val line: String,
                   val slug: String,
                   val title: String,
                   val message: String
){

}