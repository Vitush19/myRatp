package com.example.myratp.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "allmetrolines")
data class MetroLine(@PrimaryKey(autoGenerate = true) val id_metro: Int,
                     val code: String,
                     val name: String,
                     val direction: String,
                     val id: Int){
    companion object{
        val all = (1..10).map{
            MetroLine(it, "$it", "$it", "$it", 0)
        }.toMutableList()
    }
}