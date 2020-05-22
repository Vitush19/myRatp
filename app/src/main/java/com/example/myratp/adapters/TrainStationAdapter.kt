package com.example.myratp.adapters

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.myratp.R
import com.example.myratp.model.Station
import kotlinx.android.synthetic.main.station_metro_view.view.*

class TrainStationAdapter(private val list_stations: List<Station>) :
    RecyclerView.Adapter<TrainStationAdapter.TrainStationViewHolder>() {
    class TrainStationViewHolder(val stationsView: View) : RecyclerView.ViewHolder(stationsView)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TrainStationViewHolder {
        val layoutInflater: LayoutInflater = LayoutInflater.from(parent.context)
        val view: View = layoutInflater.inflate(R.layout.station_metro_view, parent, false)
        return TrainStationAdapter.TrainStationViewHolder(
            view
        )
    }

    override fun getItemCount(): Int = list_stations.size

    @SuppressLint("SetTextI18n")
    override fun onBindViewHolder(holder: TrainStationViewHolder, position: Int) {
        val lines = list_stations[position]
        holder.stationsView.station_name_textview.text = "Station : ${lines.name}"

    }

}