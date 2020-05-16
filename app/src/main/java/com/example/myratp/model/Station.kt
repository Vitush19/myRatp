package com.example.myratp.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "allstations")
data class Station(
    @PrimaryKey(autoGenerate = true) val id_station: Int,
    val name: String,
    val slug: String,
    var favoris: Boolean,
    val id_ligne: String?
) {

}