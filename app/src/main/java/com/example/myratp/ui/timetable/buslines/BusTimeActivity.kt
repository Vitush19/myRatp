package com.example.myratp.ui.timetable.buslines

import android.content.Context
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
import com.example.myratp.R
import com.example.myratp.adapters.BusLinesAdapter
import com.example.myratp.data.AppDatabase
import com.example.myratp.data.BusLineDao
import com.example.myratp.model.BusLine
import kotlinx.android.synthetic.main.activity_bus_time.*
import kotlinx.coroutines.runBlocking

class BusTimeActivity : AppCompatActivity() {

    private var busLineDao : BusLineDao? = null

    @RequiresApi(Build.VERSION_CODES.M)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_bus_time)

        var recyclerview_bus = findViewById(R.id.activities_recyclerview_bus) as RecyclerView
        recyclerview_bus.layoutManager = LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)

        val database = Room.databaseBuilder(this, AppDatabase::class.java, "allbuslines")
            .build()
        busLineDao = database.getBusLineDao()

        if(isNetworkConnected()){
            runBlocking {
                busLineDao?.deleteAllBusLines()
                val service = retrofit_bus().create(BusLinesBySearch::class.java)
                val resultat = service.getlistBusLine()
                resultat.result.buses.map {
                    val bus = BusLine(0, it.code, it.name, it.directions, it.id)
                    Log.d("CCC", "$bus")
                    busLineDao?.addBusLines(bus)
                }
                busLineDao = database.getBusLineDao()
                val bs = busLineDao?.getBusLines()
                progress_bar.visibility = View.GONE
                recyclerview_bus.adapter =
                    BusLinesAdapter(bs ?: emptyList())
            }
        }
        else{
            Toast.makeText(this, "Vérifiez votre connexion internet et réessayez à nouveau", Toast.LENGTH_SHORT).show()
        }

    }

    @RequiresApi(Build.VERSION_CODES.M)
    private fun isNetworkConnected(): Boolean {
        var result = false
        val connectivityManager = applicationContext.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager?
        connectivityManager?.let{
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