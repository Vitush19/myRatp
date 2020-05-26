package com.example.myratp.adapters

import android.annotation.SuppressLint
import android.content.Intent
import android.telephony.mbms.StreamingServiceInfo
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.myratp.R
import com.example.myratp.model.Station
import com.example.myratp.ui.timetable.trainlines.TrainScheduleActivity
import kotlinx.android.synthetic.main.station_metro_view.view.*
import kotlinx.android.synthetic.main.station_train_view.view.*

class TrainStationAdapter(private val list_stations: List<Station>, val code: String) :
    RecyclerView.Adapter<TrainStationAdapter.TrainStationViewHolder>() {
    class TrainStationViewHolder(val stationsView: View) : RecyclerView.ViewHolder(stationsView)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TrainStationViewHolder {
        val layoutInflater: LayoutInflater = LayoutInflater.from(parent.context)
        val view: View = layoutInflater.inflate(R.layout.station_train_view, parent, false)
        return TrainStationAdapter.TrainStationViewHolder(
            view
        )
    }

    override fun getItemCount(): Int = list_stations.size

    @SuppressLint("SetTextI18n")
    override fun onBindViewHolder(holder: TrainStationViewHolder, position: Int) {
        val station = list_stations[position]
        holder.stationsView.station_name_textview_train.text = "Station : ${station.name}"

        holder.stationsView.setOnClickListener {
            val intent = Intent(it.context, TrainScheduleActivity::class.java)
            intent.putExtra("code", code)
            intent.putExtra("name", station.name)
            it.context.startActivity(intent)
        }

    }

}