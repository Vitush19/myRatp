package com.example.myratp.ui.timetable.buslines

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
import com.example.myratp.ui.plans.BusPlansActivity
import com.example.myratp.R
import com.example.myratp.adapters.bus.BusLinesAdapter
import com.example.myratp.data.AppDatabase
import com.example.myratp.data.BusLineDao
import com.example.myratp.model.BusLine
import com.example.myratp.utils.retrofit
import com.google.android.material.floatingactionbutton.FloatingActionButton
import kotlinx.android.synthetic.main.activity_bus_time.*
import kotlinx.coroutines.runBlocking

class BusTimeActivity : AppCompatActivity() {

    private lateinit var busLineDao: BusLineDao

    @RequiresApi(Build.VERSION_CODES.M)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_bus_time)

        val toolbar: Toolbar = findViewById(R.id.toolbar_bus_time)
        toolbar.setTitleTextColor(Color.parseColor("#F8F7F2"))
        toolbar.setNavigationIcon(R.drawable.ic_arrow_back_white_24dp)
        toolbar.title = "Bus"
        setSupportActionBar(toolbar)

        val recyclerviewBus = findViewById<RecyclerView>(R.id.activities_recyclerview_bus)
        recyclerviewBus.layoutManager =
            LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)

        val database = Room.databaseBuilder(this, AppDatabase::class.java, "allbuslines")
            .build()
        busLineDao = database.getBusLineDao()

        val bFloat = findViewById<FloatingActionButton>(R.id.floating_button_map_busLine)
        bFloat.setOnClickListener {
            val intent = Intent(this, BusPlansActivity::class.java)
            startActivity(intent)
        }

        if (isNetworkConnected()) {
            runBlocking {
                if (busLineDao.getBusLines().isEmpty()) {
                    val service = retrofit().create(BusLinesBySearch::class.java)
                    val resultat = service.getlistBusLine()
                    resultat.result.buses.map {
                        val bus = BusLine(0, it.code, it.name, it.directions, it.id)
                        busLineDao.addBusLines(bus)
                    }
                }
                busLineDao = database.getBusLineDao()
                val bs = busLineDao.getBusLines()
                progress_bar_bus_ligne.visibility = View.GONE
                recyclerviewBus.adapter =
                    BusLinesAdapter(
                        bs, this@BusTimeActivity
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