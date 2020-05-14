package com.example.myratp.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "alltrainlines")
data class TrainLine(
    @PrimaryKey(autoGenerate = true) val id_train: Int,
    val code: String,
    val name: String,
    val direction: String,
    val id: String
) {
    companion object {
        val all = (1..10).map {
            TrainLine(it, "$it", "$it", "$it", "$it")
        }.toMutableList()
    }
}