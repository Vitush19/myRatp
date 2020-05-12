package com.example.myratp.ui.timetable.buslines

import retrofit2.http.GET
import retrofit2.http.Path

interface BusLinesBySearch {
    @GET("lines/buses")  //ajout des headers éventuellement
    suspend fun getlistBusLine(
        //@Path("type") type: String
    ): BusLinesResponse

    @GET("stations/{type}/{code}")
    suspend fun getBusStations(
        @Path("type") type:String,
        @Path("code") code:String
    ): BusStationsResponse

    @GET("schedules/{type}/{line}/{station}/{way}")
    suspend fun getScheduleMetro(
        @Path("type") type:String,
        @Path("line") line:String,
        @Path("station") station:String,
        @Path("way") way:String
    ):GetScheduleBus
}

data class BusLinesResponse(val result : Arrive = Arrive())
data class Arrive(val buses : List<BuseS> = emptyList())
data class BuseS(val code : String = "",
                  val name : String = "",
                  val directions : String = "",
                  val id : String = "")

data class BusStationsResponse(val result: BusStations = BusStations())
data class BusStations(val stations:List<BusStationName> = emptyList())
data class BusStationName(val name:String="",val slug:String="")

data class GetScheduleBus(val result: SchedulesBus = SchedulesBus())
data class SchedulesBus(val schedules:List<ScheduleB> = emptyList())
data class ScheduleB(val code:String="",val message:String="",val destination:String="")