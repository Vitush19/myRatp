package com.example.myratp.adapters

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.myratp.utils.imageMetro
import com.example.myratp.R
import com.example.myratp.utils.imageTraffic
import com.example.myratp.model.Traffic
import kotlinx.android.synthetic.main.traffic_view.view.*

class TrafficAdapter(private val list_traffic: List<Traffic>, private val type: String) :
    RecyclerView.Adapter<TrafficAdapter.trafficViewHolder>() {

    class trafficViewHolder(val trafficView: View) : RecyclerView.ViewHolder(trafficView)

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): TrafficAdapter.trafficViewHolder {
        val layoutInflater: LayoutInflater = LayoutInflater.from(parent.context)
        val view: View = layoutInflater.inflate(R.layout.traffic_view, parent, false)
        return TrafficAdapter.trafficViewHolder(
            view
        )
    }

    override fun getItemCount(): Int = list_traffic.size

    @SuppressLint("SetTextI18n")
    override fun onBindViewHolder(holder: TrafficAdapter.trafficViewHolder, position: Int) {
        val traffic = list_traffic[position]
        holder.trafficView.title_traffic_textview.text = "Statut : ${traffic.title}"
        if(type == "Metro"){
            holder.trafficView.type_image_view.setBackgroundResource(
                imageTraffic(
                    "Métro"
                )
            )
            holder.trafficView.line_image_view.setBackgroundResource(
                imageMetro(
                    "Métro ${traffic.line}"
                )
            )
        }
        if (type == "RER"){
            holder.trafficView.type_image_view.setBackgroundResource(
                imageTraffic(
                    "RER"
                )
            )
        }
        if (type == "Tramway"){
            holder.trafficView.type_image_view.setBackgroundResource(
                imageTraffic(
                    "Tramway"
                )
            )
        }
        holder.trafficView.message_traffic_textview.text = "Info : ${traffic.message}"
        when (traffic.slug) {
            "normal" -> {
                holder.trafficView.traffic_image_view.setBackgroundResource(R.drawable.vert)

            }
            "critical" -> {
                holder.trafficView.traffic_image_view.setBackgroundResource(R.drawable.rouge)

            }
            "normal_trav" -> {
                holder.trafficView.traffic_image_view.setBackgroundResource(R.drawable.travaux)
            }
        }
    }

}