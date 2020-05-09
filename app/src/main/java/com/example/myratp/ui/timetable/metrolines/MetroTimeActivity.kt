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
import com.example.myratp.model.MetroLine
import com.example.myratp.ui.timetable.buslines.retrofit_bus
import kotlinx.coroutines.runBlocking

class MetroTimeActivity : AppCompatActivity() {

    private var metroLineDao : MetroLineDao? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_metro_time)

        var recyclerview_metro = findViewById(R.id.activities_recyclerview_metro) as RecyclerView
        recyclerview_metro.layoutManager = LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)

        val database = Room.databaseBuilder(this, AppDatabase::class.java, "gestion_metros")
            .build()
        metroLineDao = database.getMetroLineDao()

        runBlocking {
            metroLineDao?.deleteAllMetroLines()
            val service = retrofit_bus().create(MetroLinesBySearch::class.java)
            val resultat = service.getlistMetroLine()
            resultat.result.metros.map {
                val m = MetroLine(0, it.code, it.name, it.directions, it.id)
                Log.d("CCC", "$m")
                metroLineDao?.addMetroLines(m)
            }
            metroLineDao = database.getMetroLineDao()
            val ms = metroLineDao?.getMetroLines()
            //val test = bs.isNullOrEmpty()
            recyclerview_metro.adapter =
                MetroLineAdapter(ms ?: emptyList())
        }
    }
}
