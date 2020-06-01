package com.example.myratp.ui.timetable.metrolines

import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.graphics.Typeface
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.os.Build
import android.os.Bundle
import android.view.Gravity
import android.view.MenuItem
import android.view.View
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.room.Room
import com.example.myratp.utils.imageMetro
import com.example.myratp.R
import com.example.myratp.adapters.metro.MetroScheduleAdapter
import com.example.myratp.data.AppDatabase
import com.example.myratp.data.ScheduleDao
import com.example.myratp.model.Schedule
import com.example.myratp.utils.retrofit
import kotlinx.android.synthetic.main.activity_metro_schedule.*
import kotlinx.coroutines.async
import kotlinx.coroutines.runBlocking


class MetroSchedulesActivity : AppCompatActivity() {

    private var code: String? = ""
    private var name: String? = ""
    private var parts: List<String> = emptyList()
    private var correspondance: String? = ""
    private var scheduleDao: ScheduleDao? = null

    @RequiresApi(Build.VERSION_CODES.M)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_metro_schedule)

        code = intent.getStringExtra("code")
        name = intent.getStringExtra("name")
        correspondance = intent.getStringExtra("correspondance") ?: ""

        val delimiter = "-"
        if (correspondance != null) {
            parts = correspondance!!.split(delimiter)
        }

        val toolbar: Toolbar = findViewById(R.id.toolbar_metro_schedule)
        toolbar.setNavigationIcon(R.drawable.ic_arrow_back_white_24dp)
        toolbar.setTitleTextColor(Color.parseColor("#F8F7F2"))
        toolbar.title = "Station : $name"
        setSupportActionBar(toolbar)

        val txtAller = findViewById<TextView>(R.id.metro_schedule_txt_aller)
        val txtRetour = findViewById<TextView>(R.id.metro_schedule_txt_retour)
        val txtStation = findViewById<TextView>(R.id.metro_schedule_txt_station)
        val imageMetro = findViewById<ImageView>(R.id.image_metro_schedule)

        imageMetro.setBackgroundResource(imageMetro("$code"))
        val stationName = "$name"
        txtStation.text = stationName

        val recyclerviewMetroScheduleAller =
            findViewById<RecyclerView>(R.id.activities_recyclerview_metro_schedule)
        recyclerviewMetroScheduleAller.layoutManager =
            LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)
        val recyclerviewMetroScheduleRetour =
            findViewById<RecyclerView>(R.id.activities_recyclerview_metro_schedule_retour)
        recyclerviewMetroScheduleRetour.layoutManager =
            LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)

        val database = Room.databaseBuilder(this, AppDatabase::class.java, "allschedule")
            .build()
        scheduleDao = database.getScheduleDao()

        if (isNetworkConnected()) {
            runBlocking {
                val deffered = async {
                    scheduleDao?.deleteAllSchedule()
                    val service = retrofit().create(MetroLinesBySearch::class.java)
                    val resultat = service.getScheduleMetro("metros", "$code", "$name", "A")
                    resultat.result.schedules.map {
                        val metroSchedule = Schedule(0, it.message, it.destination)
                        scheduleDao?.addSchedule(metroSchedule)
                        txtAller.text = it.destination
                    }
                    scheduleDao = database.getScheduleDao()
                    val scheduleAller = scheduleDao?.getSchedule()
                    progress_bar_metro_schedule.visibility = View.GONE
                    recyclerviewMetroScheduleAller.adapter =
                        MetroScheduleAdapter(
                            scheduleAller ?: emptyList()
                        )

                    scheduleDao?.deleteAllSchedule()
                    val resultatBis = service.getScheduleMetro("metros", "$code", "$name", "R")
                    resultatBis.result.schedules.map {
                        val metroSchedule = Schedule(0, it.message, it.destination)
                        scheduleDao?.addSchedule(metroSchedule)
                        txtRetour.text = it.destination
                    }
                    scheduleDao = database.getScheduleDao()
                    val scheduleRetour = scheduleDao?.getSchedule()
                    progress_bar_metro_schedule.visibility = View.GONE
                    recyclerviewMetroScheduleRetour.adapter =
                        MetroScheduleAdapter(
                            scheduleRetour ?: emptyList()
                        )
                }
                deffered.await()
                for (x in parts.indices) {
                    if (parts[x].isNotEmpty()) {
                        val num = parts[x]
                        schedule(num)
                    }
                }
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

        pull_layout_schedule_metro.setOnRefreshListener {
            val intent = intent
            intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION)
            finish()
            overridePendingTransition(0, 0)
            startActivity(getIntent())
            overridePendingTransition(0, 0)
        }
    }

    private fun schedule(num: String) {
        val database = Room.databaseBuilder(this, AppDatabase::class.java, "allschedule")
            .build()
        scheduleDao = database.getScheduleDao()
        val myLayout = LinearLayout(this)
        val directions = listOf("A", "R")
        val img = ImageView(this)
        val params = LinearLayout.LayoutParams(100, 100).apply {
            gravity = Gravity.CENTER
        }
        params.setMargins(0, 20, 0, 10)
        img.setBackgroundResource(
            imageMetro(num)
        )
        img.layoutParams = params
        linear_adding_schedules.addView(img)
        linear_adding_schedules.addView(myLayout)
        val params1 = LinearLayout.LayoutParams(
            LinearLayout.LayoutParams.MATCH_PARENT,
            LinearLayout.LayoutParams.WRAP_CONTENT
        )
        params1.setMargins(10, 10, 10, 10)
        myLayout.layoutParams = params1
        myLayout.setBackgroundColor(Color.parseColor("#0a3d62"))
        myLayout.orientation = LinearLayout.HORIZONTAL
        for (x in directions) {
            val myLinearBis = LinearLayout(this)
            val recyclerview = RecyclerView(this)
            recyclerview.layoutManager =
                LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)
            myLayout.addView(myLinearBis)
            val params2 =
                LinearLayout.LayoutParams(0, LinearLayout.LayoutParams.MATCH_PARENT).apply {
                    weight = 1.0f
                    gravity = Gravity.CENTER
                }
            params2.setMargins(10, 10, 10, 10)
            myLinearBis.layoutParams = params2
            myLinearBis.orientation = LinearLayout.VERTICAL
            myLinearBis.setBackgroundColor(Color.parseColor("#92918E"))
            val txt = TextView(this)
            val params3 = LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, 150)
            txt.layoutParams = params3
            txt.setBackgroundColor(Color.parseColor("#A6A5A2"))
            txt.textSize = 17.toFloat()
            txt.setTypeface(txt.typeface, Typeface.BOLD)
            txt.setTextColor(Color.parseColor("#343749"))
            txt.gravity = Gravity.CENTER
            myLinearBis.addView(txt)
            myLinearBis.addView(recyclerview)
            runBlocking {
                val deffered = async {
                    scheduleDao?.deleteAllSchedule()
                    var destination = ""
                    val service = retrofit().create(MetroLinesBySearch::class.java)
                    val resultat = service.getScheduleMetro("metros", num, "$name", x)
                    resultat.result.schedules.map {
                        val metroSchedule = Schedule(0, it.message, it.destination)
                        scheduleDao?.addSchedule(metroSchedule)
                        destination = it.destination
                    }
                    txt.text = destination
                    scheduleDao = database.getScheduleDao()
                    val schedule = scheduleDao?.getSchedule()
                    recyclerview.adapter =
                        MetroScheduleAdapter(
                            schedule ?: emptyList()
                        )
                }
                deffered.await()
            }
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