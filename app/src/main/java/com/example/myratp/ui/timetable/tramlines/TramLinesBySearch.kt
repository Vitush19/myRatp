package com.example.myratp.ui.timetable.tramlines

import retrofit2.http.GET
import retrofit2.http.Path

interface TramLinesBySearch {
    @GET("lines/tramways")
    suspend fun getlistTramLine(
    ): TramLinesResponse

    @GET("stations/{type}/{code}")
    suspend fun getTramStations(
        @Path("type") type: String,
        @Path("code") code: String
    ): TramStationsResponse

    @GET("schedules/{type}/{line}/{station}/{way}")
    suspend fun getScheduleTram(
        @Path("type") type: String,
        @Path("line") line: String,
        @Path("station") station: String,
        @Path("way") way: String
    ): GetScheduleTram
}

data class TramLinesResponse(val result: Arrive = Arrive())
data class Arrive(val tramways: List<TramS> = emptyList())
data class TramS(
    val code: String = "",
    val name: String = "",
    val directions: String = "",
    val id: String = ""
)

data class TramStationsResponse(val result: TramStations = TramStations())
data class TramStations(val stations: List<TramStationName> = emptyList())
data class TramStationName(val name: String = "", val slug: String = "")

data class GetScheduleTram(val result: SchedulesTram = SchedulesTram())
data class SchedulesTram(val schedules: List<ScheduleT> = emptyList())
data class ScheduleT(val code: String = "", val message: String = "", val destination: String = "")