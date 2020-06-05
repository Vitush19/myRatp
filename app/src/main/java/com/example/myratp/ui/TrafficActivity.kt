package com.example.myratp.ui

import android.content.Context
import android.graphics.Color
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.os.Build
import android.os.Bundle
import android.view.MenuItem
import android.view.View
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.room.Room
import com.example.myratp.R
import com.example.myratp.adapters.TrafficAdapter
import com.example.myratp.data.AppDatabase
import com.example.myratp.data.TrafficDao
import com.example.myratp.model.Traffic
import com.example.myratp.utils.retrofit
import com.example.myratp.ui.timetable.metrolines.MetroLinesBySearch
import com.example.myratp.ui.timetable.trainlines.TrainLinesBySearch
import com.example.myratp.ui.timetable.tramlines.TramLinesBySearch
import com.google.android.material.bottomnavigation.BottomNavigationView
import kotlinx.android.synthetic.main.activity_traffic.*
import kotlinx.coroutines.async
import kotlinx.coroutines.runBlocking

class TrafficActivity : AppCompatActivity(), BottomNavigationView.OnNavigationItemSelectedListener {

    private lateinit var trafficDao: TrafficDao

    @RequiresApi(Build.VERSION_CODES.M)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_traffic)

        val toolbar: Toolbar = findViewById(R.id.toolbar_traffic)
        toolbar.setTitleTextColor(Color.parseColor("#F8F7F2"))
        toolbar.setNavigationIcon(R.drawable.ic_arrow_back_white_24dp)
        toolbar.title = "Traffic"
        setSupportActionBar(toolbar)

        val bottom = findViewById<BottomNavigationView>(R.id.bottom_navbar_traffic)

        val recyclerviewMetro = findViewById<RecyclerView>(R.id.activities_recyclerview_traffic)
        recyclerviewMetro.layoutManager =
            LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)

        val database = Room.databaseBuilder(this, AppDatabase::class.java, "alltraffic")
            .build()
        trafficDao = database.getTrafficDao()

        bottom.setOnNavigationItemSelectedListener(this)

        if (isNetworkConnected()) {
            runBlocking {

                trafficDao.deleteAllTraffic()
                val service = retrofit().create(MetroLinesBySearch::class.java)
                val resultat = service.getTrafficMetro("metros")
                resultat.result.metros.map {
                    val traffic = Traffic(0, it.line, it.slug, it.title, it.message)
                    trafficDao.addTraffic(traffic)
                }
                trafficDao = database.getTrafficDao()
                val traf = trafficDao?.getTraffic()
                progress_bar_traffic.visibility = View.GONE
                val type = "Metro"
                recyclerviewMetro.adapter =
                    TrafficAdapter(traf, type)
            }
        }
    }

    @RequiresApi(Build.VERSION_CODES.M)
    override fun onNavigationItemSelected(item: MenuItem): Boolean {

        val recyclerviewMetro = findViewById<RecyclerView>(R.id.activities_recyclerview_traffic)
        recyclerviewMetro.layoutManager =
            LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)

        val database = Room.databaseBuilder(this, AppDatabase::class.java, "alltraffic")
            .build()
        trafficDao = database.getTrafficDao()

        if (isNetworkConnected()) {
            when (item.itemId) {
                R.id.navbar_metro -> {
                    runBlocking {
                        val deff = async {
                            trafficDao.deleteAllTraffic()
                            val service = retrofit().create(MetroLinesBySearch::class.java)
                            val resultat = service.getTrafficMetro("metros")
                            resultat.result.metros.map {
                                val traffic = Traffic(0, it.line, it.slug, it.title, it.message)
                                trafficDao.addTraffic(traffic)
                            }
                            trafficDao = database.getTrafficDao()
                            val traf = trafficDao.getTraffic()
                            progress_bar_traffic.visibility = View.GONE
                            val type = "Metro"
                            recyclerviewMetro.adapter =
                                TrafficAdapter(traf, type)
                        }
                        deff.await()
                    }
                    return true
                }
                R.id.navbar_train -> {
                    runBlocking {
                        val deffbis = async {
                            trafficDao.deleteAllTraffic()
                            val serviceBis = retrofit().create(TrainLinesBySearch::class.java)
                            val resultatBis = serviceBis.getTrafficTrain("rers")
                            resultatBis.result.rers.map {
                                val traffic = Traffic(0, it.line, it.slug, it.title, it.message)
                                trafficDao.addTraffic(traffic)
                            }
                            trafficDao = database.getTrafficDao()
                            val trafBis = trafficDao.getTraffic()
                            progress_bar_traffic.visibility = View.GONE
                            val type = "RER"
                            recyclerviewMetro.adapter =
                                TrafficAdapter ( trafBis, type)
                        }
                        deffbis.await()
                    }
                    return true
                }
                R.id.navbar_tram -> {
                    runBlocking {
                        val deffter = async {
                            trafficDao.deleteAllTraffic()
                            val serviceTer = retrofit().create(TramLinesBySearch::class.java)
                            val resultatTer = serviceTer.getTrafficTram("tramways")
                            resultatTer.result.tramways.map {
                                val traffic = Traffic(0, it.line, it.slug, it.title, it.message)
                                trafficDao.addTraffic(traffic)
                            }
                            trafficDao = database.getTrafficDao()
                            val trafTer = trafficDao.getTraffic()
                            progress_bar_traffic.visibility = View.GONE
                            val type = "Tramway"
                            recyclerviewMetro.adapter =
                                TrafficAdapter ( trafTer, type)
                        }
                        deffter.await()
                    }
                    return true
                }
            }
        }
        return false
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