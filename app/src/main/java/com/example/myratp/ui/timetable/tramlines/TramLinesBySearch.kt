package com.example.myratp.ui.timetable.tramlines

import com.example.myratp.ui.timetable.noctilien.GetTraffic
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

    @GET("traffic/{type}")
    suspend fun getTrafficNocti(
        @Path("type") type: String
    ): GetTraffic

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

data class GetTraffic(val result: TramTraffic = TramTraffic())
data class TramTraffic(val tramways: List<TramInfo> = emptyList())
data class TramInfo(
    val line: String = "",
    val slug: String = "",
    val title: String = "",
    val message: String = ""
)