package com.example.myratp.ui

import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.os.Build
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.room.Room
import com.example.myratp.R
import com.example.myratp.adapters.MetroLineAdapter
import com.example.myratp.adapters.TrafficMetroAdapter
import com.example.myratp.data.AppDatabase
import com.example.myratp.data.TrafficDao
import com.example.myratp.model.Traffic
import com.example.myratp.ui.timetable.metrolines.MetroLinesBySearch
import com.example.myratp.ui.timetable.metrolines.retrofit
import kotlinx.android.synthetic.main.activity_bus_time.*
import kotlinx.coroutines.runBlocking

class trafficActivity : AppCompatActivity() {

    private var trafficDao: TrafficDao? = null

    @RequiresApi(Build.VERSION_CODES.M)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_traffic)

        var recyclerview_metro = findViewById(R.id.activities_recyclerview_traffic) as RecyclerView
        recyclerview_metro.layoutManager =
            LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)


        val database_bis = Room.databaseBuilder(this, AppDatabase::class.java, "alltraffic")
            .build()
        trafficDao = database_bis.getTrafficDao()

        if (isNetworkConnected()) {
            runBlocking {

                trafficDao?.deleteAllTraffic()
                val service_bis = retrofit().create(MetroLinesBySearch::class.java)
                val resultat_bis = service_bis.getTrafficMetro("metros")
                resultat_bis.result.metros.map {
                    val traffic = Traffic(0, it.line, it.slug, it.title, it.message)
                    trafficDao?.addTraffic(traffic)
                }
                trafficDao = database_bis.getTrafficDao()
                val traf = trafficDao?.getTraffic()
                progress_bar.visibility = View.GONE
                recyclerview_metro.adapter =
                    TrafficMetroAdapter ( traf!!)

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