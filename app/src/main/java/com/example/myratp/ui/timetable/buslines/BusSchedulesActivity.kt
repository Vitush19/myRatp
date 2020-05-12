package com.example.myratp.ui.timetable.metrolines

import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.room.Room
import com.example.myratp.R
import com.example.myratp.adapters.BusScheduleAdapter
import com.example.myratp.data.AppDatabase
import com.example.myratp.data.ScheduleDao
import com.example.myratp.model.Schedule
import com.example.myratp.ui.timetable.buslines.BusLinesBySearch
import com.example.myratp.ui.timetable.buslines.retrofit_bus
import kotlinx.coroutines.runBlocking

class BusSchedulesActivity : AppCompatActivity(){

    private var code : String? = ""
    private var name : String? = ""
    private var scheduleDao : ScheduleDao? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_bus_schedule)

        code = intent.getStringExtra("code")
        name = intent.getStringExtra("name")

        Log.d("AAA", "$code")
        Log.d("AAA", "$name")

        var recyclerview_vus_schedule = findViewById(R.id.activities_recyclerview_bus_schedule) as RecyclerView
        recyclerview_vus_schedule.layoutManager = LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)

        val database = Room.databaseBuilder(this, AppDatabase::class.java, "allschedule")
            .build()
        scheduleDao = database.getScheduleDao()

        runBlocking {
            scheduleDao?.deleteAllSchedule()
            val service = retrofit_bus().create(BusLinesBySearch::class.java)
            val resultat = service.getScheduleMetro("buses", "$code", "$name", "A+R")
            resultat.result.schedules.map {
                val busSchedule = Schedule(0, it.message, it.destination)
                Log.d("CCC", "$busSchedule")
                scheduleDao?.addSchedule(busSchedule)
            }
            scheduleDao = database.getScheduleDao()
            val s = scheduleDao?.getSchedule()
            //val test = s.isNullOrEmpty()
            recyclerview_vus_schedule.adapter =
                BusScheduleAdapter(s ?: emptyList())
        }
    }
}