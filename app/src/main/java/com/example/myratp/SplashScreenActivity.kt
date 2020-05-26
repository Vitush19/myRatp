package com.example.myratp

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.util.Log
import androidx.room.Room
import com.example.myratp.data.*
import com.example.myratp.model.*
import com.example.myratp.ui.timetable.buslines.BusLinesBySearch
import com.example.myratp.ui.timetable.buslines.retrofit_bus
import com.example.myratp.ui.timetable.metrolines.MetroLinesBySearch
import com.example.myratp.ui.timetable.metrolines.retrofit
import com.example.myratp.ui.timetable.trainlines.TrainLinesBySearch
import com.example.myratp.ui.timetable.trainlines.retrofit_train
import kotlinx.coroutines.runBlocking

class SplashScreenActivity : AppCompatActivity() {

    private lateinit var handler: Handler
    private var metroLineDao: MetroLineDao? = null
    private var trafficDao: TrafficDao? = null
    private var busLineDao: BusLineDao? = null
    private var stationDao: StationsDao? = null
    private var trainLineDao: TrainLineDao? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash_screen)

        val databaseMetro = Room.databaseBuilder(this, AppDatabase::class.java, "allmetrolines")
            .build()
        metroLineDao = databaseMetro.getMetroLineDao()

        val databaseTraffic = Room.databaseBuilder(this, AppDatabase::class.java, "alltraffic")
            .build()
        trafficDao = databaseTraffic.getTrafficDao()

        val databaseBus = Room.databaseBuilder(this, AppDatabase::class.java, "allbuslines")
            .build()
        busLineDao = databaseBus.getBusLineDao()

        val databaseStation = Room.databaseBuilder(this, AppDatabase::class.java, "stationmetro")
            .build()
        stationDao = databaseStation.getStationsDao()

        val databaseTrain = Room.databaseBuilder(this, AppDatabase::class.java, "alltrainlines")
            .build()
        trainLineDao = databaseTrain.getTrainLineDao()

        runBlocking {
            if(trafficDao!!.getTraffic().isEmpty()) {
                val service = retrofit().create(MetroLinesBySearch::class.java)
                val resultat = service.getTrafficMetro("metros")
                resultat.result.metros.map {
                    val traffic = Traffic(0, it.line, it.slug, it.title, it.message)
                    trafficDao?.addTraffic(traffic)
                }
            }

            if(metroLineDao!!.getMetroLines().isEmpty()){
                val service = retrofit().create(MetroLinesBySearch::class.java)
                val resultat = service.getlistMetroLine()
                resultat.result.metros.map {
                    val metro = MetroLine(0, it.code, it.name, it.directions, it.id)
                    metroLineDao?.addMetroLines(metro)
                }
            }

            if(busLineDao!!.getBusLines().isEmpty()){
                val service = retrofit_bus().create(BusLinesBySearch::class.java)
                val resultat = service.getlistBusLine()
                resultat.result.buses.map {
                    val bus = BusLine(0, it.code, it.name, it.directions, it.id)
                    if(bus.code != busLineDao?.getBusLinesByCode(it.code)?.code) {
                        busLineDao?.addBusLines(bus)
                    }
                }
            }

            if(trainLineDao!!.getTrainLines().isEmpty()){
                val service = retrofit_train().create(TrainLinesBySearch::class.java)
                val resultat = service.getlistTrainLine()
                resultat.result.rers.map {
                    val train = TrainLine(0, it.code, it.name, it.directions, it.id)
                    trainLineDao?.addTrainLines(train)
                }
            }

            for(x in 1..14){
                if(stationDao!!.getStationsByLine("$x").isEmpty()){
                    val service = retrofit().create(MetroLinesBySearch::class.java)
                    val resultat = service.getMetroStations("metros", "$x")
                    val co = ""
                    resultat.result.stations.map {
                        val station =
                            Station(0, it.name, it.slug, favoris = false, id_ligne = "$x", correspondance = co)
                        stationDao?.addStations(station)
                    }
                }
            }
            val directions = listOf("3b", "7b")
            for(x in directions){
                if(stationDao!!.getStationsByLine("$x").isEmpty()){
                    val service = retrofit().create(MetroLinesBySearch::class.java)
                    val resultat = service.getMetroStations("metros", "$x")
                    val co = ""
                    resultat.result.stations.map {
                        val station =
                            Station(0, it.name, it.slug, favoris = false, id_ligne = "$x", correspondance = co)
                        stationDao?.addStations(station)
                    }
                }
            }
        }

        handler = Handler()
        handler.postDelayed({
            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
            finish()
        }, 1000)
    }
}
