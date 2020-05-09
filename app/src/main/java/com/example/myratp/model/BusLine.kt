package com.example.myratp.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "allbuslines")
data class BusLine(@PrimaryKey(autoGenerate = true) val id_bus: Int,
                     val code: String,
                     val name: String,
                     val direction: String,
                     val id: Int){
    companion object{
        val all = (1..10).map{
            TrainLine(it, "$it", "$it", "$it", 0)
        }.toMutableList()
    }
}