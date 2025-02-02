package com.example.myratp.ui.timetable.tramlines

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.MenuItem
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.room.Room
import com.example.myratp.R
import com.example.myratp.adapters.tramway.TramScheduleAdapter
import com.example.myratp.data.AppDatabase
import com.example.myratp.data.ScheduleDao
import com.example.myratp.model.Schedule
import com.example.myratp.utils.imageMetro
import com.example.myratp.utils.retrofit
import com.github.doyaaaaaken.kotlincsv.dsl.csvReader
import com.github.twocoffeesoneteam.glidetovectoryou.GlideToVectorYou
import kotlinx.android.synthetic.main.activity_tram_schedule.*
import kotlinx.coroutines.async
import kotlinx.coroutines.runBlocking

class TramSchedulesActivity : AppCompatActivity() {

    private var code: String? = ""
    private var name: String? = ""
    private val activity: Activity = this@TramSchedulesActivity
    private lateinit var scheduleDao: ScheduleDao

    @RequiresApi(Build.VERSION_CODES.M)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_tram_schedule)

        code = intent.getStringExtra("code")
        name = intent.getStringExtra("name")

        val toolbar: Toolbar = findViewById(R.id.toolbar_tram_schedule)
        toolbar.setNavigationIcon(R.drawable.ic_arrow_back_white_24dp)
        toolbar.setTitleTextColor(Color.parseColor("#F8F7F2"))
        toolbar.title = "Station : $name"
        setSupportActionBar(toolbar)

        val txtAller = findViewById<TextView>(R.id.tram_schedule_txt_aller)
        val txtRetour = findViewById<TextView>(R.id.tram_schedule_txt_retour)
        val txtStation = findViewById<TextView>(R.id.tram_schedule_txt_station)
        val imgTram = findViewById<ImageView>(R.id.image_tram_schedule)

        val csv = resources.openRawResource(R.raw.pictogrammes)
        val list: List<Map<String, String>> = csvReader().readAllWithHeader(csv)
        run loop@{
            list.map { itMap ->
                itMap.map { itIn ->
                    if(itIn.value == "T${code}"){
                        val url = itMap["noms_des_fichiers"].toString()
                        if(url != "null" && url.isNotEmpty() ){
                            val uri = Uri.parse(url)
                            GlideToVectorYou.justLoadImage(activity, uri, imgTram)
                        }
                        return@loop
                    }
                }
            }
        }
        if("T${code}" == "T4"){
            Log.d("tyui","T4")
            imgTram.setBackgroundResource(imageMetro("T4"))
        }
        if("T${code}" == "T11"){
            Log.d("tyui","T11")
            imgTram.setBackgroundResource(imageMetro("T11"))
        }

        val stationName = "$name"
        txtStation.text = stationName

        val recyclerviewTramScheduleAller =
            findViewById<RecyclerView>(R.id.activities_recyclerview_tram_schedule_aller)
        recyclerviewTramScheduleAller.layoutManager =
            LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)

        val recyclerviewTramScheduleRetour =
            findViewById<RecyclerView>(R.id.activities_recyclerview_tram_schedule_retour)
        recyclerviewTramScheduleRetour.layoutManager =
            LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)

        val database = Room.databaseBuilder(this, AppDatabase::class.java, "allschedule")
            .build()

        scheduleDao = database.getScheduleDao()
        if (isNetworkConnected()) {
            runBlocking {
                val deffered = async {
                    scheduleDao.deleteAllSchedule()
                    val service = retrofit().create(TramLinesBySearch::class.java)
                    val resultat = service.getScheduleTram("tramways", "$code", "$name", "A")
                    resultat.result.schedules.map {
                        val tramSchedule = Schedule(0, it.message, it.destination)
                        scheduleDao.addSchedule(tramSchedule)
                        txtAller.text = it.destination
                    }
                    scheduleDao = database.getScheduleDao()
                    val scheduleAller = scheduleDao.getSchedule()
                    progress_bar_tram_schedule.visibility = View.GONE
                    recyclerviewTramScheduleAller.adapter =
                        TramScheduleAdapter(
                            scheduleAller
                        )

                    scheduleDao.deleteAllSchedule()
                    val resultatBis = service.getScheduleTram("tramways", "$code", "$name", "R")
                    resultatBis.result.schedules.map {
                        val tramSchedule = Schedule(0, it.message, it.destination)
                        scheduleDao.addSchedule(tramSchedule)
                        txtRetour.text = it.destination
                    }
                    scheduleDao = database.getScheduleDao()
                    val scheduleRetour = scheduleDao.getSchedule()
                    progress_bar_tram_schedule.visibility = View.GONE
                    recyclerviewTramScheduleRetour.adapter =
                        TramScheduleAdapter(
                            scheduleRetour
                        )
                }
                deffered.await()
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

        pull_layout_tram_schedule.setOnRefreshListener {
            val intent = intent
            intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION)
            finish()
            overridePendingTransition(0, 0)
            startActivity(getIntent())
            overridePendingTransition(0, 0)
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