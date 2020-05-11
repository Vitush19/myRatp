package com.example.myratp.ui.timetable.metrolines

import com.example.myratp.model.MetroLine
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Path

interface MetroLinesBySearch {
    @GET("lines/metros")  //ajout des headers éventuellement
    suspend fun getlistMetroLine(
        //@Path("type") type: String
    ): MetroLinesResponse

    @GET("stations/{type}/{code}")
    suspend fun getMetroStations(
        @Path("type") type:String,
        @Path("code") code:String
    ): MetroStationsResponse
}

data class MetroLinesResponse(val result : Destination = Destination())
data class Destination(val metros : List<MetroS> = emptyList())
data class MetroS(val code : String = "",
                     val name : String = "",
                     val directions : String = "",
                     val id : String = "")

data class MetroStationsResponse(val result: MetroStations = MetroStations())
data class MetroStations(val stations:List<MetroStationName> = emptyList())
data class MetroStationName(val name:String="",val slug:String="")