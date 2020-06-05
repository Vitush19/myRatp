package com.example.myratp.adapters.train

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import androidx.room.Room
import com.example.myratp.R
import com.example.myratp.data.AppDatabase
import com.example.myratp.data.StationsDao
import com.example.myratp.model.Station
import com.example.myratp.ui.timetable.trainlines.TrainScheduleActivity
import kotlinx.coroutines.runBlocking
import kotlinx.android.synthetic.main.station_train_view.view.*

class TrainStationAdapter(private val list_stations: List<Station>, val code: String) :
    RecyclerView.Adapter<TrainStationAdapter.TrainStationViewHolder>() {
    class TrainStationViewHolder(val stationsView: View) : RecyclerView.ViewHolder(stationsView)


    private var stationsDao: StationsDao? = null
    private lateinit var context: Context

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TrainStationViewHolder {
        val layoutInflater: LayoutInflater = LayoutInflater.from(parent.context)
        val view: View = layoutInflater.inflate(R.layout.station_train_view, parent, false)
        context = parent.context
        return TrainStationViewHolder(
            view
        )
    }

    override fun getItemCount(): Int = list_stations.size

    @SuppressLint("SetTextI18n")
    override fun onBindViewHolder(holder: TrainStationViewHolder, position: Int) {
        val station = list_stations[position]
        holder.stationsView.station_name_textview_train.text = "Station : ${station.name}"

        val databaseSaved =
            Room.databaseBuilder(context, AppDatabase::class.java, "stationtrain")
                .build()

        stationsDao = databaseSaved.getStationsDao()

        if (!station.favoris) {
            holder.stationsView.fav_bouton_train.setBackgroundResource(R.drawable.ic_favorite_border_blue_24dp)
        } else if (station.favoris) {
            holder.stationsView.fav_bouton_train.setBackgroundResource(R.drawable.ic_favorite_blue_24dp)
        }
        holder.stationsView.fav_bouton_train.setOnClickListener {
            if (!station.favoris) {
                holder.stationsView.fav_bouton_train.setBackgroundResource(R.drawable.ic_favorite_blue_24dp)
                station.favoris = true
                runBlocking {
                    stationsDao?.updateStations(station)
                }
                val inflater = LayoutInflater.from(context)
                val v : View = holder.itemView
                val custom = v.findViewById<LinearLayout>(R.id.layout_toast_custom_add)
                val toastView = inflater.inflate(
                    R.layout.custom_toast_add,
                    custom
                )
                with(Toast(context)) {
                    duration = Toast.LENGTH_SHORT
                    view = toastView
                    show()
                }

            } else if (station.favoris) {
                holder.stationsView.fav_bouton_train.setBackgroundResource(R.drawable.ic_favorite_border_blue_24dp)
                station.favoris = false
                runBlocking {
                    stationsDao?.updateStations(station)
                }
                val inflater = LayoutInflater.from(context)
                val v : View = holder.itemView
                val custom = v.findViewById<LinearLayout>(R.id.layout_toast_custom_delete)
                val toastView = inflater.inflate(
                    R.layout.custom_toast_delete,
                    custom
                )
                with(Toast(context)) {
                    duration = Toast.LENGTH_SHORT
                    view = toastView
                    show()
                }
            }
        }

        holder.stationsView.setOnClickListener {
            val intent = Intent(it.context, TrainScheduleActivity::class.java)
            intent.putExtra("code", code)
            intent.putExtra("name", station.name)
            it.context.startActivity(intent)
        }

    }

}