package com.example.myratp.ui.timetable.noctilien

import com.example.myratp.model.MetroLine
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Path

interface NoctiLineBySearch {
    @GET("lines/noctiliens")
    suspend fun getlistNoctiLine(
    ): NoctiLinesResponse

    @GET("stations/{type}/{code}")
    suspend fun getNoctiStations(
        @Path("type") type: String,
        @Path("code") code: String
    ): NoctiStationsResponse

    @GET("traffic/{type}")
    suspend fun getTrafficNocti(
        @Path("type") type: String
    ): GetTraffic

    @GET("schedules/{type}/{line}/{station}/{way}")
    suspend fun getScheduleNocti(
        @Path("type") type: String,
        @Path("line") line: String,
        @Path("station") station: String,
        @Path("way") way: String
    ): GetScheduleNocti
}

data class NoctiLinesResponse(val result: Destination = Destination())
data class Destination(val noctiliens: List<NoctiS> = emptyList())
data class NoctiS(
    val code: String = "",
    val name: String = "",
    val directions: String = "",
    val id: String = ""
)

data class NoctiStationsResponse(val result: NoctiStations = NoctiStations())
data class NoctiStations(val stations: List<NoctiStationName> = emptyList())
data class NoctiStationName(val name: String = "", val slug: String = "")

data class GetTraffic(val result: NoctiTraffic = NoctiTraffic())
data class NoctiTraffic(val noctiliens: List<NoctiInfo> = emptyList())
data class NoctiInfo(
    val line: String = "",
    val slug: String = "",
    val title: String = "",
    val message: String = ""
)

data class GetScheduleNocti(val result: SchedulesNocti = SchedulesNocti())
data class SchedulesNocti(val schedules: List<ScheduleN> = emptyList())
data class ScheduleN(val code: String = "", val message: String = "", val destination: String = "")