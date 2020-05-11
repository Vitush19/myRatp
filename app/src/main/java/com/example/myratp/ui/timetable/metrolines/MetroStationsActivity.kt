package com.example.myratp.ui.timetable.metrolines

import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.room.Room
import com.example.myratp.R
import com.example.myratp.adapters.StationAdapter
import com.example.myratp.data.AppDatabase
import com.example.myratp.data.StationsDao
import com.example.myratp.model.Station
import kotlinx.coroutines.runBlocking

class MetroStationsActivity : AppCompatActivity(){

    private var code : String? = ""
    private var stationDao : StationsDao? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_metro_stations)

        code = intent.getStringExtra("code")

        var recyclerview_metro_station = findViewById(R.id.activities_recyclerview_metro_station) as RecyclerView
        recyclerview_metro_station.layoutManager = LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)

        val database = Room.databaseBuilder(this, AppDatabase::class.java, "allmetrolines")
            .build()
        stationDao = database.getStationsDao()

        runBlocking {
            stationDao?.deleteAllStations()
            val service = retrofit().create(MetroLinesBySearch::class.java)
            val resultat = service.getMetroStations("metros", "$code")
            resultat.result.stations.map {
                val station = Station(0, it.name, it.slug, favoris = false)
                Log.d("CCC", "$station")
                stationDao?.addStations(station)
            }
            stationDao = database.getStationsDao()
            val s = stationDao?.getStations()
            //val test = bs.isNullOrEmpty()
            recyclerview_metro_station.adapter =
                StationAdapter(s ?: emptyList())
        }


    }
}