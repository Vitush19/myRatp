package com.example.myratp.ui.timetable.buslines

import retrofit2.http.GET

interface TrainLinesBySearch {
    @GET("lines/rers")  //ajout des headers éventuellement
    suspend fun getlistTrainLine(
        //@Path("type") type: String
    ): TrainLinesResponse

}

data class TrainLinesResponse(val result : Destination = Destination())
data class Destination(val rers : List<RerS> = emptyList())
data class RerS(val code : String = "",
                 val name : String = "",
                 val directions : String = "",
                 val id : String = "")