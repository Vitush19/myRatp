package com.example.myratp.ui.timetable.metrolines

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.room.Room
import com.example.myratp.adapters.MetroLineAdapter
import com.example.myratp.R
import com.example.myratp.data.AppDatabase
import com.example.myratp.data.MetroLineDao
import com.example.myratp.data.TrafficDao
import com.example.myratp.model.MetroLine
import com.example.myratp.model.Traffic
import kotlinx.coroutines.runBlocking

class MetroTimeActivity : AppCompatActivity() {

    private var metroLineDao : MetroLineDao? = null
    private var trafficDao : TrafficDao? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_metro_time)

        var recyclerview_metro = findViewById(R.id.activities_recyclerview_metro) as RecyclerView
        recyclerview_metro.layoutManager = LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)

        val database = Room.databaseBuilder(this, AppDatabase::class.java, "allmetrolines")
            .build()
        metroLineDao = database.getMetroLineDao()

        val database_bis = Room.databaseBuilder(this, AppDatabase::class.java, "alltraffic")
            .build()
        trafficDao = database_bis.getTrafficDao()

//        runBlocking {
//
//        }

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
            //val test = bs.isNullOrEmpty()
//            recyclerview_metro.adapter =
//                MetroLineAdapter(traf ?: emptyList())


            metroLineDao?.deleteAllMetroLines()
            val service = retrofit().create(MetroLinesBySearch::class.java)
            val resultat = service.getlistMetroLine()
            resultat.result.metros.map {
                val metro = MetroLine(0, it.code, it.name, it.directions, it.id)
                Log.d("CCC", "$metro")
                metroLineDao?.addMetroLines(metro)
            }
            metroLineDao = database.getMetroLineDao()
            val ms = metroLineDao?.getMetroLines()
            //val test = bs.isNullOrEmpty()
            recyclerview_metro.adapter =
                MetroLineAdapter(ms ?: emptyList(), traf!!)
        }


    }
}
