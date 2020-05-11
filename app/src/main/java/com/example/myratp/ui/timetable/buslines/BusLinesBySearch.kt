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