package com.example.myratp.model


import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "allnoctilien")
data class Noctilien(
    @PrimaryKey(autoGenerate = true) val id_noctilien: Int,
    val code: String,
    val name: String,
    val direction: String,
    val id: String
) {
    companion object {
        val all = (1..10).map {
            Noctilien(it, "$it", "$it", "$it", "$it")
        }.toMutableList()
    }
}