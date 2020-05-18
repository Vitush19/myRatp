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
import com.example.myratp.adapters.MetroStationAdapter
import com.example.myratp.data.AppDatabase
import com.example.myratp.data.MetroLineDao
import com.example.myratp.data.StationsDao
import com.example.myratp.model.Station
import kotlinx.android.synthetic.main.activity_metro_stations.*
import kotlinx.coroutines.runBlocking

class MetroStationsActivity : AppCompatActivity() {

    private var code: String? = ""
    private var id_metro: String? = ""
    private var stationDao: StationsDao? = null
    private var metroDao: MetroLineDao? = null

    @RequiresApi(Build.VERSION_CODES.M)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_metro_stations)

        code = intent.getStringExtra("code")
        id_metro = intent.getStringExtra("id")

        var recyclerview_metro_station =
            findViewById(R.id.activities_recyclerview_metro_station) as RecyclerView
        recyclerview_metro_station.layoutManager =
            LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)

        val database = Room.databaseBuilder(this, AppDatabase::class.java, "allstations")
            .build()
        stationDao = database.getStationsDao()

//        val database_bis = Room.databaseBuilder(this, AppDatabase::class.java, "allmetrolines")
//            .build()
//        metroDao = database_bis.getMetroLineDao()
        if (isNetworkConnected()) {
            runBlocking {
                stationDao?.deleteAllStations()
                val service = retrofit().create(MetroLinesBySearch::class.java)
                val resultat = service.getMetroStations("metros", "$code")
                resultat.result.stations.map {
//
                    val station =
                        Station(0, it.name, it.slug, favoris = false, id_ligne = code)
                    Log.d("CCC", "$station")
//                    val check = stationDao?.getStations()
//                for (x in check!!.indices){
//                    if(check[x].name == station.name){
//                        val a = check[x].id_ligne
//                        val x = station.stock_lignes?.plus(a)
//                        Log.d("HHH", "$station.stock_lines")
//                    }
//                }
                    stationDao?.addStations(station)
                }
                stationDao = database.getStationsDao()


                val s = stationDao?.getStations()
                progress_bar.visibility = View.GONE
                recyclerview_metro_station.adapter =
                    MetroStationAdapter(s ?: emptyList(), "$code")
            }
        } else {
            Toast.makeText(
                this,
                "Vérifiez votre connexion internet et réessayez à nouveau",
                Toast.LENGTH_SHORT
            ).show()
        }

    }

    @RequiresApi(Build.VERSION_CODES.M)
    private fun isNetworkConnected(): Boolean {
        var result = false
        val connectivityManager =
            applicationContext.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager?
        connectivityManager?.let {
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