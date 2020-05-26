package com.example.myratp.ui.timetable.metrolines

import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.os.Build
import android.os.Bundle
import android.view.MenuItem
import android.view.View
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.room.Room
import com.example.myratp.R
import com.example.myratp.adapters.MetroStationAdapter
import com.example.myratp.data.AppDatabase
import com.example.myratp.data.StationsDao
import com.example.myratp.model.Station
import kotlinx.android.synthetic.main.activity_metro_stations.*
import kotlinx.coroutines.runBlocking

class MetroStationsActivity : AppCompatActivity() {

    private var code: String? = ""
    private var idMetro: Int? = 0
    private var stationDao: StationsDao? = null

    @RequiresApi(Build.VERSION_CODES.M)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_metro_stations)

        code = intent.getStringExtra("code")
        idMetro = intent.getIntExtra("id", 0)

        val toolbar: Toolbar = findViewById(R.id.toolbar_metro_station)
        toolbar.setNavigationIcon(R.drawable.ic_arrow_back_white_24dp)
        toolbar.title = "Ligne : $code"
        setSupportActionBar(toolbar)

        val recyclerviewMetroStation =
            findViewById<RecyclerView>(R.id.activities_recyclerview_metro_station)
        recyclerviewMetroStation.layoutManager =
            LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)

        val database = Room.databaseBuilder(this, AppDatabase::class.java, "stationmetro")
            .build()
        stationDao = database.getStationsDao()

//        val database_bis = Room.databaseBuilder(this, AppDatabase::class.java, "allmetrolines")
//            .build()
//        metroDao = database_bis.getMetroLineDao()
        if (isNetworkConnected()) {
            runBlocking {
                //stationDao?.deleteAllStations()
                //if(stationDao!!.getStationsByLine("$code").isEmpty()){
                var id = 0
                    val service = retrofit().create(MetroLinesBySearch::class.java)
                    val resultat = service.getMetroStations("metros", "$code")
                    resultat.result.stations.map {
                        var co = ""
                        val list_station = stationDao?.getStationsByName(it.name)
                        if(list_station!!.isNotEmpty()){
                            for(x in list_station.indices){
                                if("$code" != list_station[x].id_ligne){
                                    val newLine: String = list_station[x].id_ligne
                                    co = co + "$newLine-"
                                }
                                if("$code" == list_station[x].id_ligne){
                                    id = list_station[x].id_station
                                }
                            }
                        }
                        val station =
                            Station(id, it.name, it.slug, favoris = false, id_ligne = "$code", correspondance = co)

                        stationDao?.updateStations(station)
                    }
                //}
                stationDao = database.getStationsDao()

                val s = stationDao?.getStationsByLine("$code")
                progress_bar.visibility = View.GONE
                recyclerviewMetroStation.adapter =
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

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            android.R.id.home -> {
                finish()
                true
            }
            else -> super.onOptionsItemSelected(item)
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