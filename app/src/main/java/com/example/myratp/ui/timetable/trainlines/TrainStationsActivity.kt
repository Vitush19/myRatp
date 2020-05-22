package com.example.myratp.ui.timetable.trainlines

import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.room.Room
import com.example.myratp.R
import com.example.myratp.adapters.TrainStationAdapter
import com.example.myratp.data.AppDatabase
import com.example.myratp.data.StationsDao
import com.example.myratp.model.Station
import kotlinx.coroutines.runBlocking

class TrainStationsActivity : AppCompatActivity() {

    private var code: String? = ""
    private var id_train: String? = ""
    private var stationDao: StationsDao? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_train_stations)

        code = intent.getStringExtra("code")
        id_train = intent.getStringExtra("id")

        var recyclerview_train_station =
            findViewById(R.id.activities_recyclerview_train_station) as RecyclerView
        recyclerview_train_station.layoutManager =
            LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)

        val database = Room.databaseBuilder(this, AppDatabase::class.java, "allbuslines")
            .build()
        stationDao = database.getStationsDao()

        runBlocking {
            stationDao?.deleteAllStations()
            val service = retrofit_train().create(TrainLinesBySearch::class.java)
            val resultat = service.getTrainStations("rers", "$code")
            resultat.result.stations.map {
                val station = Station(0, it.name, it.slug, favoris = false, id_ligne = "$id_train")
                Log.d("CCC", "$station")
                stationDao?.addStations(station)
            }
            stationDao = database.getStationsDao()
            val s = stationDao?.getStations()
            recyclerview_train_station.adapter =
                TrainStationAdapter(s ?: emptyList())
        }


    }
}