package com.example.myratp.ui.timetable.metrolines

import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.room.Room
import com.example.myratp.R
import com.example.myratp.adapters.MetroScheduleAdapter
import com.example.myratp.data.AppDatabase
import com.example.myratp.data.ScheduleDao
import com.example.myratp.model.Schedule
import kotlinx.coroutines.runBlocking

class MetroSchedulesActivity : AppCompatActivity(){

    private var code : String? = ""
    private var name : String? = ""
    private var scheduleDao : ScheduleDao? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_metro_schedule)

        code = intent.getStringExtra("code")
        name = intent.getStringExtra("name")
        Log.d("AAA", "$code")
        Log.d("AAA", "$name")

        var recyclerview_metro_schedule = findViewById(R.id.activities_recyclerview_metro_schedule) as RecyclerView
        recyclerview_metro_schedule.layoutManager = LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)

        val database = Room.databaseBuilder(this, AppDatabase::class.java, "allschedule")
            .build()
        scheduleDao = database.getScheduleDao()

        runBlocking {
            scheduleDao?.deleteAllSchedule()
            val service = retrofit().create(MetroLinesBySearch::class.java)
            val resultat = service.getScheduleMetro("metros", "$code", "$name", "A+R")
            resultat.result.schedules.map {
                val metroSchedule = Schedule(0, it.message, it.destination)
                Log.d("CCC", "$metroSchedule")
                scheduleDao?.addSchedule(metroSchedule)
            }
            scheduleDao = database.getScheduleDao()
            val s = scheduleDao?.getSchedule()
            //val test = s.isNullOrEmpty()
            recyclerview_metro_schedule.adapter =
                MetroScheduleAdapter(s ?: emptyList())
        }
    }
}