package com.example.myratp.adapters

import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.myratp.R
import com.example.myratp.model.Traffic
import com.example.myratp.ui.timetable.metrolines.ImageMetro
import kotlinx.android.synthetic.main.metroline_view.view.metro_image_view
import kotlinx.android.synthetic.main.traffic_view.view.*

class TrafficMetroAdapter(val list_traffic: List<Traffic>) :
    RecyclerView.Adapter<TrafficMetroAdapter.trafficViewHolder>() {

    class trafficViewHolder(val trafficView: View) : RecyclerView.ViewHolder(trafficView)

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): TrafficMetroAdapter.trafficViewHolder {
        val layoutInflater: LayoutInflater = LayoutInflater.from(parent.context)
        val view: View = layoutInflater.inflate(R.layout.traffic_view, parent, false)
        return TrafficMetroAdapter.trafficViewHolder(
            view
        )
    }

    override fun getItemCount(): Int = list_traffic.size

    override fun onBindViewHolder(holder: TrafficMetroAdapter.trafficViewHolder, position: Int) {
        val traffic = list_traffic[position]
        Log.d("test", "$traffic")
        holder.trafficView.title_traffic_textview.text = "statut : ${traffic.title}"
        holder.trafficView.metro_image_view.setBackgroundResource(ImageMetro("Métro ${traffic.line}"))
        holder.trafficView.message_traffic_textview.text = "Info : ${traffic.message}"
        if (traffic.slug == "normal") {
            holder.trafficView.traffic_image_view.setBackgroundResource(R.drawable.vert)

        } else if (traffic.slug == "critical") {
            holder.trafficView.traffic_image_view.setBackgroundResource(R.drawable.rouge)

        } else if (traffic.slug == "normal_trav") {
            holder.trafficView.traffic_image_view.setBackgroundResource(R.drawable.travaux)
        }

    }

}