package com.example.myratp.adapters

import android.content.Intent
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.myratp.R
import com.example.myratp.model.MetroLine
import com.example.myratp.model.Traffic
import com.example.myratp.ui.timetable.metrolines.ImageMetro
import com.example.myratp.ui.timetable.metrolines.MetroStationsActivity
import kotlinx.android.synthetic.main.metroline_view.view.*

class MetroLineAdapter(val list_metrolines: List<MetroLine>, val list_traffic: List<Traffic>) :
    RecyclerView.Adapter<MetroLineAdapter.MetroLinesViewHolder>() {

    val metro3b = list_traffic[3].message
    val metro7b = list_traffic[8].message

    class MetroLinesViewHolder(val metrolinesView: View) : RecyclerView.ViewHolder(metrolinesView)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MetroLinesViewHolder {
        val layoutInflater: LayoutInflater = LayoutInflater.from(parent.context)
        val view: View = layoutInflater.inflate(R.layout.metroline_view, parent, false)
        return MetroLinesViewHolder(
            view
        )
    }

    override fun getItemCount(): Int = list_metrolines.size

    override fun onBindViewHolder(holder: MetroLinesViewHolder, position: Int) {
        val metrolines = list_metrolines[position]
        //holder.metrolinesView.metroline_code_textview.text = "Ligne : ${metrolines.code}"
        holder.metrolinesView.metroline_destination_textview.text =
            "Destination : ${metrolines.direction}"
        holder.metrolinesView.metro_image_view.setBackgroundResource(ImageMetro(metrolines.name))
        for (x in list_traffic.indices) {
            if (metrolines.code == list_traffic[x].line) {
                holder.metrolinesView.metroline_name_textview.text = list_traffic[x].message
            }
            if ("3b" == metrolines.code) {
                holder.metrolinesView.metroline_name_textview.text = metro3b
            }
            if ("7b" == metrolines.code) {
                holder.metrolinesView.metroline_name_textview.text = metro7b
            }
            if (metrolines.code == "Fun") {
                holder.metrolinesView.metroline_name_textview.text =
                    "Informations traffic indisponible"
            }
            if (metrolines.code == "Orv") {
                holder.metrolinesView.metroline_name_textview.text =
                    "Informations traffic indisponible"
            }
        }
        holder.metrolinesView.setOnClickListener {
            val intent = Intent(it.context, MetroStationsActivity::class.java)
            intent.putExtra("code", metrolines.code)
            intent.putExtra("id", metrolines.id_metro)

            Log.d("DF", "${metrolines.code}")
            Log.d("DF", "${metrolines.id_metro}")
            it.context.startActivity(intent)
        }
    }

}

