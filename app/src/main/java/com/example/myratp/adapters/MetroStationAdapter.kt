package com.example.myratp.adapters

import android.content.Context
import android.content.Intent
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.content.ContentProviderCompat.requireContext
import androidx.recyclerview.widget.RecyclerView
import androidx.room.Room
import com.example.myratp.R
import com.example.myratp.data.AppDatabase
import com.example.myratp.data.StationsDao
import com.example.myratp.model.Station
import com.example.myratp.ui.timetable.metrolines.ImageMetro
import com.example.myratp.ui.timetable.metrolines.MetroSchedulesActivity
import kotlinx.android.synthetic.main.station_view.view.*
import kotlinx.coroutines.runBlocking

class MetroStationAdapter(val list_stations: List<Station>, val code: String) :
    RecyclerView.Adapter<MetroStationAdapter.MetroStationViewHolder>() {
    class MetroStationViewHolder(val stationsView: View) : RecyclerView.ViewHolder(stationsView)

    private var stationsDao: StationsDao? = null
    private lateinit var context: Context


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MetroStationViewHolder {
        val layoutInflater: LayoutInflater = LayoutInflater.from(parent.context)
        val view: View = layoutInflater.inflate(R.layout.station_view, parent, false)
        context = parent.context

        return MetroStationAdapter.MetroStationViewHolder(
            view
        )
    }

    override fun getItemCount(): Int = list_stations.size


    override fun onBindViewHolder(holder: MetroStationViewHolder, position: Int) {
        val station = list_stations[position]
        holder.stationsView.station_name_textview.text = "Station : ${station.name}"
        holder.stationsView.station_image_view.setBackgroundResource(ImageMetro("$code"))

        val databasesaved =
            Room.databaseBuilder(context, AppDatabase::class.java, "favStation")
                .build()
        stationsDao = databasesaved.getStationsDao()
        holder.stationsView.fav_bouton.setOnClickListener {
            if (station.favoris == false) {
                station.favoris = true
                runBlocking {
                    stationsDao?.addStations(station)
                }
                Log.d("aaa", "$station")

                Toast.makeText(
                    context,
                    "La station a bien été ajouté des favoris",
                    Toast.LENGTH_SHORT
                ).show()

            } else if (station.favoris == true) {
                station.favoris = false
                runBlocking {
                    stationsDao?.deleteStations(station)
                }
                Toast.makeText(
                    context,
                    "La station a bien été supprimé des favoris",
                    Toast.LENGTH_SHORT
                ).show()
            }

            runBlocking {
            val s = stationsDao?.getStations()
            Log.d("aaa", "$s")
            }
        }

        holder.stationsView.setOnClickListener {
            val intent = Intent(it.context, MetroSchedulesActivity::class.java)
            intent.putExtra("code", code)
            intent.putExtra("name", station.name)
            it.context.startActivity(intent)
        }
    }

}