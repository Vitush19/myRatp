package com.example.myratp.ui.timetable.buslines

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
import com.example.myratp.adapters.BusStationAdapter
import com.example.myratp.data.AppDatabase
import com.example.myratp.data.StationsDao
import com.example.myratp.model.Station
import kotlinx.android.synthetic.main.activity_bus_time.progress_bar
import kotlinx.coroutines.runBlocking

class BusStationsActivity : AppCompatActivity() {

    private var code: String? = ""
    private var idBus: Int? = 0
    private var stationDao: StationsDao? = null

    @RequiresApi(Build.VERSION_CODES.M)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_bus_stations)

        code = intent.getStringExtra("code")
        idBus = intent.getIntExtra("id", 0)

        val toolbar: Toolbar = findViewById(R.id.toolbar_bus_station)
        toolbar.setNavigationIcon(R.drawable.ic_arrow_back_white_24dp)
        toolbar.title = "Ligne : $code"
        setSupportActionBar(toolbar)

        val recyclerviewBusStation =
            findViewById<RecyclerView>(R.id.activities_recyclerview_bus_station)
        recyclerviewBusStation.layoutManager =
            LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)

        val database = Room.databaseBuilder(this, AppDatabase::class.java, "allbuslines")
            .build()
        stationDao = database.getStationsDao()
        val co = ""
        if (isNetworkConnected()) {
            runBlocking {
                if (stationDao!!.getStationsByLine("$code").isEmpty()) {
                    val service = retrofit_bus().create(BusLinesBySearch::class.java)
                    val resultat = service.getBusStations("buses", "$code")
                    resultat.result.stations.map {
                        val station =
                            Station(0, it.name, it.slug, favoris = false, id_ligne = "$idBus", correspondance = co)
                        stationDao?.addStations(station)
                    }
                    stationDao = database.getStationsDao()
                    val schedule = stationDao?.getStations()
                    progress_bar.visibility = View.GONE
                    recyclerviewBusStation.adapter =
                        BusStationAdapter(schedule ?: emptyList(), "$code")
                }
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