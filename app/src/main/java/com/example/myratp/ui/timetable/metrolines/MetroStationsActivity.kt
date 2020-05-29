package com.example.myratp.ui.timetable.metrolines

import android.content.Context
import android.graphics.Color
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
import com.example.myratp.model.Type
import kotlinx.android.synthetic.main.activity_metro_stations.*
import kotlinx.coroutines.runBlocking

class MetroStationsActivity : AppCompatActivity() {

    private var code: String? = ""
    private var idMetro: Int? = 0
    private var stationDao: StationsDao? = null
    private var newFavoris: Boolean = false

    @RequiresApi(Build.VERSION_CODES.M)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_metro_stations)

        code = intent.getStringExtra("code")
        idMetro = intent.getIntExtra("id", 0)

        val toolbar: Toolbar = findViewById(R.id.toolbar_metro_station)
        toolbar.setNavigationIcon(R.drawable.ic_arrow_back_white_24dp)
        toolbar.setTitleTextColor(Color.parseColor("#F8F7F2"))
        toolbar.title = "Ligne : $code"
        setSupportActionBar(toolbar)

        val recyclerviewMetroStation =
            findViewById<RecyclerView>(R.id.activities_recyclerview_metro_station)
        recyclerviewMetroStation.layoutManager =
            LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)

        val database = Room.databaseBuilder(this, AppDatabase::class.java, "stationmetro")
            .build()
        stationDao = database.getStationsDao()

        if (isNetworkConnected()) {
            runBlocking {
                var id = 0
                val service = retrofit().create(MetroLinesBySearch::class.java)
                val resultat = service.getMetroStations("metros", "$code")
                resultat.result.stations.map {
                    var co = ""
                    val listStation = stationDao?.getStationsByName(it.name)
                    if (listStation!!.isNotEmpty()) {
                        for (x in listStation.indices) {
                            if ("$code" != listStation[x].id_ligne) {
                                val newLine: String = listStation[x].id_ligne
                                newFavoris = listStation[x].favoris
                                co += "$newLine-"
                            }
                            if ("$code" == listStation[x].id_ligne) {
                                id = listStation[x].id_station
                                newFavoris = listStation[x].favoris
                            }
                        }
                    }
                    val station =
                        Station(
                            id,
                            it.name,
                            it.slug,
                            favoris = newFavoris,
                            id_ligne = "$code",
                            correspondance = co,
                            type = Type.Metro,
                            code = "$code"
                        )

                    stationDao?.updateStations(station)
                }
                //}
                stationDao = database.getStationsDao()

                val s = stationDao?.getStationsByLine("$code")
                progress_bar_metro_station.visibility = View.GONE
                recyclerviewMetroStation.adapter =
                    MetroStationAdapter(s ?: emptyList(), "$code")
            }
        } else {
            Toast.makeText(
                this,
                getString(R.string.Connexion_internet),
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