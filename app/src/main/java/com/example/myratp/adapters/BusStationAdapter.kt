package com.example.myratp.adapters

import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.myratp.R
import com.example.myratp.model.Station
import com.example.myratp.ui.timetable.buslines.BusSchedulesActivity
import kotlinx.android.synthetic.main.station_bus_view.view.*

class BusStationAdapter(val list_stations: List<Station>, val code: String) :
    RecyclerView.Adapter<BusStationAdapter.BusStationViewHolder>() {
    class BusStationViewHolder(val stationsView: View) : RecyclerView.ViewHolder(stationsView)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BusStationViewHolder {
        val layoutInflater: LayoutInflater = LayoutInflater.from(parent.context)
        val view: View = layoutInflater.inflate(R.layout.station_bus_view, parent, false)
        return BusStationAdapter.BusStationViewHolder(
            view
        )
    }

    override fun getItemCount(): Int = list_stations.size


    override fun onBindViewHolder(holder: BusStationViewHolder, position: Int) {
        val lines = list_stations[position]
        holder.stationsView.station_name_textview_bus.text = "Station : ${lines.name}"

        holder.stationsView.setOnClickListener {
            val intent = Intent(it.context, BusSchedulesActivity::class.java)
            intent.putExtra("code", code)
            intent.putExtra("name", lines.name)
            it.context.startActivity(intent)
        }
    }

}