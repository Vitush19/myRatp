package com.example.myratp.ui.timetable.noctilien

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
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.room.Room
import com.example.myratp.R
import com.example.myratp.adapters.noctilien.NoctilienStationAdapter
import com.example.myratp.data.AppDatabase
import com.example.myratp.data.StationsDao
import com.example.myratp.model.Station
import com.example.myratp.model.Type
import com.example.myratp.utils.retrofit
import kotlinx.android.synthetic.main.activity_nocti_stations.*
import kotlinx.coroutines.runBlocking
import java.util.*

class NoctilienStationsActivity : AppCompatActivity() {

    private var code: String? = ""
    private var idNocti: String? = ""
    private var stationDao: StationsDao? = null

    @RequiresApi(Build.VERSION_CODES.M)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_nocti_stations)

        code = intent.getStringExtra("code")
        idNocti = intent.getStringExtra("id")

        val toolbar: Toolbar = findViewById(R.id.toolbar_nocti_station)
        toolbar.setNavigationIcon(R.drawable.ic_arrow_back_white_24dp)
        toolbar.title = "Ligne : $code"
        toolbar.setTitleTextColor(Color.parseColor("#F8F7F2"))
        setSupportActionBar(toolbar)

        val recyclerviewNoctiStation =
            findViewById<RecyclerView>(R.id.activities_recyclerview_nocti_station)
        recyclerviewNoctiStation.layoutManager =
            LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)

        val database = Room.databaseBuilder(this, AppDatabase::class.java, "stationnoctilien")
            .build()
        stationDao = database.getStationsDao()

        val notif = findViewById<CardView>(R.id.cardview_noctilien_station)
        val rightNow: Calendar = Calendar.getInstance()
        val currentHourIn24Format: Int = rightNow.get(Calendar.HOUR_OF_DAY)
        if (currentHourIn24Format > 22 || currentHourIn24Format < 6) {
            notif.visibility = View.GONE
        }

        val co = ""
        if (isNetworkConnected()) {
            runBlocking {
                val service = retrofit().create(NoctiLineBySearch::class.java)
                val resultat = service.getNoctiStations("noctiliens", "$code")
                resultat.result.stations.map {
                    val station =
                        Station(
                            0,
                            it.name,
                            it.slug,
                            favoris = false,
                            id_ligne = "$idNocti",
                            correspondance = co,
                            type = Type.Noctilien,
                            code = "$code"
                        )
                    stationDao?.addStations(station)
                }
                stationDao = database.getStationsDao()
                val station = stationDao?.getStationsByLine("$idNocti")
                progress_bar_nocti_station.visibility = View.GONE
                recyclerviewNoctiStation.adapter =
                    NoctilienStationAdapter(
                        station ?: emptyList(),
                        "$code"
                    )
            }
        } else {
            val toastView = layoutInflater.inflate(
                R.layout.custom_toast,
                findViewById(R.id.layout_toast_custom)
            )
            with(Toast(applicationContext)) {
                duration = Toast.LENGTH_SHORT
                view = toastView
                show()
            }
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