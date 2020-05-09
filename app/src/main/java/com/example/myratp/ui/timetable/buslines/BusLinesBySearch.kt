package com.example.myratp.ui.timetable.buslines

import retrofit2.http.GET

interface BusLinesBySearch {
    @GET("lines/buses")  //ajout des headers éventuellement
    suspend fun getlistMetroLine(
        //@Path("type") type: String
    ): BusLinesResponse

}

data class BusLinesResponse(val result : Destination = Destination())
data class Destination(val buses : List<BuseS> = emptyList())
data class BuseS(val code : String = "",
                  val name : String = "",
                  val directions : String = "",
                  val id : Int = 0)