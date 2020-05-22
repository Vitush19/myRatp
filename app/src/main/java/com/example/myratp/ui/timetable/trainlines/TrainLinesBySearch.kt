package com.example.myratp.ui.timetable.trainlines

import retrofit2.http.GET
import retrofit2.http.Path

interface TrainLinesBySearch {
    @GET("lines/rers")
    suspend fun getlistTrainLine(
    ): TrainLinesResponse

    @GET("stations/{type}/{code}")
    suspend fun getTrainStations(
        @Path("type") type: String,
        @Path("code") code: String
    ): TrainStationsResponse

}

data class TrainLinesResponse(val result: Destination = Destination())
data class Destination(val rers: List<RerS> = emptyList())
data class RerS(
    val code: String = "",
    val name: String = "",
    val directions: String = "",
    val id: String = ""
)

data class TrainStationsResponse(val result: TrainStations = TrainStations())
data class TrainStations(val stations: List<TrainStationName> = emptyList())
data class TrainStationName(val name: String = "", val slug: String = "")