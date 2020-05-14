package com.example.myratp.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "allbuslines")
data class BusLine(
    @PrimaryKey(autoGenerate = true) val id_bus: Int,
    val code: String,
    val name: String,
    val direction: String,
    val id: String
) {
    companion object {
        val all = (1..10).map {
            BusLine(it, "$it", "$it", "$it", "$it")
        }.toMutableList()
    }
}