package com.example.myratp.adapters

import android.annotation.SuppressLint
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

class MetroLineAdapter(private val listMetroLines: List<MetroLine>, private val list_traffic: List<Traffic>) :
    RecyclerView.Adapter<MetroLineAdapter.MetroLinesViewHolder>() {

    private val metro3b = list_traffic[3].message
    private val metro7b = list_traffic[8].message

    class MetroLinesViewHolder(val metroLinesView: View) : RecyclerView.ViewHolder(metroLinesView)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MetroLinesViewHolder {
        val layoutInflater: LayoutInflater = LayoutInflater.from(parent.context)
        val view: View = layoutInflater.inflate(R.layout.metroline_view, parent, false)
        return MetroLinesViewHolder(
            view
        )
    }

    override fun getItemCount(): Int = listMetroLines.size

    @SuppressLint("SetTextI18n")
    override fun onBindViewHolder(holder: MetroLinesViewHolder, position: Int) {
        val metroLines = listMetroLines[position]
        holder.metroLinesView.metroline_destination_textview.text =
            "Destination : ${metroLines.direction}"
        holder.metroLinesView.metro_image_view.setBackgroundResource(ImageMetro(metroLines.name))
        for (x in list_traffic.indices) {
            if (metroLines.code == list_traffic[x].line) {
                holder.metroLinesView.metroline_name_textview.text = list_traffic[x].message
            }
            if ("3b" == metroLines.code) {
                holder.metroLinesView.metroline_name_textview.text = metro3b
            }
            if ("7b" == metroLines.code) {
                holder.metroLinesView.metroline_name_textview.text = metro7b
            }
            if (metroLines.code == "Fun") {
                holder.metroLinesView.metroline_name_textview.text =
                    "Informations traffic indisponible"
            }
            if (metroLines.code == "Orv") {
                holder.metroLinesView.metroline_name_textview.text =
                    "Informations traffic indisponible"
            }
        }
        holder.metroLinesView.setOnClickListener {
            val intent = Intent(it.context, MetroStationsActivity::class.java)
            intent.putExtra("code", metroLines.code)
            intent.putExtra("id", metroLines.id_metro)
            it.context.startActivity(intent)
        }
    }

}

