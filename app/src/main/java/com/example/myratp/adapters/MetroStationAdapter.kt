package com.example.myratp.adapters

import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.myratp.R
import com.example.myratp.model.Station
import com.example.myratp.ui.timetable.metrolines.MetroSchedulesActivity
import kotlinx.android.synthetic.main.station_view.view.*

class MetroStationAdapter(val list_stations: List<Station>, val code: String) :
    RecyclerView.Adapter<MetroStationAdapter.MetroStationViewHolder>() {
    class MetroStationViewHolder(val stationsView: View) : RecyclerView.ViewHolder(stationsView)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MetroStationViewHolder {
        val layoutInflater: LayoutInflater = LayoutInflater.from(parent.context)
        val view: View = layoutInflater.inflate(R.layout.station_view, parent, false)
        return MetroStationAdapter.MetroStationViewHolder(
            view
        )
    }

    override fun getItemCount(): Int = list_stations.size


    override fun onBindViewHolder(holder: MetroStationViewHolder, position: Int) {
        val lines = list_stations[position]
        holder.stationsView.station_name_textview.text = "Station : ${lines.name}"

        holder.stationsView.setOnClickListener {
            val intent = Intent(it.context, MetroSchedulesActivity::class.java)
            intent.putExtra("code", code)
            intent.putExtra("name", lines.name)
            it.context.startActivity(intent)
        }
    }

}