package com.example.myratp.ui.timetable.trainlines

import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.MenuItem
import android.view.View
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.appcompat.widget.Toolbar
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.room.Room
import com.example.myratp.R
import com.example.myratp.ui.plans.TrainPlansActivity
import com.example.myratp.adapters.train.TrainLinesAdapter
import com.example.myratp.data.AppDatabase
import com.example.myratp.data.TrainLineDao
import com.example.myratp.model.TrainLine
import com.example.myratp.utils.retrofit
import com.google.android.material.floatingactionbutton.FloatingActionButton
import kotlinx.android.synthetic.main.activity_train_time.*
import kotlinx.coroutines.runBlocking

class TrainTimeActivity : AppCompatActivity() {

    private lateinit var trainLineDao: TrainLineDao

    @RequiresApi(Build.VERSION_CODES.M)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_train_time)

        val toolbar: Toolbar = findViewById(R.id.toolbar_train_time)
        toolbar.setNavigationIcon(R.drawable.ic_arrow_back_white_24dp)
        toolbar.setTitleTextColor(Color.parseColor("#F8F7F2"))
        toolbar.title = "RER"
        setSupportActionBar(toolbar)

        val recyclerviewTrain = findViewById<RecyclerView>(R.id.activities_recyclerview_train)
        recyclerviewTrain.layoutManager =
            LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)

        val database = Room.databaseBuilder(this, AppDatabase::class.java, "alltrainlines")
            .build()
        trainLineDao = database.getTrainLineDao()

        val bFloat = findViewById<FloatingActionButton>(R.id.floating_button_map_trainLine)
        bFloat.setOnClickListener {
            val intent = Intent(this, TrainPlansActivity::class.java)
            startActivity(intent)
        }

        if (isNetworkConnected()) {
            runBlocking {
                if (trainLineDao.getTrainLines().isEmpty()) {
                    val service = retrofit().create(TrainLinesBySearch::class.java)
                    val resultat = service.getlistTrainLine()
                    resultat.result.rers.map {
                        val train = TrainLine(0, it.code, it.name, it.directions, it.id)
                        trainLineDao.addTrainLines(train)
                    }
                }
                trainLineDao = database.getTrainLineDao()
                val trainStation = trainLineDao.getTrainLines()
                progress_bar_train.visibility = View.GONE
                recyclerviewTrain.adapter =
                    TrainLinesAdapter(
                        trainStation, this@TrainTimeActivity
                    )
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