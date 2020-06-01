package com.example.myratp.ui.timetable.noctilien

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
import com.example.myratp.adapters.noctilien.NoctilienScheduleAdapter
import com.example.myratp.data.AppDatabase
import com.example.myratp.data.ScheduleDao
import com.example.myratp.model.Schedule
import com.example.myratp.utils.retrofit
import kotlinx.android.synthetic.main.activity_nocti_schedule.*
import kotlinx.coroutines.async
import kotlinx.coroutines.runBlocking

class NoctilienSchedulesActivity : AppCompatActivity() {

    private var code: String? = ""
    private var name: String? = ""
    private var scheduleDao: ScheduleDao? = null

    @RequiresApi(Build.VERSION_CODES.M)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_nocti_schedule)

        code = intent.getStringExtra("code")
        name = intent.getStringExtra("name")

        val toolbar: Toolbar = findViewById(R.id.toolbar_nocti_schedule)
        toolbar.setNavigationIcon(R.drawable.ic_arrow_back_white_24dp)
        toolbar.setTitleTextColor(Color.parseColor("#F8F7F2"))
        toolbar.title = "Station : $name"
        setSupportActionBar(toolbar)

        val txtAller = findViewById<TextView>(R.id.nocti_schedule_txt_aller)
        val txtRetour = findViewById<TextView>(R.id.nocti_schedule_txt_retour)
        val txtStation = findViewById<TextView>(R.id.nocti_schedule_txt_station)

        val stationName = "$name"
        txtStation.text = stationName

        val recyclerviewNoctiScheduleAller =
            findViewById<RecyclerView>(R.id.activities_recyclerview_nocti_schedule_aller)
        recyclerviewNoctiScheduleAller.layoutManager =
            LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)

        val recyclerviewNoctiScheduleRetour =
            findViewById<RecyclerView>(R.id.activities_recyclerview_nocti_schedule_retour)
        recyclerviewNoctiScheduleRetour.layoutManager =
            LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)

        val database = Room.databaseBuilder(this, AppDatabase::class.java, "allschedule")
            .build()

        scheduleDao = database.getScheduleDao()
        if (isNetworkConnected()) {
            runBlocking {
                val deffered = async {
                    scheduleDao?.deleteAllSchedule()
                    val service = retrofit().create(NoctiLineBySearch::class.java)
                    val resultat = service.getScheduleNocti("noctiliens", "$code", "$name", "A")
                    resultat.result.schedules.map {
                        val noctiSchedule = Schedule(0, it.message, it.destination)
                        scheduleDao?.addSchedule(noctiSchedule)
                        txtAller.text = it.destination
                    }
                    scheduleDao = database.getScheduleDao()
                    val scheduleAller = scheduleDao?.getSchedule()
                    progress_bar_nocti_schedule.visibility = View.GONE
                    recyclerviewNoctiScheduleAller.adapter =
                        NoctilienScheduleAdapter(
                            scheduleAller ?: emptyList()
                        )

                    scheduleDao?.deleteAllSchedule()
                    val resultatBis = service.getScheduleNocti("noctiliens", "$code", "$name", "R")
                    resultatBis.result.schedules.map {
                        val noctiSchedule = Schedule(0, it.message, it.destination)
                        scheduleDao?.addSchedule(noctiSchedule)
                        txtRetour.text = it.destination
                    }
                    scheduleDao = database.getScheduleDao()
                    val scheduleRetour = scheduleDao?.getSchedule()
                    progress_bar_nocti_schedule.visibility = View.GONE
                    recyclerviewNoctiScheduleRetour.adapter =
                        NoctilienScheduleAdapter(
                            scheduleRetour ?: emptyList()
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

        pull_layout_nocti_schedule.setOnRefreshListener {
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