package com.example.myratp.ui.timetable.metrolines

import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.room.Room
import com.example.myratp.R
import com.example.myratp.adapters.BusStationAdapter
import com.example.myratp.data.AppDatabase
import com.example.myratp.data.StationsDao
import com.example.myratp.model.Station
import com.example.myratp.ui.timetable.buslines.BusLinesBySearch
import com.example.myratp.ui.timetable.buslines.retrofit_bus
import kotlinx.android.synthetic.main.activity_bus_stations.*
import kotlinx.android.synthetic.main.activity_bus_time.*
import kotlinx.android.synthetic.main.activity_bus_time.progress_bar
import kotlinx.coroutines.runBlocking

class BusStationsActivity : AppCompatActivity(){

    private var code : String? = ""
    private var id_bus : String? = ""
    private var stationDao : StationsDao? = null

    @RequiresApi(Build.VERSION_CODES.M)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_bus_stations)

        code = intent.getStringExtra("code")
        id_bus = intent.getStringExtra("id")

        var recyclerview_bus_station = findViewById(R.id.activities_recyclerview_bus_station) as RecyclerView
        recyclerview_bus_station.layoutManager = LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)

        val database = Room.databaseBuilder(this, AppDatabase::class.java, "allbuslines")
            .build()
        stationDao = database.getStationsDao()
        if(isNetworkConnected()){
            runBlocking {
                stationDao?.deleteAllStations()
                val service = retrofit_bus().create(BusLinesBySearch::class.java)
                val resultat = service.getBusStations("buses", "$code")
                resultat.result.stations.map {
                    val station = Station(0, it.name, it.slug, favoris = false, id_ligne = "$id_bus")
                    Log.d("CCC", "$station")
                    stationDao?.addStations(station)
                }
                stationDao = database.getStationsDao()
                val s = stationDao?.getStations()
                progress_bar.visibility = View.GONE
                recyclerview_bus_station.adapter =
                    BusStationAdapter(s ?: emptyList(), "$code")
            }
        }
        else{
            Toast.makeText(this, "Vérifiez votre connexion internet et réessayez à nouveau", Toast.LENGTH_SHORT).show()
        }
    }

    @RequiresApi(Build.VERSION_CODES.M)
    private fun isNetworkConnected(): Boolean {
        var result = false
        val connectivityManager = applicationContext.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager?
        connectivityManager?.let{
            it.getNetworkCapabilities(connectivityManager.activeNetwork)?.apply {
                result = when {
                    hasTransport(NetworkCapabilities.TRANSPORT_WIFI) -> true
                    hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR) -> true
                    else -> false
                }
            }
        }
        return result
    }
}