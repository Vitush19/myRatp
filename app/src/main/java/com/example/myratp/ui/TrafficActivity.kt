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
import com.example.myratp.adapters.TrafficMetroAdapter
import com.example.myratp.data.AppDatabase
import com.example.myratp.data.TrafficDao
import com.example.myratp.model.Traffic
import com.example.myratp.retrofit
import com.example.myratp.ui.timetable.metrolines.MetroLinesBySearch
import kotlinx.android.synthetic.main.activity_traffic.*
import kotlinx.coroutines.runBlocking

class TrafficActivity : AppCompatActivity() {

    private var trafficDao: TrafficDao? = null

    @RequiresApi(Build.VERSION_CODES.M)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_traffic)

        val recyclerviewMetro = findViewById<RecyclerView>(R.id.activities_recyclerview_traffic)
        recyclerviewMetro.layoutManager =
            LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)


        val database = Room.databaseBuilder(this, AppDatabase::class.java, "alltraffic")
            .build()
        trafficDao = database.getTrafficDao()

        if (isNetworkConnected()) {
            runBlocking {

                trafficDao?.deleteAllTraffic()
                val service = retrofit().create(MetroLinesBySearch::class.java)
                val resultat = service.getTrafficMetro("metros")
                resultat.result.metros.map {
                    val traffic = Traffic(0, it.line, it.slug, it.title, it.message)
                    trafficDao?.addTraffic(traffic)
                }
                trafficDao = database.getTrafficDao()
                val traf = trafficDao?.getTraffic()
                progress_bar_traffic.visibility = View.GONE
                recyclerviewMetro.adapter =
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