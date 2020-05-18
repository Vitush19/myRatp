package com.example.myratp.adapters

import android.content.Context
import android.content.Intent
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import androidx.room.Room
import com.example.myratp.R
import com.example.myratp.data.AppDatabase
import com.example.myratp.data.StationsDao
import com.example.myratp.model.Station
import com.example.myratp.ui.timetable.metrolines.MetroSchedulesActivity
import kotlinx.android.synthetic.main.station_view.view.*
import kotlinx.coroutines.runBlocking

class StationAdapter(val list_stations: List<Station>) :
    RecyclerView.Adapter<StationAdapter.StationViewHolder>() {
    class StationViewHolder(val stationsView: View) : RecyclerView.ViewHolder(stationsView)

    private var stationsDao: StationsDao? = null
    private lateinit var context: Context

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): StationViewHolder {
        val layoutInflater: LayoutInflater = LayoutInflater.from(parent.context)
        val view: View = layoutInflater.inflate(R.layout.station_view, parent, false)
        context = parent.context
        return StationAdapter.StationViewHolder(
            view
        )
    }

    override fun getItemCount(): Int = list_stations.size


    override fun onBindViewHolder(holder: StationViewHolder, position: Int) {
        val lines = list_stations[position]
        holder.stationsView.station_name_textview.text = "Station : ${lines.name}"

        val databasesaved =
            Room.databaseBuilder(context, AppDatabase::class.java, "favStation")
                .build()
        stationsDao = databasesaved.getStationsDao()
        holder.stationsView.fav_bouton.setOnClickListener {
            if (lines.favoris == false) {
                lines.favoris = true
                runBlocking {
                    stationsDao?.addStations(lines)
                }
                Log.d("aaa", "$lines")

                Toast.makeText(
                    context,
                    "La station a bien été ajouté des favoris",
                    Toast.LENGTH_SHORT
                ).show()

            } else if (lines.favoris == true) {
                lines.favoris = false
                runBlocking {
                    stationsDao?.deleteStations(lines)
                }
                Toast.makeText(
                    context,
                    "La station a bien été supprimé des favoris",
                    Toast.LENGTH_SHORT
                ).show()
            }
        }

        holder.stationsView.setOnClickListener {
            val intent = Intent(it.context, MetroSchedulesActivity::class.java)
            intent.putExtra("code", lines.id_ligne)
            intent.putExtra("name", lines.name)
            it.context.startActivity(intent)
        }

    }

}