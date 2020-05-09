package com.example.myratp.ui.timetable.buslines

import retrofit2.http.GET

interface BusLinesBySearch {
    @GET("lines/buses")  //ajout des headers éventuellement
    suspend fun getlistBusLine(
        //@Path("type") type: String
    ): BusLinesResponse

}

data class BusLinesResponse(val result : Arrive = Arrive())
data class Arrive(val buses : List<BuseS> = emptyList())
data class BuseS(val code : String = "",
                  val name : String = "",
                  val directions : String = "",
                  val id : Int = 0)