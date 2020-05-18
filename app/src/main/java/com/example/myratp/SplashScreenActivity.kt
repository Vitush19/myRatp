package com.example.myratp

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.util.Log
import androidx.room.Room
import com.example.myratp.data.AppDatabase
import com.example.myratp.data.MetroLineDao
import com.example.myratp.data.TrafficDao
import com.example.myratp.model.MetroLine
import com.example.myratp.model.Traffic
import com.example.myratp.ui.timetable.metrolines.MetroLinesBySearch
import com.example.myratp.ui.timetable.metrolines.retrofit
import kotlinx.coroutines.runBlocking

class SplashScreenActivity : AppCompatActivity() {

    private lateinit var handler: Handler
    private var metroLineDao: MetroLineDao? = null
    private var trafficDao: TrafficDao? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash_screen)

        val database = Room.databaseBuilder(this, AppDatabase::class.java, "allmetrolines")
            .build()
        metroLineDao = database.getMetroLineDao()

        val database_bis = Room.databaseBuilder(this, AppDatabase::class.java, "alltraffic")
            .build()
        trafficDao = database_bis.getTrafficDao()

        runBlocking {
            if(trafficDao!!.getTraffic().isEmpty()) {
                val service_bis = retrofit().create(MetroLinesBySearch::class.java)
                val resultat_bis = service_bis.getTrafficMetro("metros")
                resultat_bis.result.metros.map {
                    val traffic = Traffic(0, it.line, it.slug, it.title, it.message)
                    trafficDao?.addTraffic(traffic)
                }
            }
            if(metroLineDao!!.getMetroLines().isEmpty()){
                val service = retrofit().create(MetroLinesBySearch::class.java)
                val resultat = service.getlistMetroLine()
                resultat.result.metros.map {
                    val metro = MetroLine(0, it.code, it.name, it.directions, it.id)
                    Log.d("CCC", "$metro")
                    metroLineDao?.addMetroLines(metro)
                }
            }
        }

        handler = Handler()
        handler.postDelayed({
            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
            finish()
        }, 1000)
    }
}
