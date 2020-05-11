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
import com.example.myratp.ui.timetable.buslines.BusLinesBySearch
import com.example.myratp.ui.timetable.buslines.TrainLinesBySearch
import com.example.myratp.ui.timetable.buslines.retrofit_bus
import com.example.myratp.ui.timetable.buslines.retrofit_train
import kotlinx.coroutines.runBlocking

class TrainStationsActivity : AppCompatActivity(){

    private var code : String? = ""
    private var stationDao : StationsDao? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_train_stations)

        code = intent.getStringExtra("code")

        var recyclerview_train_station = findViewById(R.id.activities_recyclerview_train_station) as RecyclerView
        recyclerview_train_station.layoutManager = LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)

        val database = Room.databaseBuilder(this, AppDatabase::class.java, "allbuslines")
            .build()
        stationDao = database.getStationsDao()

        runBlocking {
            stationDao?.deleteAllStations()
            val service = retrofit_train().create(TrainLinesBySearch::class.java)
            val resultat = service.getTrainStations("rers", "$code")
            resultat.result.stations.map {
                val station = Station(0, it.name, it.slug, favoris = false)
                Log.d("CCC", "$station")
                stationDao?.addStations(station)
            }
            stationDao = database.getStationsDao()
            val s = stationDao?.getStations()
            //val test = bs.isNullOrEmpty()
            recyclerview_train_station.adapter =
                StationAdapter(s ?: emptyList())
        }


    }
}