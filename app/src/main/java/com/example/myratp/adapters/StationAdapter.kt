package com.example.myratp.adapters

import android.annotation.SuppressLint
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
import com.example.myratp.model.Type
import com.example.myratp.ui.timetable.buslines.BusSchedulesActivity
import com.example.myratp.ui.timetable.metrolines.ImageMetro
import com.example.myratp.ui.timetable.metrolines.MetroSchedulesActivity
import com.example.myratp.ui.timetable.trainlines.TrainScheduleActivity
import kotlinx.android.synthetic.main.station_view.view.*
import kotlinx.coroutines.runBlocking

class StationAdapter(private val list_stations: List<Station>) :
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


    @SuppressLint("SetTextI18n")
    override fun onBindViewHolder(holder: StationViewHolder, position: Int) {
        val station = list_stations[position]
        holder.stationsView.station_name_textview.text = "Station : ${station.name}"

        holder.stationsView.image_type.setImageResource(
            when(station.type){
                Type.Metro-> R.drawable.metro_bleu
                Type.Bus-> R.drawable.bus
                Type.Train-> R.drawable.rer
            }
        )
        var databaseSaved =
            Room.databaseBuilder(context, AppDatabase::class.java, "stationtrain")
                .build()
         if (station.type == Type.Metro){
             holder.stationsView.station_image_view.setBackgroundResource(ImageMetro(station.id_ligne))
             databaseSaved =
                 Room.databaseBuilder(context, AppDatabase::class.java, "stationmetro")
                     .build()
         }else if (station.type == Type.Bus) {
             databaseSaved =
                 Room.databaseBuilder(context, AppDatabase::class.java, "stationbus")
                     .build()
         }







        stationsDao = databaseSaved.getStationsDao()

        if (!station.favoris) {
            holder.stationsView.fav_bouton.setBackgroundResource(R.drawable.ic_favorite_border_blue_24dp)
        } else if (station.favoris) {
            holder.stationsView.fav_bouton.setBackgroundResource(R.drawable.ic_favorite_blue_24dp)
        }
        holder.stationsView.fav_bouton.setOnClickListener {
            if (!station.favoris) {
                holder.stationsView.fav_bouton.setBackgroundResource(R.drawable.ic_favorite_blue_24dp)
                station.favoris = true
                runBlocking {
                    stationsDao?.updateStations(station)
                }
                Toast.makeText(
                    context,
                    "La station a bien été ajouté des favoris",
                    Toast.LENGTH_SHORT
                ).show()
            } else if (station.favoris) {
                holder.stationsView.fav_bouton.setBackgroundResource(R.drawable.ic_favorite_border_blue_24dp)
                station.favoris = false
                runBlocking {
                    stationsDao?.updateStations(station)
                }
                Toast.makeText(
                    context,
                    "La station a bien été supprimé des favoris",
                    Toast.LENGTH_SHORT
                ).show()
            }
        }

        holder.stationsView.setOnClickListener {
            var intent = Intent(it.context, MetroSchedulesActivity::class.java)
//            when(station.type){
//                Type.Metro-> intent = Intent(it.context, MetroSchedulesActivity::class.java)
//                Type.Bus-> intent = Intent(it.context, BusSchedulesActivity::class.java)
//                Type.Train-> intent = Intent(it.context, TrainScheduleActivity::class.java)
//            }
//            intent.putExtra("code", station.id_ligne)
//            intent.putExtra("name", station.name)
//            it.context.startActivity(intent)

            if(station.type == Type.Metro){
                intent = Intent(it.context, MetroSchedulesActivity::class.java)
                intent.putExtra("code", station.id_ligne)
                intent.putExtra("name", station.name)
                intent.putExtra("correspondance", station.correspondance)
//                it.context.startActivity(intent)

            }else if(station.type == Type.Bus) {
                intent = Intent(it.context, BusSchedulesActivity::class.java)
                intent.putExtra("code", station.id_ligne)
                intent.putExtra("name", station.name)


            }else if(station.type == Type.Train) {
                intent = Intent(it.context, TrainScheduleActivity::class.java)
                intent.putExtra("code", station.id_ligne)
                intent.putExtra("name", station.name)
//                it.context.startActivity(intent)
            }
            it.context.startActivity(intent)
        }
    }
}