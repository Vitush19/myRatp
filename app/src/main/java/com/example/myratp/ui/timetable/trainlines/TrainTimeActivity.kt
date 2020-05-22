package com.example.myratp.ui.timetable.trainlines

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.room.Room
import com.example.myratp.R
import com.example.myratp.adapters.TrainLinesAdapter
import com.example.myratp.data.AppDatabase
import com.example.myratp.data.TrainLineDao
import com.example.myratp.model.TrainLine
import kotlinx.coroutines.runBlocking

class TrainTimeActivity : AppCompatActivity() {

    private var trainLineDao: TrainLineDao? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_train_time)

        var recyclerview_train = findViewById(R.id.activities_recyclerview_train) as RecyclerView
        recyclerview_train.layoutManager =
            LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)

        val database = Room.databaseBuilder(this, AppDatabase::class.java, "alltrainlines")
            .build()
        trainLineDao = database.getTrainLineDao()

        runBlocking {
            trainLineDao?.deleteAllTrainLines()
            val service = retrofit_train().create(TrainLinesBySearch::class.java)
            val resultat = service.getlistTrainLine()
            resultat.result.rers.map {
                val train = TrainLine(0, it.code, it.name, it.directions, it.id)
                Log.d("CCC", "$train")
                trainLineDao?.addTrainLines(train)
            }
            trainLineDao = database.getTrainLineDao()
            val ts = trainLineDao?.getTrainLines()
            val test = ts.isNullOrEmpty()
            Log.d("CCC", "$test")
            recyclerview_train.adapter =
                TrainLinesAdapter(ts ?: emptyList())
        }
    }
}