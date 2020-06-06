package com.example.myratp.ui.timetable.metrolines

import retrofit2.http.GET
import retrofit2.http.Path

interface MetroLinesBySearch {
    @GET("lines/metros")
    suspend fun getlistMetroLine(
    ): MetroLinesResponse

    @GET("stations/{type}/{code}")
    suspend fun getMetroStations(
        @Path("type") type: String,
        @Path("code") code: String
    ): MetroStationsResponse

    @GET("traffic/{type}")
    suspend fun getTrafficMetro(
        @Path("type") type: String
    ): GetTraffic

    @GET("schedules/{type}/{line}/{station}/{way}")
    suspend fun getScheduleMetro(
        @Path("type") type: String,
        @Path("line") line: String,
        @Path("station") station: String,
        @Path("way") way: String
    ): GetScheduleMetro
}

data class MetroLinesResponse(val result: Destination = Destination())
data class Destination(val metros: List<MetroS> = emptyList())
data class MetroS(
    val code: String = "",
    val name: String = "",
    val directions: String = "",
    val id: String = ""
)

data class MetroStationsResponse(val result: MetroStations = MetroStations())
data class MetroStations(val stations: List<MetroStationName> = emptyList())
data class MetroStationName(val name: String = "", val slug: String = "")

data class GetTraffic(val result: MetroTraffic = MetroTraffic())
data class MetroTraffic(val metros: List<MetroInfo> = emptyList())
data class MetroInfo(
    val line: String = "",
    val slug: String = "",
    val title: String = "",
    val message: String = ""
)

data class GetScheduleMetro(val result: SchedulesMetro = SchedulesMetro())
data class SchedulesMetro(val schedules: List<ScheduleM> = emptyList())
data class ScheduleM(val code: String = "", val message: String = "", val destination: String = "")