package com.example.myratp.ui.timetable.metrolines

import android.content.Context
import android.content.Intent
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.room.Room
import com.example.myratp.MetroPlansActivity
import com.example.myratp.adapters.MetroLineAdapter
import com.example.myratp.R
import com.example.myratp.data.AppDatabase
import com.example.myratp.data.MetroLineDao
import com.example.myratp.data.TrafficDao
import com.example.myratp.model.MetroLine
import com.example.myratp.model.Traffic
import com.google.android.material.floatingactionbutton.FloatingActionButton
import kotlinx.android.synthetic.main.activity_bus_time.progress_bar
import kotlinx.coroutines.runBlocking

class MetroTimeActivity : AppCompatActivity() {

    private var metroLineDao: MetroLineDao? = null
    private var trafficDao: TrafficDao? = null

    @RequiresApi(Build.VERSION_CODES.M)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_metro_time)


        var recyclerview_metro = findViewById(R.id.activities_recyclerview_metro) as RecyclerView
        recyclerview_metro.layoutManager =
            LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)

        val database = Room.databaseBuilder(this, AppDatabase::class.java, "allmetrolines")
            .build()
        metroLineDao = database.getMetroLineDao()

        val database_bis = Room.databaseBuilder(this, AppDatabase::class.java, "alltraffic")
            .build()
        trafficDao = database_bis.getTrafficDao()

        val bfloat = findViewById(R.id.floating_button_map_metroline) as FloatingActionButton
        bfloat.setOnClickListener {
            val intent = Intent(this, MetroPlansActivity::class.java)
            startActivity(intent)
        }

        if (isNetworkConnected()) {
            runBlocking {
                //trafficDao?.deleteAllTraffic()
//                if (trafficDao!!.getTraffic().isEmpty()) {
//
//                }
                Log.d("DF", "traff null")
                val service_bis = retrofit().create(MetroLinesBySearch::class.java)
                val resultat_bis = service_bis.getTrafficMetro("metros")
                resultat_bis.result.metros.map {
                    val traffic = Traffic(0, it.line, it.slug, it.title, it.message)
                    trafficDao?.addTraffic(traffic)
                }
                trafficDao = database_bis.getTrafficDao()
                val traf = trafficDao?.getTraffic()

                //metroLineDao?.deleteAllMetroLines()
                if (metroLineDao!!.getMetroLines().isEmpty()) {
                    Log.d("DF", "null")
                    val service = retrofit().create(MetroLinesBySearch::class.java)
                    val resultat = service.getlistMetroLine()
                    resultat.result.metros.map {
                        val metro = MetroLine(0, it.code, it.name, it.directions, it.id)
                        Log.d("CCC", "$metro")
                        metroLineDao?.addMetroLines(metro)
                    }
                }

                metroLineDao = database.getMetroLineDao()
                val ms = metroLineDao?.getMetroLines()
                Log.d("DF", "$ms")
                progress_bar.visibility = View.GONE
                recyclerview_metro.adapter =
                    MetroLineAdapter(ms ?: emptyList(), traf!!)

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
