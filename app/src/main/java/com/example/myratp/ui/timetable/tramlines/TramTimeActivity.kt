package com.example.myratp.ui.timetable.tramlines

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
import com.example.myratp.ui.plans.TramPlansActivity
import com.example.myratp.adapters.tramway.TramLineAdapter
import com.example.myratp.data.AppDatabase
import com.example.myratp.data.TramLineDao
import com.example.myratp.model.TramLine
import com.example.myratp.utils.retrofit
import com.google.android.material.floatingactionbutton.FloatingActionButton
import kotlinx.android.synthetic.main.activity_tram_time.*
import kotlinx.coroutines.runBlocking

class TramTimeActivity : AppCompatActivity() {

    private lateinit var tramLineDao: TramLineDao

    @RequiresApi(Build.VERSION_CODES.M)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_tram_time)

        val toolbar: Toolbar = findViewById(R.id.toolbar_tram_time)
        toolbar.setNavigationIcon(R.drawable.ic_arrow_back_white_24dp)
        toolbar.setTitleTextColor(Color.parseColor("#F8F7F2"))
        toolbar.title = "Tramway"
        setSupportActionBar(toolbar)

        val recyclerviewTram = findViewById<RecyclerView>(R.id.activities_recyclerview_tram)
        recyclerviewTram.layoutManager =
            LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)

        val database = Room.databaseBuilder(this, AppDatabase::class.java, "alltramlines")
            .build()
        tramLineDao = database.getTramLineDao()

        val bFloat = findViewById<FloatingActionButton>(R.id.floating_button_map_tramLine)
        bFloat.setOnClickListener {
            val intent = Intent(this, TramPlansActivity::class.java)
            startActivity(intent)
        }

        if (isNetworkConnected()) {
            runBlocking {
                if (tramLineDao.getTramLines().isEmpty()) {
                    val service = retrofit().create(TramLinesBySearch::class.java)
                    val resultat = service.getlistTramLine()
                    resultat.result.tramways.map {
                        val tramway = TramLine(0, it.code, it.name, it.directions, it.id)
                        tramLineDao.addTramLines(tramway)
                    }
                }
                tramLineDao = database.getTramLineDao()
                val tramStation = tramLineDao.getTramLines()
                progress_bar_tram.visibility = View.GONE
                recyclerviewTram.adapter =
                    TramLineAdapter(
                        tramStation
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