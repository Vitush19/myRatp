package com.example.myratp.ui.timetable.metrolines

import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.MenuItem
import android.view.View
import android.widget.*
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.room.Room
import com.example.myratp.R
import com.example.myratp.adapters.MetroScheduleAdapter
import com.example.myratp.data.AppDatabase
import com.example.myratp.data.ScheduleDao
import com.example.myratp.model.Schedule
import kotlinx.android.synthetic.main.activity_metro_schedule.*
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
        if(correspondance != null){
            parts = correspondance!!.split(delimiter)
        }
        Log.d("tyui", "affichage des correspondance : $parts")

        val toolbar: Toolbar = findViewById(R.id.toolbar_metro_schedule)
        toolbar.setNavigationIcon(R.drawable.ic_arrow_back_black_24dp)
        toolbar.title = "Station : $name"
        setSupportActionBar(toolbar)

        val txtAller = findViewById<TextView>(R.id.metro_schedule_txt_aller)
        val txtRetour = findViewById<TextView>(R.id.metro_schedule_txt_retour)
        val txtStation = findViewById<TextView>(R.id.metro_schedule_txt_station)
        val imageMetro = findViewById<ImageView>(R.id.image_metro_schedule)

        imageMetro.setBackgroundResource(ImageMetro("$code"))
        txtStation.text = "$name"
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
//                val deffered = async {
                    scheduleDao?.deleteAllSchedule()
                    val service = retrofit().create(MetroLinesBySearch::class.java)
                    val resultat = service.getScheduleMetro("metros", "$code", "$name", "A")
                    resultat.result.schedules.map {
                        val metroSchedule = Schedule(0, it.message, it.destination)
                        scheduleDao?.addSchedule(metroSchedule)
                        txtAller.text = it.destination
                    }
                    scheduleDao = database.getScheduleDao()
                    val schedule = scheduleDao?.getSchedule()
                    progress_bar_metro_schedule.visibility = View.GONE
                    recyclerviewMetroScheduleAller.adapter =
                        MetroScheduleAdapter(schedule ?: emptyList())


                    scheduleDao?.deleteAllSchedule()
                    val resultatBis = service.getScheduleMetro("metros", "$code", "$name", "R")
                    resultatBis.result.schedules.map {
                        val metroSchedule = Schedule(0, it.message, it.destination)
                        scheduleDao?.addSchedule(metroSchedule)
                        txtRetour.text = it.destination
                    }
                    scheduleDao = database.getScheduleDao()
                    val sta = scheduleDao?.getSchedule()
                    progress_bar_metro_schedule.visibility = View.GONE
                    recyclerviewMetroScheduleRetour.adapter =
                        MetroScheduleAdapter(sta ?: emptyList())
//                }

                for (x in parts.indices){
                    if(parts[x].isNotEmpty()){
                        val num = parts[x]
                        schedule(num)
                    }
                }

            }
        } else {
            Toast.makeText(
                this,
                "Vérifiez votre connexion internet et réessayez à nouveau",
                Toast.LENGTH_SHORT
            ).show()
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

    private fun schedule(num : String){
        val context = applicationContext
        val database = Room.databaseBuilder(this, AppDatabase::class.java, "allschedule")
            .build()
        scheduleDao = database.getScheduleDao()
        val myLayout = LinearLayout(this)
        val directions = listOf("A", "R")
        linear_adding_schedules.addView(myLayout)
        myLayout.layoutParams.width=LinearLayout.LayoutParams.MATCH_PARENT
        myLayout.layoutParams.height= LinearLayout.LayoutParams.WRAP_CONTENT
        myLayout.setBackgroundColor(Color.parseColor("#FFFFFF"))
        myLayout.setPadding(10,10,10,10)
        myLayout.orientation=LinearLayout.HORIZONTAL
        for (x in directions) {
            val myLinearBis = LinearLayout(this)
            val recyclerview = RecyclerView(this)
            recyclerview.layoutManager = LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)
            myLayout.addView(myLinearBis)
            val midwidth = (myLayout.width) / 2
            myLinearBis.layoutParams.width = LinearLayout.LayoutParams.WRAP_CONTENT
            myLinearBis.layoutParams.height = LinearLayout.LayoutParams.WRAP_CONTENT
            myLinearBis.orientation = LinearLayout.VERTICAL
            myLinearBis.setBackgroundColor(Color.parseColor("#000000"))
            myLinearBis.setPadding(10,10,10,10)
            myLinearBis.addView(recyclerview)
            runBlocking {
                scheduleDao?.deleteAllSchedule()
                val service = retrofit().create(MetroLinesBySearch::class.java)
                val resultat = service.getScheduleMetro("metros", "$num", "$name", "$x")
                resultat.result.schedules.map {
                    val metroSchedule = Schedule(0, it.message, it.destination)
                    scheduleDao?.addSchedule(metroSchedule)
                }
                scheduleDao = database.getScheduleDao()
                val schedule = scheduleDao?.getSchedule()
                recyclerview.adapter =
                    MetroScheduleAdapter(schedule ?: emptyList())
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