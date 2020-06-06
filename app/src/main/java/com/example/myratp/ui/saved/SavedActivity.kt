package com.example.myratp.ui.saved

import android.graphics.Color
import android.os.Bundle
import android.view.MenuItem
import android.view.View
import android.widget.RelativeLayout
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.room.Room
import com.example.myratp.R
import com.example.myratp.adapters.StationAdapter
import com.example.myratp.data.AppDatabase
import com.example.myratp.data.StationsDao
import kotlinx.coroutines.runBlocking

class SavedActivity : AppCompatActivity(){


    private lateinit var stationsDao: StationsDao

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_saved)

        val toolbar: Toolbar = findViewById(R.id.toolbar_saved)
        toolbar.setTitleTextColor(Color.parseColor("#F8F7F2"))
        toolbar.setNavigationIcon(R.drawable.ic_arrow_back_white_24dp)
        toolbar.title = "Vos Stations"
        setSupportActionBar(toolbar)

        val noFav = findViewById<RelativeLayout>(R.id.linear_no_fav)

        val stationRecyclerview =
            findViewById<View>(R.id.activities_recyclerview_station_fav) as RecyclerView
        stationRecyclerview.layoutManager =
            LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)

        val database =
            Room.databaseBuilder(
                this,
                AppDatabase::class.java,
                "stationmetro"
            )
                .build()

        val databaseBus =
            Room.databaseBuilder(
                this,
                AppDatabase::class.java,
                "stationbus"
            )
                .build()

        val databaseTrain =
            Room.databaseBuilder(
                this,
                AppDatabase::class.java,
                "stationtrain"
            )
                .build()

        val databaseTram =
            Room.databaseBuilder(
                this,
                AppDatabase::class.java,
                "stationtram"
            )
                .build()

        val databaseNotilien =
            Room.databaseBuilder(
                this,
                AppDatabase::class.java,
                "stationnoctilien"
            )
                .build()

        runBlocking {

            stationsDao = database.getStationsDao()
            val stationFav = stationsDao.getStationFav(true)

            stationsDao = databaseBus.getStationsDao()
            val stationFavb = stationsDao.getStationFav(true)

            stationsDao = databaseTrain.getStationsDao()
            val stationFavt = stationsDao.getStationFav(true)

            stationsDao = databaseTram.getStationsDao()
            val stationFavtram = stationsDao.getStationFav(true)

            stationsDao = databaseNotilien.getStationsDao()
            val stationFavN = stationsDao.getStationFav(true)

            val favtotal = stationFav + stationFavb + stationFavt + stationFavtram + stationFavN

            if (favtotal.isNotEmpty()) {
                noFav.visibility = View.GONE
            }

            stationRecyclerview.adapter = StationAdapter(favtotal, this@SavedActivity)

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
}