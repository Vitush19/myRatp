package com.example.myratp.model

import android.os.Parcelable
import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.android.parcel.Parcelize

enum class Type{
    Metro, Bus, Train, Tram, Noctilien
}

@Parcelize
@Entity(tableName = "allstations")
data class Station(
    @PrimaryKey(autoGenerate = true) val id_station: Int,
    val name: String,
    val slug: String,
    var favoris: Boolean,
    val id_ligne: String,
    val correspondance: String,
    val type: Type,
    val code: String

) : Parcelable {

    companion object {
        val all = (1..10).map {
            Station(it, "$it", "$it", false, "$it","$it", Type.Metro, "$it")
        }.toMutableList()
    }

}