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

}

data class MetroLinesResponse(val result : Destination = Destination())
data class Destination(val metros : List<MetroS> = emptyList())
data class MetroS(val code : String = "",
                     val name : String = "",
                     val directions : String = "",
                     val id : Int = 0)
