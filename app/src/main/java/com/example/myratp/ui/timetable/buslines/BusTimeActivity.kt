package com.example.myratp.ui.timetable.buslines

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.room.Room
import com.example.myratp.R
import com.example.myratp.adapters.BusLinesAdapter
import com.example.myratp.data.AppDatabase
import com.example.myratp.data.BusLineDao
import com.example.myratp.model.BusLine
import kotlinx.coroutines.runBlocking

class BusTimeActivity : AppCompatActivity() {

    private var busLineDao : BusLineDao? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_bus_time)

        var recyclerview_bus = findViewById(R.id.activities_recyclerview_bus) as RecyclerView
        recyclerview_bus.layoutManager = LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)

        val database = Room.databaseBuilder(this, AppDatabase::class.java, "allbuslines")
            .build()
        busLineDao = database.getBusLineDao()

        runBlocking {
            busLineDao?.deleteAllBusLines()
            val service = retrofit_bus().create(BusLinesBySearch::class.java)
            val resultat = service.getlistBusLine()
            resultat.result.buses.map {
                val bus = BusLine(0, it.code, it.name, it.directions, it.id)
                Log.d("CCC", "$bus")
                busLineDao?.addBusLines(bus)
            }
            busLineDao = database.getBusLineDao()
            //val bs = busLineDao?.getBusLines()
            val bs = busLineDao?.getBusLines()
            val test = bs.isNullOrEmpty()
            Log.d("CCC", "$test")
            recyclerview_bus.adapter =
                BusLinesAdapter(bs ?: emptyList())
        }
    }
}