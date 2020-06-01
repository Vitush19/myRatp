package com.example.myratp.ui.timetable.noctilien

import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.MenuItem
import android.view.View
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.appcompat.widget.Toolbar
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.room.Room
import com.example.myratp.NoctiPlansActivity
import com.example.myratp.R
import com.example.myratp.adapters.NoctilienAdapter
import com.example.myratp.data.AppDatabase
import com.example.myratp.data.NoctilienDao
import com.example.myratp.model.Noctilien
import com.example.myratp.retrofit
import com.google.android.material.floatingactionbutton.FloatingActionButton
import kotlinx.android.synthetic.main.activity_nocti_time.*
import kotlinx.coroutines.runBlocking
import java.util.*

class NoctilienTimeActivity : AppCompatActivity() {

    private var noctilienDao: NoctilienDao? = null

    @RequiresApi(Build.VERSION_CODES.M)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_nocti_time)

        val toolbar: Toolbar = findViewById(R.id.toolbar_nocti_time)
        toolbar.setTitleTextColor(Color.parseColor("#F8F7F2"))
        toolbar.setNavigationIcon(R.drawable.ic_arrow_back_white_24dp)
        toolbar.title = "Noctilien"
        setSupportActionBar(toolbar)

        val recyclerviewNocti = findViewById<RecyclerView>(R.id.activities_recyclerview_nocti)
        recyclerviewNocti.layoutManager =
            LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)

        val database = Room.databaseBuilder(this, AppDatabase::class.java, "allnoctilien")
            .build()
        noctilienDao = database.getNoctilienDao()

        val bFloat = findViewById<FloatingActionButton>(R.id.floating_button_map_nocti)
        bFloat.setOnClickListener {
            val intent = Intent(this, NoctiPlansActivity::class.java)
            startActivity(intent)
        }

        val notif = findViewById<CardView>(R.id.cardview_noctilien_line)
        val rightNow: Calendar = Calendar.getInstance()
        val currentHourIn24Format: Int = rightNow.get(Calendar.HOUR_OF_DAY)
        if(currentHourIn24Format > 22 || currentHourIn24Format < 6){
            notif.visibility = View.GONE
        }

        if (isNetworkConnected()) {
            runBlocking {
                if (noctilienDao!!.getNoctilien().isEmpty()) {
                    val service = retrofit().create(NoctiLineBySearch::class.java)
                    val resultat = service.getlistNoctiLine()
                    resultat.result.noctiliens.map {
                        val nocti = Noctilien(0, it.code, it.name, it.directions, it.id)
                        noctilienDao?.addNoctilien(nocti)
                    }
                }
                noctilienDao = database.getNoctilienDao()
                val noc = noctilienDao?.getNoctilien()
                progress_bar_nocti_ligne.visibility = View.GONE
                recyclerviewNocti.adapter =
                    NoctilienAdapter(noc ?: emptyList())
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