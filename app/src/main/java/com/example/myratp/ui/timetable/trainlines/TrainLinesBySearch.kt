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

    @GET("schedules/{type}/{line}/{station}/{way}")
    suspend fun getScheduleTrain(
        @Path("type") type: String,
        @Path("line") line: String,
        @Path("station") station: String,
        @Path("way") way: String
    ): GetScheduleTrain
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

data class GetScheduleTrain(val result: SchedulesTrain = SchedulesTrain())
data class SchedulesTrain(val schedules: List<ScheduleT> = emptyList())
data class ScheduleT(val code: String = "", val message: String = "", val destination: String = "")