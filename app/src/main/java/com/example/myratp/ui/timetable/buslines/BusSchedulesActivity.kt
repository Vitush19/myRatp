package com.example.myratp.ui.timetable.buslines

import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.os.Build
import android.os.Bundle
import android.view.MenuItem
import android.view.View
import android.widget.TextView
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.room.Room
import com.example.myratp.R
import com.example.myratp.adapters.bus.BusScheduleAdapter
import com.example.myratp.data.AppDatabase
import com.example.myratp.data.ScheduleDao
import com.example.myratp.model.Schedule
import com.example.myratp.utils.retrofit
import kotlinx.android.synthetic.main.activity_bus_schedule.*
import kotlinx.coroutines.async
import kotlinx.coroutines.runBlocking

class BusSchedulesActivity : AppCompatActivity() {

    private var code: String? = ""
    private var name: String? = ""
    private lateinit var scheduleDao: ScheduleDao

    @RequiresApi(Build.VERSION_CODES.M)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_bus_schedule)

        code = intent.getStringExtra("code")
        name = intent.getStringExtra("name")

        val toolbar: Toolbar = findViewById(R.id.toolbar_bus_schedule)
        toolbar.setNavigationIcon(R.drawable.ic_arrow_back_white_24dp)
        toolbar.setTitleTextColor(Color.parseColor("#F8F7F2"))
        toolbar.title = "Station : $name"
        setSupportActionBar(toolbar)

        val txtAller = findViewById<TextView>(R.id.bus_schedule_txt_aller)
        val txtRetour = findViewById<TextView>(R.id.bus_schedule_txt_retour)
        val txtStation = findViewById<TextView>(R.id.bus_schedule_txt_station)

        val stationName = "$name"
        txtStation.text = stationName

        val recyclerviewBusScheduleAller =
            findViewById<RecyclerView>(R.id.activities_recyclerview_bus_schedule_aller)
        recyclerviewBusScheduleAller.layoutManager =
            LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)

        val recyclerviewBusScheduleRetour =
            findViewById<RecyclerView>(R.id.activities_recyclerview_bus_schedule_retour)
        recyclerviewBusScheduleRetour.layoutManager =
            LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)

        val database = Room.databaseBuilder(this, AppDatabase::class.java, "allschedule")
            .build()

        scheduleDao = database.getScheduleDao()
        if (isNetworkConnected()) {
            runBlocking {
                val deffered = async {
                    scheduleDao.deleteAllSchedule()
                    val service = retrofit().create(BusLinesBySearch::class.java)
                    val resultat = service.getScheduleBus("buses", "$code", "$name", "A")
                    resultat.result.schedules.map {
                        val busSchedule = Schedule(0, it.message, it.destination)
                        scheduleDao.addSchedule(busSchedule)
                        txtAller.text = it.destination
                    }
                    scheduleDao = database.getScheduleDao()
                    val scheduleAller = scheduleDao.getSchedule()
                    progress_bar_bus_schedule.visibility = View.GONE
                    recyclerviewBusScheduleAller.adapter =
                        BusScheduleAdapter(
                            scheduleAller
                        )

                    scheduleDao.deleteAllSchedule()
                    val resultatBis = service.getScheduleBus("buses", "$code", "$name", "R")
                    resultatBis.result.schedules.map {
                        val busSchedule = Schedule(0, it.message, it.destination)
                        scheduleDao.addSchedule(busSchedule)
                        txtRetour.text = it.destination
                    }
                    scheduleDao = database.getScheduleDao()
                    val scheduleRetour = scheduleDao.getSchedule()
                    progress_bar_bus_schedule.visibility = View.GONE
                    recyclerviewBusScheduleRetour.adapter =
                        BusScheduleAdapter(
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

        pull_layout_bus_schedule.setOnRefreshListener {
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