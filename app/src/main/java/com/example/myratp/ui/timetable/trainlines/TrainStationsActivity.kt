package com.example.myratp.ui.timetable.trainlines

import android.content.Context
import android.graphics.Color
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.os.Build
import android.os.Bundle
import android.util.Log
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
import com.example.myratp.adapters.TrainStationAdapter
import com.example.myratp.data.AppDatabase
import com.example.myratp.data.StationsDao
import com.example.myratp.model.Station
import com.example.myratp.model.Type
import kotlinx.android.synthetic.main.activity_train_stations.*
import kotlinx.coroutines.runBlocking

class TrainStationsActivity : AppCompatActivity() {

    private var code: String? = ""
    private var idTrain: String? = ""
    private var stationDao: StationsDao? = null

    @RequiresApi(Build.VERSION_CODES.M)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_train_stations)

        code = intent.getStringExtra("code")
        idTrain = intent.getStringExtra("id")

        val toolbar: Toolbar = findViewById(R.id.toolbar_train_station)
        toolbar.setNavigationIcon(R.drawable.ic_arrow_back_white_24dp)
        toolbar.title = "Ligne : $code"
        toolbar.setTitleTextColor(Color.parseColor("#F8F7F2"))
        setSupportActionBar(toolbar)

        val recyclerviewTrainStation =
            findViewById<RecyclerView>(R.id.activities_recyclerview_train_station)
        recyclerviewTrainStation.layoutManager =
            LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)

        val database = Room.databaseBuilder(this, AppDatabase::class.java, "stationtrain")
            .build()
        stationDao = database.getStationsDao()

        val co = ""
        if (isNetworkConnected()) {
            runBlocking {
                val service = retrofit_train().create(TrainLinesBySearch::class.java)
                val resultat = service.getTrainStations("rers", "$code")
                resultat.result.stations.map {
                    val station = Station(
                        0,
                        it.name,
                        it.slug,
                        favoris = false,
                        id_ligne = "$idTrain",
                        correspondance = co,
                        type = Type.Train,
                        code = "$code"
                    )
                    stationDao?.addStations(station)
                }
                stationDao = database.getStationsDao()
                val trainStation = stationDao?.getStationsByLine("$idTrain")
                progress_bar_train_station.visibility = View.GONE
                recyclerviewTrainStation.adapter =
                    TrainStationAdapter(trainStation ?: emptyList(), "$code")
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