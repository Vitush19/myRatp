package com.example.myratp.ui.timetable.metrolines

import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.os.Build
import android.os.Bundle
import android.view.MenuItem
import android.view.View
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.room.Room
import com.example.myratp.ui.plans.MetroPlansActivity
import com.example.myratp.R
import com.example.myratp.adapters.metro.MetroLineAdapter
import com.example.myratp.data.AppDatabase
import com.example.myratp.data.MetroLineDao
import com.example.myratp.data.TrafficDao
import com.example.myratp.model.MetroLine
import com.example.myratp.model.Traffic
import com.example.myratp.utils.retrofit
import com.google.android.material.floatingactionbutton.FloatingActionButton
import kotlinx.android.synthetic.main.activity_metro_time.*
import kotlinx.coroutines.runBlocking

class MetroTimeActivity : AppCompatActivity() {

    private lateinit var metroLineDao: MetroLineDao
    private lateinit var trafficDao: TrafficDao

    @RequiresApi(Build.VERSION_CODES.M)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_metro_time)

        val toolbar: Toolbar = findViewById(R.id.toolbar_metro_time)
        toolbar.setNavigationIcon(R.drawable.ic_arrow_back_white_24dp)
        toolbar.setTitleTextColor(Color.parseColor("#F8F7F2"))
        toolbar.title = "Métro"
        setSupportActionBar(toolbar)


        val recyclerviewMetro = findViewById<RecyclerView>(R.id.activities_recyclerview_metro)
        recyclerviewMetro.layoutManager =
            LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)

        val database = Room.databaseBuilder(this, AppDatabase::class.java, "allmetrolines")
            .build()
        metroLineDao = database.getMetroLineDao()

        val databaseBis = Room.databaseBuilder(this, AppDatabase::class.java, "alltraffic")
            .build()
        trafficDao = databaseBis.getTrafficDao()

        val bFloat = findViewById<FloatingActionButton>(R.id.floating_button_map_metroLine)
        bFloat.setOnClickListener {
            val intent = Intent(this, MetroPlansActivity::class.java)
            startActivity(intent)
        }

        if (isNetworkConnected()) {
            runBlocking {
                val service = retrofit().create(MetroLinesBySearch::class.java)
                val resultat = service.getTrafficMetro("metros")
                resultat.result.metros.map {
                    val traffic = Traffic(0, it.line, it.slug, it.title, it.message)
                    trafficDao.addTraffic(traffic)
                }
                trafficDao = databaseBis.getTrafficDao()
                val traffic = trafficDao.getTraffic()

                if (metroLineDao.getMetroLines().isEmpty()) {
                    val serviceBis = retrofit().create(MetroLinesBySearch::class.java)
                    val resultatBis = serviceBis.getlistMetroLine()
                    resultatBis.result.metros.map {
                        val metro = MetroLine(0, it.code, it.name, it.directions, it.id)
                        metroLineDao.addMetroLines(metro)
                    }
                }
                metroLineDao = database.getMetroLineDao()
                val ms = metroLineDao.getMetroLines()
                progress_bar_metro_time.visibility = View.GONE
                recyclerviewMetro.adapter =
                    MetroLineAdapter(
                        ms, traffic
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
